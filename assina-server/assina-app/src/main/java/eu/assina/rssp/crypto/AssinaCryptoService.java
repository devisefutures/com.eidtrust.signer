package eu.assina.rssp.crypto;

import eu.assina.rssp.common.config.CSCProperties;
import eu.assina.rssp.common.error.ApiException;
import eu.assina.rssp.common.error.AssinaError;
import eu.assina.rssp.common.model.AssinaCredential;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class AssinaCryptoService {

    private static final Logger log = LoggerFactory.getLogger(AssinaCryptoService.class);

    private final CertificateGenerator generator;
    private final CryptoSigner cryptoSigner;
    private final CryptoConfig config;
    private final PemConverter pemConverter;

    public AssinaCryptoService(@Autowired CSCProperties cscProperties) {
        this.config = cscProperties.getCrypto();
        this.cryptoSigner = new CryptoSigner(config);
        this.generator = new CertificateGenerator(config);
        this.pemConverter = new PemConverter(config);
    }


    public AssinaCredential createCredential(String owner, String subjectDN)
    {
        try {
            final KeyPair keyPair = generator.generateKeyPair();
            final X509Certificate selfSignedCert =
                    generator.createSelfSignedCert(keyPair, subjectDN);
            AssinaCredential credential = new AssinaCredential();
            credential.setKeyAlgorithmOIDs(generator.getKeyAlgorithmOIDs());
            credential.setKeyBitLength(generator.getKeyBitLength());
            credential.setECDSACurveOID(generator.getECSDACurveOID());
            credential.setKeyEnabled(true); // enabled by definition
            // convert keys and cert to Base64 strings
            credential.setCertificate(pemConverter.certificateToString(selfSignedCert));
            credential.setPrivateKey(pemConverter.privateKeyToString(keyPair.getPrivate()));
            credential.setPublicKey(pemConverter.publicKeyToString(keyPair.getPublic()));


            credential.setOwner(owner);
            return credential;
        }
        catch (Exception e) {
            throw new ApiException(AssinaError.FailedCreatingCredential, e);
        }
    }

    public byte[] signWithPemCertificate(byte[] dataToSign, String pemCertificate, String pemSigningKey,
                                         String signingAlgo, String signingAlgoParams) {
        // TODO validate signingAlgo against the signing algo params
        final X509Certificate x509Certificate = pemToX509Certificate(pemCertificate);
        final PrivateKey privateKey = pemToPrivateKey(pemSigningKey);
        try {
            final byte[] bytes = cryptoSigner.signData(dataToSign, x509Certificate, privateKey);
            return bytes;
        } catch (CertificateEncodingException | IOException | CMSException | OperatorCreationException e) {
            throw new ApiException(AssinaError.FailedSigningData, e);
        }
    }

    /**
     * Unmarshall the PEM string (Base64) form of the certificate into an X509Certificate object
     * @param pemCertificate
     * @return proper X509Certificate object
     */
    public X509Certificate pemToX509Certificate(String pemCertificate) {
        try {
            final X509Certificate x509Certificate = pemConverter.stringToCertificate(pemCertificate);
            return x509Certificate;
        } catch (IOException | CertificateException e) {
            throw new ApiException(AssinaError.FailedUnmarshallingPEM, e);
        }
    }

    /**
     * Unmarshall the PEM string (Base64) form of the certificate into an X509Certificate object
     * @param pemKey
     * @return proper X509Certificate object
     */
    public PrivateKey pemToPrivateKey(String pemKey) {
        try {
            final PrivateKey privateKey = pemConverter.stringToPrivateKey(pemKey);
            return privateKey;
        } catch (IOException | PKCSException e) {
            throw new ApiException(AssinaError.FailedUnmarshallingPEM, e);
        }
    }

    public boolean isCertificateExpired(X509Certificate x509Certificate) {
        return generator.isCertificateExpired(x509Certificate);
    }
}
