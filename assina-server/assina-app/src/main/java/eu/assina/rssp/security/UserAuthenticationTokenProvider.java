package eu.assina.rssp.security;

import eu.assina.rssp.common.config.AppProperties;
import eu.assina.rssp.security.jwt.JwtProvider;
import eu.assina.rssp.security.jwt.JwtProviderConfig;
import eu.assina.rssp.security.jwt.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationTokenProvider.class);

    private JwtProvider jwtProvider;

    public UserAuthenticationTokenProvider(AppProperties appProperties) {
        JwtProviderConfig jwtConfig = appProperties.getAuth();
        jwtProvider = new JwtProvider(jwtConfig);
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String subject = userPrincipal.getUsername();
        final JwtToken token = jwtProvider.createToken(subject);
        return token.getRawToken();
    }

    public JwtToken validateToken(String authToken) {
        JwtToken token = jwtProvider.validateToken(authToken);
        if (!token.isValid()) {
            log.error(token.getError());
        }
        return token;
    }

}
