package eu.assina.sa.controller;

import eu.assina.sa.error.InvalidRequestException;
import eu.assina.sa.payload.SignedFileResponse;
import eu.assina.sa.services.AssinaSigningService;
import eu.assina.sa.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class AssinaSigningController {
	private static final Logger log = LoggerFactory.getLogger(AssinaSigningController.class);

	private AssinaSigningService signingService;
	private FileStorageService fileStorageService;

	public AssinaSigningController(AssinaSigningService signingService,
								   FileStorageService fileStorageService) {
		this.signingService = signingService;
		this.fileStorageService = fileStorageService;
	}

	@PostMapping("/signFile")
	public Object uploadFile(@RequestParam("file") MultipartFile file,
							 @RequestHeader("Authorization") String authorizationHeader,
							 @RequestParam("pin") String pin,
							 @RequestParam("credential") String credential) {

		if (!StringUtils.hasText(authorizationHeader)) {
			throw new InvalidRequestException("Expected an authorization header");
		}

		String originalFileName = fileStorageService.storeFile(file);
		String signedFileName = signingService.signFile(originalFileName, pin, credential, authorizationHeader);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
										 .path("/downloadFile/")
										 .path(signedFileName)
										 .toUriString();

		final SignedFileResponse signedFileResponse =
				new SignedFileResponse(signedFileName, fileDownloadUri, file.getContentType(), file.getSize());
		return signedFileResponse;
	}


	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			log.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
					   .contentType(MediaType.parseMediaType(contentType))
					   .header(HttpHeaders.CONTENT_DISPOSITION,
							   "attachment; filename=\"" + resource.getFilename() + "\"")
					   .body(resource);
	}

}
