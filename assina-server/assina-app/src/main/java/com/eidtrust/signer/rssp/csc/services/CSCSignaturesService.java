package com.eidtrust.signer.rssp.csc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.eidtrust.signer.csc.error.CSCInvalidRequest;
import com.eidtrust.signer.csc.payload.CSCSignaturesSignHashRequest;
import com.eidtrust.signer.csc.payload.CSCSignaturesSignHashResponse;
import com.eidtrust.signer.csc.payload.CSCSignaturesTimestampRequest;
import com.eidtrust.signer.csc.payload.CSCSignaturesTimestampResponse;
import com.eidtrust.signer.rssp.api.services.CredentialService;
import com.eidtrust.signer.rssp.common.error.ApiException;
import com.eidtrust.signer.rssp.common.model.AssinaCredential;
import com.eidtrust.signer.rssp.crypto.AssinaCryptoService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSCSignaturesService {

	private static final Logger log = LoggerFactory.getLogger(CSCSignaturesService.class);
	private final CredentialService credentialService;
	private AssinaCryptoService cryptoService;
	private CSCSADProvider sadProvider;

	public CSCSignaturesService(CredentialService credentialService,
								AssinaCryptoService cryptoService,
								CSCSADProvider sadProvider) {
		this.credentialService = credentialService;
		this.cryptoService = cryptoService;
		this.sadProvider = sadProvider;
	}

	/**
	 * Sign the provided hash with the credential specified in the request
	 * @param signHashRequest
	 * @return
	 */
	public CSCSignaturesSignHashResponse signHash(
			@Valid @RequestBody CSCSignaturesSignHashRequest signHashRequest) {
		CSCSignaturesSignHashResponse response = new CSCSignaturesSignHashResponse();
		final String credentialID = signHashRequest.getCredentialID();

		final AssinaCredential credential =
				credentialService.getCredentialWithId(credentialID).orElseThrow(
						() -> new ApiException(CSCInvalidRequest.InvalidCredentialId,
								"No credential found with the given Id", credentialID));

		// we know SAD is not empty thanks to annotations in the DTO, but is it valid?
        // if it is expired or otherwise invalid, the provider will throw the right
		// exception for the CSC standard
        sadProvider.validateSAD(signHashRequest.getSAD());

		// assume our sign algo has no params and our hashAlgo is implied by the signalgo
		final List<String> hashes = signHashRequest.getHash();
		List<String> signedHashes = new ArrayList<>();
		for (String hash : signHashRequest.getHash()) {
			// TODO safe to assume default charset for hash (UTF-8)
			byte[] signedData = cryptoService.signWithPemCertificate(
					hash.getBytes(),
					credential.getCertificate(),
					credential.getPrivateKey(),
					signHashRequest.getSignAlgo(),
					signHashRequest.getSignAlgoParams());
			signedHashes.add(new String(signedData)); // assumes UTF8
		}
		response.setSignatures(signedHashes);
		return response;
	}

	public CSCSignaturesTimestampResponse generateTimestamp(CSCSignaturesTimestampRequest timestampRequest) {
		log.error("Generate Timestamp is not supported by Assina");
		throw new IllegalStateException("Not Supported by Assina Implemented"); // TODO implement this
	}
}
