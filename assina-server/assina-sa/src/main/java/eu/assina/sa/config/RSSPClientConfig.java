package eu.assina.sa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rssp")
public class RSSPClientConfig {

    private String cscBaseUrl;

    public String setCscBaseUrl() {
        return cscBaseUrl;
    }

    public void setCscBaseUrl(String cscBaseUrl) {
        this.cscBaseUrl = cscBaseUrl;
    }
}
