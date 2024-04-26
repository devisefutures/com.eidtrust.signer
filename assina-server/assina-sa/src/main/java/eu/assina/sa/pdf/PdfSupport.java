package eu.assina.sa.pdf;

import eu.assina.sa.client.ClientContext;
import eu.assina.sa.model.AssinaSigner;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Extracts content and adds signature to PDF
 * Taken from the pdfbox examples
 */
public class PdfSupport {

    private AssinaSigner signer;

    public PdfSupport(AssinaSigner contentSigner) {
        this.signer = contentSigner;
    }

    public byte[] signPdfContent(InputStream content, ClientContext context) throws IOException, NoSuchAlgorithmException {
        byte[] pdfContentBytes = IOUtils.toByteArray(content);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] pdfHash = digest.digest(pdfContentBytes);
        byte[] signedHash = signer.signHash(pdfHash, context);
        return signedHash;
    }

    /**
     * Signs the given PDF file.
     *
     * @param inFile  input PDF file
     * @param outFile output PDF file
     * @throws IOException if the input file could not be read
     */
    public void signDetached(File inFile, File outFile) throws IOException, NoSuchAlgorithmException {
        if (inFile == null || !inFile.exists()) {
            throw new FileNotFoundException("Document for signing does not exist");
        }

        FileOutputStream fos = new FileOutputStream(outFile);

        // sign
        PDDocument doc = null;
        try {
            doc = PDDocument.load(inFile);
            signDetached(doc, fos);
        } finally {
            IOUtils.closeQuietly(doc);
            IOUtils.closeQuietly(fos);
        }
    }

    public void signDetached(PDDocument document, OutputStream output) throws IOException, NoSuchAlgorithmException {
        int accessPermissions = getMDPPermission(document);
        if (accessPermissions == 1) {
            throw new IllegalStateException("No changes to the document are permitted due to DocMDP transform parameters dictionary");
        }

        // create signature dictionary
        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);

        // the signing date, needed for valid signature
        signature.setSignDate(Calendar.getInstance());

        // Optional: certify
        if (accessPermissions == 0) {
            setMDPPermission(document, signature, 2);
        }

        ClientContext context = signer.prepCredenital();

        signature.setName(context.getSubject());
        signature.setLocation("Braga, Portugal");
        signature.setReason("Signing documents with Assina");

        document.addSignature(signature);
        ExternalSigningSupport externalSigning = document.saveIncrementalForExternalSigning(output);
        // invoke external signature service
        byte[] cmsSignature = signPdfContent(externalSigning.getContent(), context);

        // set signature bytes received from the service
        externalSigning.setSignature(cmsSignature);
    }

    // Utilities for permissions Copied from the Apache Pdf-Box examples

    /**
     * Get the access permissions granted for this document in the DocMDP transform parameters
     * dictionary. Details are described in the table "Entries in the DocMDP transform parameters
     * dictionary" in the PDF specification.
     *
     * @param doc document.
     * @return the permission value. 0 means no DocMDP transform parameters dictionary exists. Other
     * return values are 1, 2 or 3. 2 is also returned if the DocMDP transform parameters dictionary
     * is found but did not contain a /P entry, or if the value is outside the valid range.
     */
    public static int getMDPPermission(PDDocument doc) {
        COSBase base = doc.getDocumentCatalog().getCOSObject().getDictionaryObject(COSName.PERMS);
        if (base instanceof COSDictionary) {
            COSDictionary permsDict = (COSDictionary) base;
            base = permsDict.getDictionaryObject(COSName.DOCMDP);
            if (base instanceof COSDictionary) {
                COSDictionary signatureDict = (COSDictionary) base;
                base = signatureDict.getDictionaryObject(COSName.REFERENCE);
                if (base instanceof COSArray) {
                    COSArray refArray = (COSArray) base;
                    for (int i = 0; i < refArray.size(); ++i) {
                        base = refArray.getObject(i);
                        if (base instanceof COSDictionary) {
                            COSDictionary sigRefDict = (COSDictionary) base;
                            if (COSName.DOCMDP.equals(sigRefDict.getDictionaryObject(COSName.TRANSFORM_METHOD))) {
                                base = sigRefDict.getDictionaryObject(COSName.TRANSFORM_PARAMS);
                                if (base instanceof COSDictionary) {
                                    COSDictionary transformDict = (COSDictionary) base;
                                    int accessPermissions = transformDict.getInt(COSName.P, 2);
                                    if (accessPermissions < 1 || accessPermissions > 3) {
                                        accessPermissions = 2;
                                    }
                                    return accessPermissions;
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Set the "modification detection and prevention" permissions granted for this document in the
     * DocMDP transform parameters dictionary. Details are described in the table "Entries in the
     * DocMDP transform parameters dictionary" in the PDF specification.
     *
     * @param doc               The document.
     * @param signature         The signature object.
     * @param accessPermissions The permission value (1, 2 or 3).
     * @throws IOException if a signature exists.
     */
    public static void setMDPPermission(PDDocument doc, PDSignature signature, int accessPermissions) throws IOException {
        for (PDSignature sig : doc.getSignatureDictionaries()) {
            // "Approval signatures shall follow the certification signature if one is present"
            // thus we don't care about timestamp signatures
            if (COSName.DOC_TIME_STAMP.equals(sig.getCOSObject().getItem(COSName.TYPE))) {
                continue;
            }
            if (sig.getCOSObject().containsKey(COSName.CONTENTS)) {
                throw new IOException("DocMDP transform method not allowed if an approval signature exists");
            }
        }

        COSDictionary sigDict = signature.getCOSObject();

        // DocMDP specific stuff
        COSDictionary transformParameters = new COSDictionary();
        transformParameters.setItem(COSName.TYPE, COSName.TRANSFORM_PARAMS);
        transformParameters.setInt(COSName.P, accessPermissions);
        transformParameters.setName(COSName.V, "1.2");
        transformParameters.setNeedToBeUpdated(true);

        COSDictionary referenceDict = new COSDictionary();
        referenceDict.setItem(COSName.TYPE, COSName.SIG_REF);
        referenceDict.setItem(COSName.TRANSFORM_METHOD, COSName.DOCMDP);
        referenceDict.setItem(COSName.DIGEST_METHOD, COSName.getPDFName("SHA1"));
        referenceDict.setItem(COSName.TRANSFORM_PARAMS, transformParameters);
        referenceDict.setNeedToBeUpdated(true);

        COSArray referenceArray = new COSArray();
        referenceArray.add(referenceDict);
        sigDict.setItem(COSName.REFERENCE, referenceArray);
        referenceArray.setNeedToBeUpdated(true);

        // Catalog
        COSDictionary catalogDict = doc.getDocumentCatalog().getCOSObject();
        COSDictionary permsDict = new COSDictionary();
        catalogDict.setItem(COSName.PERMS, permsDict);
        permsDict.setItem(COSName.DOCMDP, signature);
        catalogDict.setNeedToBeUpdated(true);
        permsDict.setNeedToBeUpdated(true);
    }
}
