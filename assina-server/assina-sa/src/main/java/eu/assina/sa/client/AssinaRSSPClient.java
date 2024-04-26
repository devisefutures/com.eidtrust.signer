package eu.assina.sa.client;

import eu.assina.csc.payload.CSCCredentialsAuthorizeRequest;
import eu.assina.csc.payload.CSCCredentialsAuthorizeResponse;
import eu.assina.csc.payload.CSCCredentialsInfoRequest;
import eu.assina.csc.payload.CSCCredentialsInfoResponse;
import eu.assina.csc.payload.CSCCredentialsListRequest;
import eu.assina.csc.payload.CSCCredentialsListResponse;
import eu.assina.csc.payload.CSCSignaturesSignHashRequest;
import eu.assina.csc.payload.CSCSignaturesSignHashResponse;
import eu.assina.sa.config.RSSPClientConfig;
import eu.assina.sa.error.CredentialNotFoundException;
import eu.assina.sa.error.InvalidRequestException;
import eu.assina.sa.error.RSSPClientException;
import eu.assina.sa.model.AssinaSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * REST client for the RSSP web service, used by signing applications to get credentials and sign hashes
 */
public class AssinaRSSPClient implements AssinaSigner {

    private WebClient webClient;
    private ClientContext context;
    private static final Logger log = LoggerFactory.getLogger(AssinaRSSPClient.class);

    public AssinaRSSPClient(RSSPClientConfig config) {
        webClient = WebClient.builder().baseUrl(config.setCscBaseUrl())
                            .defaultCookie("cookieKey", "cookieValue")
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    @Override
    /** Prepares the AssignSigner */
    public ClientContext prepCredenital() {
        final List<String> credentials = listCredentialsForCurrentUser();
        if (credentials == null || credentials.isEmpty()) {
            throw new CredentialNotFoundException("The current user has no credentials with which to sign");
        }

        String credentialID = getContext().getCredentialID();
        if (StringUtils.hasText(credentialID)) {
            // make sure the current user owns the credential if it specified
            if (!credentials.contains(credentialID)) {
                throw new InvalidRequestException("The current user does not own the specified credential");
            }
        } else {
            // convention: simply use the first credential if there is non specified in the context
            credentialID = credentials.get(0);
        }
        context.setCredentialID(credentialID);

        final CSCCredentialsInfoResponse credentialInfo = getCredentialInfo(credentialID);
        if (credentialInfo == null) {
            throw new InvalidRequestException("Could not get info on the specified credential");
        }

        // get the algo from the credential info
        final CSCCredentialsInfoResponse.Key key = credentialInfo.getKey();
        if (key == null) {
            throw new InvalidRequestException("Could not get key info for the credential");
        }
        final List<String> algos = key.getAlgo();
        if (algos == null || algos.isEmpty()) {
            throw new InvalidRequestException("Could not get sign algos from the key info for the credential");
        }
        // simply use the fisrt algo
        String signAlgo = algos.get(0);
        context.setSignAlgo(signAlgo);

        final CSCCredentialsInfoResponse.Cert cert = credentialInfo.getCert();
        context.setSubject(cert.getSubjectDN());
        return context;
    }


    @Override
    /** Implements the AssignSigner by signing the hash with several requests to the RSSP */
    public byte[] signHash(byte[] pdfHash, ClientContext context) {

        String credentialID = context.getCredentialID();

        // now authorize it
        final String SAD = authorizeCredential(credentialID, getPIN());
        if (SAD == null) {
            throw new InvalidRequestException("Could not authorize the credential with the PIN");
        }

        // and use it to sign
        log.info("Signing hash with credential: {}", credentialID);
        final String signedHash = signHash(new String(pdfHash), credentialID, SAD, context.getSignAlgo());
        return signedHash.getBytes(StandardCharsets.UTF_8);
    }

    public Mono<CSCCredentialsListResponse> requestCredentialList(CSCCredentialsListRequest request) {
        return webClient.post()
                       .uri("/credentials/list")
                       .bodyValue(request)
                       .header("Authorization", buildAuthHeader())
                       .exchangeToMono(response -> {
                           if (response.statusCode().equals(HttpStatus.OK)) {
                                return response.bodyToMono(CSCCredentialsListResponse.class);
                           }
                           else {
                               return Mono.error(new RSSPClientException(response));
                           }
                       });
    }

    public Mono<CSCCredentialsInfoResponse> requestCredentialInfo(CSCCredentialsInfoRequest request) {
        return webClient.post()
                       .uri("/credentials/info")
                       .bodyValue(request)
                       .header("Authorization", buildAuthHeader())
                       .exchangeToMono(response -> {
                           if (response.statusCode().equals(HttpStatus.OK)) {
                               return response.bodyToMono(CSCCredentialsInfoResponse.class);
                           }
                           else {
                               return Mono.error(new RSSPClientException(response));
                           }
                       });
    }

    public Mono<CSCCredentialsAuthorizeResponse> requestAuthorizeCredential(CSCCredentialsAuthorizeRequest request) {
        return webClient.post()
                       .uri("/credentials/authorize")
                       .bodyValue(request)
                       .header("Authorization", buildAuthHeader())
                       .exchangeToMono(response -> {
                           if (response.statusCode().equals(HttpStatus.OK)) {
                               return response.bodyToMono(CSCCredentialsAuthorizeResponse.class);
                           }
                           else {
                               return Mono.error(new RSSPClientException(response));
                           }
                       });
    }

    public Mono<CSCSignaturesSignHashResponse> requestSignHash(CSCSignaturesSignHashRequest request) {
        return webClient.post()
                       .uri("/signatures/signHash")
                       .bodyValue(request)
                       .header("Authorization", buildAuthHeader())
                       .exchangeToMono(response -> {
                           if (response.statusCode().equals(HttpStatus.OK)) {
                               return response.bodyToMono(CSCSignaturesSignHashResponse.class);
                           }
                           else {
                               return Mono.error(new RSSPClientException(response));
                           }
                       });
    }

    public List<String> listCredentialsForCurrentUser() {
        CSCCredentialsListRequest request = new CSCCredentialsListRequest();
        final CSCCredentialsListResponse response = requestCredentialList(request).block();
        return response.getCredentialIDs();
    }

    public CSCCredentialsInfoResponse getCredentialInfo(String credentialID) {
        CSCCredentialsInfoRequest request = new CSCCredentialsInfoRequest();
        request.setCredentialID(credentialID);
        request.setCertInfo(true);
        final CSCCredentialsInfoResponse response = requestCredentialInfo(request).block();
        return response;
    }

    public String authorizeCredential(String credentialID, String PIN) {
        CSCCredentialsAuthorizeRequest request = new CSCCredentialsAuthorizeRequest();
        request.setPIN(PIN);
        request.setCredentialID(credentialID);
        request.setNumSignatures(1);
        final CSCCredentialsAuthorizeResponse response = requestAuthorizeCredential(request).block();
        final String sad = response.getSAD();
        return sad;
    }

    public String signHash(String pdfHash, String credentialID, String SAD, String signAlgo) {
        CSCSignaturesSignHashRequest request = new CSCSignaturesSignHashRequest();
        request.setHash(Collections.singletonList(pdfHash));
        request.setCredentialID(credentialID);
        request.setSAD(SAD);
        request.setSignAlgo(signAlgo);
        final CSCSignaturesSignHashResponse response = requestSignHash(request).block();
        final List<String> signatures = response.getSignatures();
        return signatures.get(0);
    }

    /**
     * Set the context to be used in the next request next request
     */
    public void setContext(ClientContext context) {
        this.context = context;
    }

    private ClientContext getContext() {
        if (context == null) {
            throw new InvalidRequestException("ClientContext not set before using client");
        }
        return context;
    }

    private String getPIN() {
        String PIN = getContext().getPIN();
        if (PIN == null) {
            throw new InvalidRequestException("PIN not set in context");
        }
        return PIN;
    }

    private String buildAuthHeader() {
        String authorizationHeader = getContext().getAuthorizationHeader();
        if (authorizationHeader == null) {
            throw new InvalidRequestException("Authorization not set in context");
        }
        return authorizationHeader;
    }
}
