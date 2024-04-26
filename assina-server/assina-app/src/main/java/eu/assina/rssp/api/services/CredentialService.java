package eu.assina.rssp.api.services;

import eu.assina.rssp.common.error.AssinaError;
import eu.assina.rssp.common.model.AssinaCredential;
import eu.assina.rssp.common.error.ApiException;
import eu.assina.rssp.crypto.AssinaCryptoService;
import eu.assina.rssp.repository.CredentialRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CredentialService
{
	private CredentialRepository credentialRepository;
	private AssinaCryptoService cryptoService;

	public CredentialService(CredentialRepository credentialRepository,
							 AssinaCryptoService cryptoService)
	{
		this.credentialRepository = credentialRepository;
		this.cryptoService = cryptoService;
	}

	/**
	 * Creates a new certificate and keypair combinations and stores them in the repository.
	 * The private key is always stored encrypted.
	 *
	 * @param owner
	 * @param subjectDN name of the subject used in the certificate
	 * @return
	 */
	public AssinaCredential createCredential(String owner, String subjectDN)
	{
		AssinaCredential credential = cryptoService.createCredential(owner, subjectDN);
		credential.setCreatedAt(Instant.now());
		credentialRepository.save(credential);
		return credential;
	}

	/**
	 * Gets credentials for the specified owner a page at a time
	 * @param owner user who owns the credentials (also the subject of the cert
	 * @param pageable
	 * @return
	 */
	public Page<AssinaCredential> getCredentialsByOwner(String owner, Pageable pageable)
	{
		Page<AssinaCredential> byOwner = credentialRepository.findByOwner(owner, pageable);
		return byOwner;
	}

	/**
	 * Gets count of credentials by this owner
	 * @param owner user who owns the credentials (also the subject of the cert
	 * @return
	 */
	public long countCredentialsByOwner(String owner)
	{
		long count = credentialRepository.countByOwner(owner);
		return count;
	}

	/**
	 * Gets credentials for all users a page at a time
	 * @param pageable
	 */
	public Page<AssinaCredential> getCredentials(Pageable pageable)
	{
		Page<AssinaCredential> credentials = credentialRepository.findAll(pageable);
		return credentials;
	}

	public Optional<AssinaCredential> getCredentialWithId(String id) {
		return credentialRepository.findById(id);
	}

	/**
	 * Deletes the credentials with the specified Id
	 *
	 * @param id id of the credentials to be deleted
	 */
	public void deleteCredentials(String id) {
		try {
			credentialRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException ex) {
			throw new ApiException(AssinaError.CredentialNotFound, "Attempted to delete credentials that do not exist", id);
		}
	}
}
