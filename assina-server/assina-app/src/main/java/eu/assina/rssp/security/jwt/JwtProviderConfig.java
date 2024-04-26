package eu.assina.rssp.security.jwt;

/**
 * Minimal configuration for JWT token creation and validation
 */
public class JwtProviderConfig {
    private long lifetimeMinutes;
    private String tokenSecret;
    private String type;

    public long getLifetimeMinutes() {
        return lifetimeMinutes;
    }

    public void setLifetimeMinutes(long lifetimeMinutes) {
        this.lifetimeMinutes = lifetimeMinutes;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
