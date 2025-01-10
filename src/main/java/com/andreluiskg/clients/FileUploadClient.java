package com.andreluiskg.clients;
/*
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
*/
//@ApplicationScoped
public class FileUploadClient {/*

	private static final Logger LOGGER = Logger.getLogger(FileUploadClient.class);

	@Inject
	MinioClient minioClient;

	@Inject
	@ConfigProperty(name = "quarkus.minio.url")
	String minioUrl;

	@Inject
	@ConfigProperty(name = "quarkus.minio.access-key")
	String minioAccessKey;

	@Inject
	@ConfigProperty(name = "quarkus.minio.secret-key")
	String minioSecretKey;

	@Inject
	@ConfigProperty(name = "quarkus.minio.bucket-name")
	String minioBucketName;

	public boolean uploadToMinIO(InputStream uploadedInputStream, String fileName) {
		try {
			MinioClient minioClient = MinioClient.builder().endpoint(minioUrl) // Ensure endpoint is set
					.credentials(minioAccessKey, minioSecretKey).build();
			LOGGER.info("checkpoint1");

			// Convert InputStream to byte array
			byte[] fileBytes = uploadedInputStream.readAllBytes();
			LOGGER.info("checkpoint2");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
			LOGGER.info("checkpoint3");

			// Upload the file to MinIO
			minioClient.putObject(PutObjectArgs.builder().bucket(minioBucketName).object(fileName)
					.stream(byteArrayInputStream, byteArrayInputStream.available(), -1).contentType("application/pdf")
					.build());
			LOGGER.info("File uploaded successfully to MinIO.");
			return true;
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException e) {
			LOGGER.error("Error uploading file to MinIO.", e);
			e.printStackTrace();
			return false;
		} catch (MinioException e) {
			LOGGER.error("MinioException: Error processing file upload.", e);
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			LOGGER.error("IOException: Error processing file upload.", e);
			e.printStackTrace();
			return false;
		}
	}

	public void deleteFromMinIO(String fileName) {
		try {
			MinioClient minioClient = MinioClient.builder().endpoint(minioUrl)
					.credentials(minioAccessKey, minioSecretKey).build();
			minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioBucketName).object(fileName).build());
			LOGGER.info("File deleted from MinIO: " + fileName);
		} catch (Exception e) {
			LOGGER.error("Error deleting file from MinIO.", e);
		}
	}
*/}
