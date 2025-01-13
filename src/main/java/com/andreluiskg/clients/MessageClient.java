package com.andreluiskg.clients;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.File;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import io.minio.MinioClient;
import io.minio.GetObjectArgs;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import testes.Testes;

@ApplicationScoped
@Path("/")
public class MessageClient {

	private static final Logger LOGGER = Logger.getLogger(MessageClient.class);

	@Inject
	@ConfigProperty(name = "rabbitmq.queue.name")
	String rabbitmqQueueName;

	@Inject
	@ConfigProperty(name = "rabbitmq.host")
	String rabbitmqHost;

	@Inject
	@ConfigProperty(name = "rabbitmq.username")
	String rabbitmqUsername;

	@Inject
	@ConfigProperty(name = "rabbitmq.password")
	String rabbitmqPassword;

	@Inject
	@ConfigProperty(name = "rabbitmq.port")
	int rabbitmqPort;

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
	@ConfigProperty(name = "minio.bucket-name")
	String minioBucketName;

	private String message = "";

	@GET
	@Path("/listen")
	public Response listenToQueue() {
		new Thread(this::readMessagesFromQueue).start();
		copyFileFromMinioAndSend("dc.pdf");
		Testes t = new Testes();
		return Response.ok("Listening to RabbitMQ queue... \n" + message).build();
	}

	@GET
	@Path("/listfiles")
	public Response listFiles() {
		message = new Testes().listarArquivosMinio(minioUrl, minioAccessKey, minioSecretKey, minioBucketName);
		return Response.ok("List files from MinIO: " + message).build();
	}

	public boolean sendMessageToQueue(String fileName, String sourceName, String userName) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitmqHost);
		factory.setPort(rabbitmqPort);
		factory.setUsername(rabbitmqUsername);
		factory.setPassword(rabbitmqPassword);
		try (Connection connection = factory.newConnection(); 
				Channel channel = connection.createChannel()) {
			channel.queueDeclare(rabbitmqQueueName, true, false, false, null);
			//String message =
			message =
					String.format("File Name: %s, Source Name: %s, User Name: %s, Date/Time: %s", fileName,
					sourceName, userName, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
			channel.basicPublish("", rabbitmqQueueName, null, message.getBytes());
			LOGGER.info("\n***Message sent to RabbitMQ: " + message + "***");
			return true;
		} catch (Exception e) {
			LOGGER.error("\n***Error sending message to RabbitMQ***", e);
			return false;
		}
	}
	
	public void readMessagesFromQueue() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitmqHost);
		factory.setPort(rabbitmqPort);
		factory.setUsername(rabbitmqUsername);
		factory.setPassword(rabbitmqPassword);
		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.queueDeclare(rabbitmqQueueName, true, false, false, null);
			LOGGER.info("readMessagesFromQueue -> Waiting for messages from RabbitMQ...");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				LOGGER.info("readMessagesFromQueue -> Received message: " + message);
			};

			channel.basicConsume(rabbitmqQueueName, true, deliverCallback, consumerTag -> {
				LOGGER.info("readMessagesFromQueue -> Consumer " + consumerTag + " cancelled");
			});

			// Adicionar um delay para garantir que as mensagens sejam lidas
			Thread.sleep(2000);
		} catch (Exception e) {
			LOGGER.error("readMessagesFromQueue -> Error reading messages from RabbitMQ.", e);
		}
	}

	public boolean copyFileFromMinioAndSend(String fileName) {
		try {
			MinioClient minioClient = MinioClient.builder()
					.endpoint(minioUrl)
					.credentials(minioAccessKey, minioSecretKey)
					.build();

			InputStream fileStream = minioClient.getObject(
					GetObjectArgs.builder()
							.bucket(minioBucketName)
							.object(fileName)
							.build());

			File tempFile = File.createTempFile("upload-", ".tmp");
			Files.copy(fileStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			// Change the file name before sending
			String newFileName = "new_" + fileName;

			URL url = new URL("http://localhost:8081/upload");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---ContentBoundary");

			try (OutputStream outputStream = connection.getOutputStream()) {
				outputStream.write(("-----ContentBoundary\r\n" +
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + newFileName + "\"\r\n" +
						"Content-Type: application/octet-stream\r\n\r\n").getBytes());

				Files.copy(tempFile.toPath(), outputStream);
				outputStream.write("\r\n-----ContentBoundary--\r\n".getBytes());
			}

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				LOGGER.info("File sent successfully to the endpoint.");
				return true;
			} else {
				LOGGER.error("Failed to send file to the endpoint. Response code: " + responseCode);
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Error copying file from MinIO and sending to endpoint.", e);
			return false;
		}
	}
}
