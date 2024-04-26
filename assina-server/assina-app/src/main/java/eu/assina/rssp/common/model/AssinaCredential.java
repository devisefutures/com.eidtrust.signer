package eu.assina.rssp.common.model;

import eu.assina.rssp.api.model.DateAudit;
import eu.assina.rssp.api.model.StringListConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents the credential object from the CSC Spec v 1.0.4.0:
 *    credential: cryptographic object and related data used to support remote digital signatures over the Internet.
 *    Consists of the combination of a public/private key pair (also named “signing key” in CEN EN 419 241-1 [i.5])
 *    and a X.509 public key certificate managed by a remote signing service provider on behalf of a user.
 */
@Entity
public class AssinaCredential extends DateAudit
{
	// TODO creds and keys should be strings pem
	// need to include the other details the key OIDs etc.

	@Id
	private String id;

	private String owner;
	private String description;

	/** store the public key as a PEM string  */
	@Column(length = 2000)
	private String publicKey;

	/** store the private key as a PEM string and encrypt it */
	@Column(length = 2000)
	private String privateKey;

	// REQUIRED
	// matches key status in CSC API
	// One of enabled | disabled
	// The status of the signing key of the credential:
	// • “enabled”: the signing key is enabled and can be used for signing.
	// • “disabled”: the signing key is disabled and cannot be used for signing.
	// This MAY occur when the owner has disabled it or when the RSSP has detected
	// that the associated certificate is expired or revoked.
	private boolean keyEnabled = true;

	// The list of OIDs of the supported key algorithms.
	// For example: 1.2.840.113549.1.1.1 = RSA encryption,
	// 1.2.840.10045.4.3.2 = ECDSA with SHA256.
	@Convert(converter = StringListConverter.class)
	@Column(length = 2000)
	private List<String> keyAlgorithmOIDs;

	// Number The length of the cryptographic key in bits.
	private int keyBitLength;

	// The OID of the ECDSA curve.
	// The value SHALL only be returned if keyAlgo is based on ECDSA.
	private String keyCurve;

	// The Issuer Distinguished Name from the X.509v3 end entity certificate as
	// UTF-8-encoded character string according to RFC 4514 [4]. This value SHALL
	private String issuerDN;

	// The Serial Number from the X.509v3 end entity certificate represented as
	// hex-encoded string format.
	private String serialNumber;

	// The Subject Distinguished Name from the X.509v3 end entity certificate as
	// UTF-8-encoded character string,according to RFC 4514 [4].
	private String subjectDN;

	// The validity start date from the X.509v3 end entity certificate as character
	// string, encoded as GeneralizedTime (RFC 5280 [8]) (e.g.  “YYYYMMDDHHMMSSZ”)
	private String validFrom;

	// The validity end date from the X.509v3 end enity certificate as character
	// string, encoded as GeneralizedTime (RFC 5280 [8]) (e.g.  “YYYYMMDDHHMMSSZ”).
	private String validTo;

	@Column(length = 2000)
	private String certificate;

	public AssinaCredential()
	{
		id = UUID.randomUUID().toString();
	}

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getCertificate()
	{
		return certificate;
	}

	public void setCertificate(String certificate)
	{
		this.certificate = certificate;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public boolean isKeyEnabled() {
		return keyEnabled;
	}

	public void setKeyEnabled(boolean enabled) {
		this.keyEnabled = enabled;
	}

	public List<String> getKeyAlgorithmOIDs() {
		return keyAlgorithmOIDs;
	}

	public void setKeyAlgorithmOIDs(List<String> keyAlgoritmOIDs) {
		this.keyAlgorithmOIDs = keyAlgoritmOIDs;
	}

	public int getKeyBitLength() {
		return keyBitLength;
	}

	public void setKeyBitLength(int keyBitLength) {
		this.keyBitLength = keyBitLength;
	}

	public String getECDSACurveOID() {
		return keyCurve;
	}

	public void setECDSACurveOID(String curve) {
		this.keyCurve = curve;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AssinaCredential that = (AssinaCredential) o;
		return Objects.equals(owner, that.owner) &&
							 Objects.equals(certificate, that.certificate) &&
							 Objects.equals(publicKey, that.publicKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(owner, certificate, publicKey);
	}
}

