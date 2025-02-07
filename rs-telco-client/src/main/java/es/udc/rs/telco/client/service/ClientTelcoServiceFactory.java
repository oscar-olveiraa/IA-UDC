package es.udc.rs.telco.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

//hora proporcionaremos una nueva implementación para acceder al servicio REST
//utilizando la API cliente de JAX-RS

public class ClientTelcoServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "ClientTelcoServiceFactory.className";
	private static ClientTelcoService service = null;

	private ClientTelcoServiceFactory() {
	}

	@SuppressWarnings("rawtypes")
	private static ClientTelcoService getInstance() {
		try {
			String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class serviceClass = Class.forName(serviceClassName);
			return (ClientTelcoService) serviceClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public synchronized static ClientTelcoService getService() {

		if (service == null) {
			service = getInstance();
		}
		return service;

	}
}
