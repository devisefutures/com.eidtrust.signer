package eu.assina.rssp.api.controller;

import eu.assina.rssp.common.model.AssinaCredential;
import eu.assina.rssp.api.model.AuthProvider;
import eu.assina.rssp.api.model.User;
import eu.assina.rssp.api.payload.CredentialSummary;
import eu.assina.rssp.api.services.CredentialService;
import eu.assina.rssp.api.services.UserService;
import eu.assina.rssp.common.error.ApiException;
import eu.assina.rssp.common.error.AssinaError;
import eu.assina.rssp.security.CurrentUser;
import eu.assina.rssp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/credentials")
public class AssinaCredentialController
{
	private CredentialService credentialService;
	private UserService userService;

	public AssinaCredentialController(@Autowired final CredentialService credentialService,
									  @Autowired final UserService userService)
	{
		this.credentialService = credentialService;
		this.userService = userService;
	}

	@GetMapping
	public Page<CredentialSummary> getCredentialsPaginated(Pageable pageable)
	{
		return credentialService.getCredentials(pageable).map(this::summarize);
	}

	@GetMapping("/{id}")
	public CredentialSummary getCredentialsByOwner(@PathVariable(value = "id") String id)
	{
		return credentialService.getCredentialWithId(id).map(this::summarize).orElseThrow(
				() -> new ApiException(AssinaError.CredentialNotFound, "Failed to find credential with id {}", id));
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<AssinaCredential> createCredential(@CurrentUser UserPrincipal userPrincipal) {
		String id = userPrincipal.getId();
		User user = userService.getUserById(id).orElseThrow(
				() -> new ApiException(AssinaError.UserNotFound, "Current user unknown: {}"));

		// use the id as the credential owner, and the username or email as the DN
		final AssinaCredential credential =
				credentialService.createCredential(user.getId(), user.getUsername());
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(credential.getId())
				.toUri();
		final ResponseEntity.BodyBuilder responseEntityBuilder = ResponseEntity.created(location);
		return responseEntityBuilder.body(credential);
	}

	@DeleteMapping("/{credentialId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCredential(@PathVariable(value = "credentialId") String credentialId)
	{
		credentialService.deleteCredentials(credentialId);
	}

	/**
	 * Functional method to convert a credential into a summary for returning
	 **/
	private CredentialSummary summarize(AssinaCredential credential) {
		return summarizeCredential(credential, userService);
	}

	public static CredentialSummary summarizeCredential(AssinaCredential credential, UserService userService) {
	    String ownerId = credential.getOwner();
		User dummy = new User(ownerId, "unknown", "unknown", AuthProvider.local);
		User user;
		if (userService != null) {
			user = userService.getUserById(ownerId).orElse(dummy);
		}
		else {
			user = dummy; // used by tests that don't have a user service
		}
		return new CredentialSummary(credential.getId(), user.getUsername(), user.getName(), credential.getCreatedAt(),
				credential.getDescription());
	}
}
