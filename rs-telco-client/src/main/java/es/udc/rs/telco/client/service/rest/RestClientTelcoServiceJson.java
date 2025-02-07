package es.udc.rs.telco.client.service.rest;

import jakarta.ws.rs.core.MediaType;

public class RestClientTelcoServiceJson extends RestClientTelcoService {

	@Override
	protected MediaType getMediaType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
