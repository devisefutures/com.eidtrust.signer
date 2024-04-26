package eu.assina.sa.client;

/**
 * Context for a call to the RSSP
 */
public class ClientContext {
    private String authorizationHeader;
    private String credentialID;
    private String SAD;
    private String signAlgo;
    private String PIN;
    private String pdfHash;
    private String subject;

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    public void setAuthorizationHeader(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }

    public String getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(String credentialID) {
        this.credentialID = credentialID;
    }

    public String getSAD() {
        return SAD;
    }

    public void setSAD(String SAD) {
        this.SAD = SAD;
    }

    public String getSignAlgo() {
        return signAlgo;
    }

    public void setSignAlgo(String signAlgo) {
        this.signAlgo = signAlgo;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String pin) {
        this.PIN = pin;
    }

    public String getPdfHash() {
        return pdfHash;
    }

    public void setPdfHash(String pdfHash) {
        this.pdfHash = pdfHash;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}
