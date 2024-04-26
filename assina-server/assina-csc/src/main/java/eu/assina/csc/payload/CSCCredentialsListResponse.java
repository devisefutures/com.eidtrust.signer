package eu.assina.csc.payload;

import java.util.List;

/**
 * Body for response of credentials/list - a list, possibly paginated, of credential IDs
 * From section 11.4 of the CSC API V_1.0.4.0 spec
 */
public class CSCCredentialsListResponse {

    // REQUIRED
    // One or more credentialID(s) associated with the provided or implicit userID.
    // No more than maxResults items SHALL be returned.
    private List<String> credentialIDs;

    // OPTIONAL
    // The page token required to retrieve the next page of results.
    // No value SHALL be returned if the remote service
    // does not suport items pagination or the response relates to the last page of results.
    private String nextPageToken;

    public List<String> getCredentialIDs() {
        return credentialIDs;
    }

    public void setCredentialIDs(List<String> credentialIDs) {
        this.credentialIDs = credentialIDs;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
