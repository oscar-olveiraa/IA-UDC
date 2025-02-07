package es.udc.rs.telco.model.telcoservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class TelcoServiceFactory {

	private static final String CLASS_NAME_PARAMETER = "TelcoServiceFactory.className";
	private static TelcoService service = null;

	private TelcoServiceFactory() {
	}

	@SuppressWarnings("rawtypes")
	private static TelcoService getInstance() {
		try {
			String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class serviceClass = Class.forName(serviceClassName);
			return (TelcoService) serviceClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static synchronized  TelcoService getService() {

		if (service == null) {
			service = getInstance();
		}
		return service;

	}
}
