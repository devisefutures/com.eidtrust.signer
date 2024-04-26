package eu.assina.rssp.api.payload;

import java.time.Instant;

/**
 * Wrapper around the user object for display a user profile
 */
public class UserProfile {
    private String id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Long credentialCount;

    public UserProfile(String id, String username, String name, Instant joinedAt,
                       Long credentialCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.credentialCount = credentialCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getCredentialCount() {
        return credentialCount;
    }

    public void setCredentialCount(Long credentialCount) {
        this.credentialCount = credentialCount;
    }
}
