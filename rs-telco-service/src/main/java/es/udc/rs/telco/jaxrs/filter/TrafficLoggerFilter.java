package es.udc.rs.telco.jaxrs.filter;

import org.glassfish.jersey.message.internal.ReaderWriter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import java.io.*;
import java.util.List;

@Priority(0)
@PreMatching
@Provider
public class TrafficLoggerFilter implements ContainerRequestFilter,
        ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext reqContext,
                       ContainerResponseContext respContext) throws IOException {
        logResponse(reqContext, respContext);
    }

    @Override
    public void filter(ContainerRequestContext reqContext) throws IOException {
        logRequest(reqContext);
    }

    private static void logRequest(ContainerRequestContext reqContext) {
        UriInfo uriInfo = reqContext.getUriInfo();
        String method = reqContext.getMethod();
        MultivaluedMap<String,String> headers = reqContext.getHeaders();
        List<Object> matchedResources = uriInfo.getMatchedResources();
        StringBuilder sb = new StringBuilder();
        sb.append("\n* REQUEST: ").append(method).append(" ").append(uriInfo.getRequestUri());
        sb.append("\n  Headers: ").append(headers);
        sb.append("\n  Matched Resources: ").append(matchedResources);
        sb.append("\n  Entity: " ).append(getEntityBody(reqContext));
        System.out.println(sb.toString());
    }

    private static String getEntityBody(ContainerRequestContext reqContext) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = reqContext.getEntityStream();

        final StringBuilder b = new StringBuilder();
        try {
            ReaderWriter.writeTo(in, out);
            byte[] requestEntity = out.toByteArray();
            if (requestEntity.length == 0) {
                b.append("").append("\n");
            } else {
                b.append(new String(requestEntity)).append("\n");
            }
            reqContext.setEntityStream(new ByteArrayInputStream(requestEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b.toString();
    }

    private static void logResponse(ContainerRequestContext reqContext, ContainerResponseContext respContext) {
        int status = respContext.getStatus();
        MultivaluedMap<String, String> stringHeaders = respContext.getStringHeaders();
        UriInfo uriInfo = reqContext.getUriInfo();
        String method = reqContext.getMethod();
        StringBuilder sb = new StringBuilder();
        sb.append("\n* RESPONSE TO: ").append(method).append(" ").append(uriInfo.getRequestUri());
        sb.append("\n  Status: ").append(status);
        sb.append("\n  Headers: ").append(stringHeaders);
        sb.append("\n  Entity: " ).append(respContext.getEntity());
        System.out.println(sb.toString());
    }

}
