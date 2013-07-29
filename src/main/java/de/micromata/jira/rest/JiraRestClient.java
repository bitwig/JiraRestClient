package de.micromata.jira.rest;


import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang3.StringUtils;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

import de.micromata.jira.rest.util.RestConstants;
import de.micromata.jira.rest.util.RestException;

/**
 * Created with IntelliJ IDEA.
 * User: Christian
 * Date: 01.03.13
 * Time: 17:22
 * 
 * <p>Contains informations about the client which is connecting to JIRA over REST.</p>
 */
public class JiraRestClient {

    private ApacheHttpClient client;

    /** The base URI. */
    private URI baseUri;

    private String username = StringUtils.EMPTY;

    /**
     * Builds and configures a new client connection to JIRA. 
     *
     * @param uri = the login mask URI where JIRA is running
     * @param username = login name
     * @param password = login password
     * @throws RestException 
     */
    public JiraRestClient(URI uri, String username, String password) throws RestException {
        this.username = username;

    	//Apache HTTP client setup 
    	ApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, Boolean.TRUE);
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION, Boolean.TRUE);
        clientConfig.getState().setCredentials(AuthScope.ANY_REALM, uri.getHost(), uri.getPort(), username, password);
        this.client = ApacheHttpClient.create(clientConfig);
        
        //check for errors
        ClientResponse clientResponse = client.resource(uri).get(ClientResponse.class);
        Status status = clientResponse.getClientResponseStatus();
        if(status.getFamily() == Family.CLIENT_ERROR || status.getFamily() == Family.SERVER_ERROR) {
        	throw new RestException(clientResponse);
        }
        
        this.baseUri = UriBuilder.fromUri(uri).path(RestConstants.BASE_REST_PATH).build();
        clientResponse = this.client.resource(baseUri).type(MediaType.APPLICATION_JSON).
        		accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        status = clientResponse.getClientResponseStatus();
        if(status == Status.UNAUTHORIZED) {
        	throw new RestException(clientResponse);
        }
    }

    /**
     * Gets the Apache HTTP client.
     *
     * @return the client
     */
    public ApacheHttpClient getClient() {
        return client;
    }

    /**
     * Gets the base URI.
     *
     * @return the base URI
     */
    public URI getBaseUri() {
        return baseUri;
    }

    public String getUsername() {
        return username;
    }
}
