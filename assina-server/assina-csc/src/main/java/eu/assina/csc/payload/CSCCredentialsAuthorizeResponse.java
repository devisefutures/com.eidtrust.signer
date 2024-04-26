package eu.assina.csc.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Body for response of credentials/authorize - provides the client the SAD token needed to
 * authorize the subsequent request to sign a hash.
 * From section 11.6 of the CSC API V_1.0.4.0 spec
 */
public class CSCCredentialsAuthorizeResponse {

    // REQUIRED
    // The Signature Activation Data (SAD) to be used as input to the signatures/signHash method,
    // as defined in section 11.9.
    private String SAD;

    // OPTIONAL
    // The lifetime in seconds of the SAD. If omitted, the default expiration time is 3600 (1 hour).
    private long expiresIn;

    @JsonProperty("SAD") // must be uppercase
    public String getSAD() {
        return SAD;
    }

    public void setSAD(String SAD) {
        this.SAD = SAD;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
