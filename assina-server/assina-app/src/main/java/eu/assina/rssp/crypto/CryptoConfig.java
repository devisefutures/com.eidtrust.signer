package eu.assina.rssp.crypto;

/**
 * Configuration properties for generating key pairs, certificates, and encryption
 */
public class CryptoConfig {
    private String keyAlgorithm;
    private int keySize;
    private int monthsValidity;
    private String signatureAlgorithm;
    private String passphrase;
    private String issuer;

    /**
     * Key generation algorithm name
     * Example: "RSA"
     */
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    /**
     * Certificate Signature algorithm name: must correspond with the key algorithm
     * Example "SHA256WithRSA" (corresponds with "RSA")
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * Key size in bits
     * Example: 2048
     */
    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    /**
     * Number of months new certificates should be valid for from now
     * Example: 12 (1 year)
     */
    public int getMonthsValidity() {
        return monthsValidity;
    }

    public void setMonthsValidity(int monthsValidity) {
        this.monthsValidity = monthsValidity;
    }

    /**
     * Encryption passphrase: used to encrypt private keys at rest.
     */
    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    /**
     * Distinguished Name for the Certificate Issuer
     */
    public String getCertificateIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

}
