package eu.assina.csc.payload;

/**
 * Body for request of signatures/timestamp for generating timestamp tokens for hashes.
 * From section 11.10 of the CSC API V_1.0.4.0 spec
 */
public class CSCSignaturesTimestampResponse {

    // REQUIRED
    // The Base64-encoded time-stamp token as defined in RFC 3161 [2] as updated by RFC 5816 [10].
    // If the nonce parameter is included in the request then it SHALL also be included in the
    // time-stamp token, otherwise the response SHALL be rejected.
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
