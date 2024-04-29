package com.eidtrust.signer.sa.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eidtrust.signer.sa.client.AssinaRSSPClient;
import com.eidtrust.signer.sa.client.ClientContext;
import com.eidtrust.signer.sa.config.RSSPClientConfig;
import com.eidtrust.signer.sa.error.InternalErrorException;
import com.eidtrust.signer.sa.pdf.PdfSupport;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

@Service
public class AssinaSigningService {

	final AssinaRSSPClient rsspClient;
	PdfSupport pdfSupport;
	private FileStorageService fileStorageService;

	public AssinaSigningService(RSSPClientConfig rsspClientConfig,
								FileStorageService fileStorageService) {
		rsspClient = new AssinaRSSPClient(rsspClientConfig);
		pdfSupport = new PdfSupport(rsspClient);
		this.fileStorageService = fileStorageService;
	}


	public String signFile(String originalFileName, String PIN, String credentialID, String authorizationHeader) {
		String signedFileName;
		try {
			Path originalFilePath = fileStorageService.getFilePath(originalFileName);
			String baseName = originalFilePath.getFileName().toString();
			signedFileName = StringUtils.stripFilenameExtension(baseName) + "_signed.pdf";
			Path signedFilePath = fileStorageService.newFilePath(signedFileName);
			ClientContext context = new ClientContext();
			context.setAuthorizationHeader(authorizationHeader);
			context.setPIN(PIN);
			context.setCredentialID(credentialID);
			rsspClient.setContext(context);
			pdfSupport.signDetached(originalFilePath.toFile(), signedFilePath.toFile());
		} catch (IOException | NoSuchAlgorithmException e) {
			throw new InternalErrorException("Internal error in Signing Application", e);
		} finally {
			rsspClient.setContext(null); // clear it just in case it gets resused
		}
		return signedFileName;
	}
}
