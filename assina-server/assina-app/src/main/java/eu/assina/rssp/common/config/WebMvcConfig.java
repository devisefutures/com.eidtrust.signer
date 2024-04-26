package eu.assina.rssp.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Autowired
    public Environment env;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String clientUrl = env.getProperty("ASSINA_CLIENT_BASE_URL");
        registry.addMapping("/**")
        .allowedOrigins(clientUrl, "http://localhost:3000")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(AssinaConstants.API_URL_ROOT,
                HandlerTypePredicate.forAnnotation(RestController.class)
                        .and(HandlerTypePredicate.forBasePackage(
                                        "eu.assina.rssp.api")));
        configurer.addPathPrefix(AssinaConstants.CSC_URL_ROOT,
                HandlerTypePredicate.forAnnotation(RestController.class)
                        .and(HandlerTypePredicate.forBasePackage(
                                "eu.assina.rssp.csc")));
    }
}
