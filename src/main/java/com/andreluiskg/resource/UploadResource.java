package com.andreluiskg.resource;
/*
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.andreluiskg.service.UploadService;
import com.andreluiskg.clients.MessageReaderClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
*/
//@Path("/")
public class UploadResource {/*

	@Inject
	UploadService greetingService;

	@Inject
	MessageReaderClient messageReaderClient;

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello RESTEasy";
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(MultipartFormDataInput input) {
		return greetingService.uploadFile(input);
	}

	@GET
	@Path("/read-messages")
	@Produces(MediaType.TEXT_PLAIN)
	public Response readMessages() {
		messageReaderClient.readMessagesFromQueue();
		return Response.ok("Reading messages from RabbitMQ...").build();
	}

	@GET
	@Path("/read-log-messages")
	@Produces(MediaType.TEXT_PLAIN)
	public Response readAndLogMessages() {
		messageReaderClient.readAndLogMessagesFromQueue();
		return Response.ok("Reading and logging messages from RabbitMQ...").build();
	}

*/}
