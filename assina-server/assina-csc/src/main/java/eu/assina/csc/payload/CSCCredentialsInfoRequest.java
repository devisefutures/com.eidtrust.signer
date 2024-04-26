package eu.assina.csc.payload;

import javax.validation.constraints.NotBlank;

/**
 * Body for request of credentials/info - information about a specific credential record
 * of credential IDs
 * From section 11.5 of the CSC API V_1.0.4.0 spec
 */
public class CSCCredentialsInfoRequest {

    public CSCCredentialsInfoRequest() {}

    // REQUIRED
    // The unique identifier associated to the credential.
    @NotBlank(message = "MissingCredentialId")
    private String credentialID;

    // OPTIONAL
    // none | single | chain
    // Specifies which certificates from the certificate chain shall be returned in certs/certificates.
    //  • “none”: No certificate SHALL be returned.
    //  • “single”: Only the end entity certificate SHALL be returned.
    //  • “chain”: The full certificate chain SHALL be returned.
    // The default value is “single”, so if the parameter is omitted then the method will
    // only return the end entity certificate.
    private String certificates;

    // OPTIONAL
    // Request to return various parameters containing information from the end entity
    // certificate.
    // This is useful in case the signature application wants to retrieve some details
    // of the certificate without having to decode it first.
    // The default value is “false”, so if the parameter is omitted then the information will
    // not be returned.
    private boolean certInfo = false;

    // OPTIONAL
    // Request to return various parameters containing information on the
    // authorization mechanisms supported by this  credential (PIN and OTP groups).
    // The default value is “false”, so if the parameter is omitted then the
    // information will not be returned.
    private boolean authInfo = false;

    // OPTIONAL
    // String The clientData as defined in the Input parameter table in section 8.3.2.
    private String clientData;

    // OPTIONAL
    // The lang as defined in the Input parameter table in section 11.1.
    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(String credentialID) {
        this.credentialID = credentialID;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public boolean isCertInfo() {
        return certInfo;
    }

    public void setCertInfo(boolean certInfo) {
        this.certInfo = certInfo;
    }

    public boolean isAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(boolean authInfo) {
        this.authInfo = authInfo;
    }

    public String getClientData() {
        return clientData;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }
}
