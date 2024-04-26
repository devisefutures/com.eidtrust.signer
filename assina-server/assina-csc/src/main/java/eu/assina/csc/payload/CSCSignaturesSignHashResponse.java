package eu.assina.csc.payload;

import java.util.List;

/**
 * Body for response of signatures/signHash for signing document hashes
 * From section 11.9 of the CSC API V_1.0.4.0 spec
 */
public class CSCSignaturesSignHashResponse {

    // REQUIRED
    // One or more Base64-encoded signed hash(s).
    // In case of multiple signatures, the signed hashes SHALL be returned in the same order as
    // the corresponding hashes provided as an input parameter.
    private List<String> signatures;

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }
}
