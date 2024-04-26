package eu.assina.sa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);
    private final long MAX_AGE_SECS = 3600;

    @Autowired
    public Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String clientUrl = env.getProperty("ASSINA_CLIENT_BASE_URL");
        log.warn("CORS: Allowing client origin: {}", clientUrl);
        registry.addMapping("/**")
                .allowedOrigins(clientUrl, "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
