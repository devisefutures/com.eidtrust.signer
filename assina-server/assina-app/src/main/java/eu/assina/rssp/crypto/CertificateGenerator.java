package eu.assina.rssp.crypto;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Generates key pairs and self-signed certificates
 */
public class CertificateGenerator {
	private List<String> keyAlgorithmOIDs;
	private CryptoConfig config;
	private boolean initialized;
	KeyPairGenerator keyPairGenerator;

	private static final Logger log = LoggerFactory.getLogger(CertificateGenerator.class);

	public CertificateGenerator(CryptoConfig config) {
		this.config = config;
		initialized = false;
	}

	// lazy init
	private void init() {
		if (!initialized) {
			try {
				if (!"RSA".equalsIgnoreCase(config.getKeyAlgorithm())) {
					String msg = "Not YET Implemented support for non RSA algorithms";
					log.error(msg);
					throw new IllegalArgumentException(msg);
				}
				// this is not as robust as using a library but works the same way as the sun
				// library sun.security.x509.AlgorithmId.checkKeyAndSigAlgMatch

				// TODO restore the SUN code reference or find the proper BC way to get the OID
				// And to confirm that the key sig matches
				if (config.getSignatureAlgorithm() == null ||
							!config.getSignatureAlgorithm().toLowerCase().endsWith("withrsa")) {
					String error = "Assina Configuration Error: signatureAlgorithm is incompatible with keyAlgorithm";
					log.error(error);
					throw new IllegalArgumentException(error);
				}
				final ASN1ObjectIdentifier rsaIdentifier = PKCSObjectIdentifiers.rsaEncryption;
				String rsaOID = rsaIdentifier.toString();
				keyAlgorithmOIDs = Collections.singletonList(rsaOID); // TODO could there be more?

				keyPairGenerator = KeyPairGenerator.getInstance(config.getKeyAlgorithm());
				keyPairGenerator.initialize(config.getKeySize(), new SecureRandom());

				/*
				// NOTE: This is a SUN class - it makes this code JRE dependent it also has
				// compilation problems with Java 11
				final sun.security.x509.AlgorithmId
						algorithmId = sun.security.x509.AlgorithmId.get(config.getKeyAlgorithm());

				// TODO could there be more keys?
				keyAlgorithmOIDs = Collections.singletonList(algorithmId.getOID().toString());

				// throws IllegalArgument exception if the key and signature algorithms are incompatible
				try {
					sun.security.x509.AlgorithmId.checkKeyAndSigAlgMatch(config.getKeyAlgorithm(), config.getSignatureAlgorithm());
				} catch (IllegalArgumentException e) {
					log.error("Assina Configuration Error: signatureAlgorithm is incompatible with " + "keyAlgorithm", e);
					// re throw the exception - we cannot recover from this
					throw e;
				}
				 */
			} catch (NoSuchAlgorithmException nsae) {
				log.error("Failed to init crypto", nsae);
				throw new IllegalArgumentException(nsae);
			} finally {
				initialized = true;
			}
		}
	}

	public List<String> getKeyAlgorithmOIDs() {
		init();
		return keyAlgorithmOIDs;
	}

	public int getKeyBitLength() {
		return config.getKeySize();
	}

	public String getECSDACurveOID() {
		// TODO update this if we ever use ECSD vs RSA
		if (!"RSA".equalsIgnoreCase(config.getKeyAlgorithm())) {
			throw new IllegalArgumentException("Not YET Implemented support for non RSA algorithms");
		}
		// null is correct return value for RSA algos: only EC (elliptic curve)
		// algorithms have curves
		return null;
	}

	public KeyPair generateKeyPair() throws Exception {
	    init();
		KeyPair pair = keyPairGenerator.generateKeyPair();
		return pair;
	}

	/**
	 * Generates an X509v3 (per the CSC spec) self-signed certificate for use in signing document
	 * hashes.
     * The certicate is valid from the moment this
	 *
	 * @param keyPair public/private key pair accompanying the certificate.
	 * @param subjectName distinguisned name for the certificate subject - usually username or email
	 *
	 * @return
	 * @throws OperatorCreationException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public X509Certificate createSelfSignedCert(KeyPair keyPair, String subjectName)
			throws OperatorCreationException, CertificateException, IOException {
		init();
		Provider bcProvider = new BouncyCastleProvider();
		Security.addProvider(bcProvider);

		long now = System.currentTimeMillis();
		Date startDate = nowUTC();

		X500Name subjectDN = new X500Name("CN=" + subjectName);
		X500Name issuerDN = new X500Name("CN=" + config.getCertificateIssuer());

		BigInteger certSerialNumber = new BigInteger(Long.toString(now)); // <-- Using the current timestamp as the certificate serial number

		// calculate the timestamp for the end date
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, config.getMonthsValidity());
		Date endDate = calendar.getTime();

		String signatureAlgorithm = config.getSignatureAlgorithm();
		ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());

		// Use X509v3 Certificates per the CSC Standard
		JcaX509v3CertificateBuilder certBuilder =
				new JcaX509v3CertificateBuilder(issuerDN, certSerialNumber, startDate, endDate,
						subjectDN, keyPair.getPublic());

		// Extensions --------------------------

		// Basic Constraints
        // TODO determine if basic constraints are necessary
		BasicConstraints basicConstraints = new BasicConstraints(true); // <-- true for CA, false for EndEntity
		certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints); // Basic Constraints is usually marked as critical.

		final X509Certificate certificate =
				new JcaX509CertificateConverter().setProvider(bcProvider).getCertificate(certBuilder.build(contentSigner));
		return certificate;
	}

	public boolean isCertificateExpired(X509Certificate x509Certificate) {
	    return x509Certificate.getNotAfter().before(nowUTC());
	}

	private Date nowUTC() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return calendar.getTime();
	}
}
