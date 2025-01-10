package com.andreluiskg.service;
/*
import java.io.IOException;
import java.io.InputStream;

import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.andreluiskg.clients.FileUploadClient;
import com.andreluiskg.clients.MessageClient;
import com.andreluiskg.clients.MessageReaderClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
*/
//@ApplicationScoped
public class UploadService {/*

	private static final Logger LOGGER = Logger.getLogger(UploadService.class);

	@Inject
	FileUploadClient fileUploadClient;

	@Inject
	MessageClient messageClient;

	@Inject
	MessageReaderClient messageReaderClient;

	public Response uploadFile(MultipartFormDataInput input) {

		InputStream uploadedInputStream = null;
		String fileName = null;
		String sourceName = null;
		String userName = null;

		try {
			if (input.getFormDataMap().containsKey("file")) {
				uploadedInputStream = input.getFormDataPart("file", InputStream.class, null);
				LOGGER.info("File input stream obtained successfully.");
				fileName = input.getFormDataMap().get("file").get(0).getHeaders().getFirst("Content-Disposition")
						.split(";")[2].split("=")[1].trim().replaceAll("\"", ""); // Obtém o nome do arquivo
				sourceName = input.getFormDataPart("sourceName", String.class, null);
				userName = input.getFormDataPart("userName", String.class, null);
				LOGGER.info("fileName = " + fileName + ", sourceName = " + sourceName + ", userName = " + userName);
				LOGGER.info("File input stream obtained successfully.");
			} else {
				LOGGER.error("File input stream is missing.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).entity("Error processing form data").build();
		}

		if (uploadedInputStream == null) {
			LOGGER.error("Input stream is null.");
			return Response.status(400).entity("File is missing").build();
		}

		boolean isUploaded = fileUploadClient.uploadToMinIO(uploadedInputStream, fileName);
		if (isUploaded) {
			boolean isMessageSent = messageClient.sendMessageToQueue(fileName, sourceName, userName);
			if (isMessageSent) {
				// Capturar e exibir mensagens do RabbitMQ no log
				messageReaderClient.readAndLogMessagesFromQueue();
				return Response.status(200).entity("File uploaded successfully to MinIO").build();
			} else {
				fileUploadClient.deleteFromMinIO(fileName);
				return Response.status(500).entity("Error sending message to RabbitMQ, file deleted from MinIO")
						.build();
			}
		} else {
			return Response.status(500).entity("Error uploading file to MinIO").build();
		}

	}

*/}
