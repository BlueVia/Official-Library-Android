///@cond private
package com.bluevia.android.rest.commons.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;


/**
 * Interface that will be implemented by REST clients that will allow to
 * manage Bluevia REST HTTP Requests and responses
 *
 * Available connectors may be
 * <ul>
 *   <li>Http Connector API
 *   <li>Mock Connector API
 * </ul>
 *
 * @author Telefonica R&D
 * 
 */
public interface IConnector {
	

	/**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity
     * using POST method
     *
     * @param feedUri the Uri to create the entity remotely via REST
     * @param entity The entity object that will be created remotely
     * @param serializer The serializer to serialize the entity into a stream
     * @param authenticationInfo The oauth info to generate the security headers
     * @return the response stream received
     * @throws IOException
     * @throws HttpException
     * @throws ParseException
     */
    public InputStream createEntity (String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo) throws IOException, HttpException, ParseException;


    /**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity
     * using POST method
     *
     * @param feedUri the Uri to create the entity remotely via REST
     * @param entity The entity object that will be created remotely
     * @param serializer The serializer to serialize the entity into a stream
     * @param authenticationInfo The oauth info to generate the security headers
     * @param parameters The http query parameters for the request
     * @param responseHeaders the response headers received. This object should be created by the method invocator
     * @return the response stream received
     * @throws IOException
     * @throws HttpException
     * @throws ParseException
     */
    public InputStream createEntity (String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo, 
    		HttpQueryParams parameters, HashMap<String, String> responseHeaders)
        throws IOException, HttpException, ParseException;

    /**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity
     * using POST method and a set of request headers
     *
     * @param feedUri the Uri to create the entity remotely via REST
     * @param entity The entity object that will be created remotely
     * @param serializer The serializer to serialize the entity into a stream
     * @param authenticationInfo The oauth info to generate the security headers
     * @param parameters The http query parameters for the request
     * @param requestHeaders The http headers for the request
     * @param responseHeaders the response headers received. This object should be created by the method invocator
     * @return the response stream received
     * @throws IOException
     * @throws HttpException
     * @throws ParseException
     */
    public InputStream createEntity (String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
            HttpQueryParams parameters,
            HashMap<String, String> requestHeaders,
            HashMap<String, String> responseHeaders)
        throws IOException, HttpException, ParseException;

    /**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity
     * with multiple parts using POST method
     * @param feedUri the Uri to create the entity remotely via REST
     * @param entity The entity object that will be created remotely
     * @param serializer The serializer to serialize the entity into a stream
     * @param authenticationInfo The oauth info to generate the security headers
     * @param parameters The http query parameters for the request
     * @param responseHeaders the response headers received. This object should be created by the method invocator
     * @param parts the multiple parts that are part of the entity
     * @return the response stream received
     * @throws IOException
     * @throws HttpException
     * @throws ParseException
     */
    public InputStream createEntity (String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo, 
    		HttpQueryParams parameters, HashMap<String, String> responseHeaders, ArrayList<BlueviaPartBase> parts)
    throws IOException, HttpException, ParseException;



    /**
     * Creates a request using HTTP REST to the gSDP server in order to retrieve an entity
     * from the server using GET method
     *
     * @param feedUri the Uri to create the entity remotely via REST
     * @param authenticationInfo The oauth info to generate the security headers
     * @return the inputstream containing the data received
     * @throws IOException
     * @throws HttpException
     */
    public InputStream retrieveEntity (String feedUri, AuthenticationInfo authenticationInfo) throws IOException, HttpException;

    /**
     * Creates a request using HTTP REST to the gSDP server in order to retrieve an entity
     * from the server using GET method including some query filters
     *
     * @param feedUri the Uri to create the entity remotely via REST
     * @param authenticationInfo The oauth info to generate the security headers
     * @param parameters the parameters in order to do the filtering
     * @param responseHeaders the response headers received. This object should be created by the method invocator
     * @return the inputstream containing the data received
     * @throws IOException
     * @throws HttpException
     */
    public InputStream retrieveEntity(String feedUri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters, HashMap<String, String> responseHeaders) throws IOException, HttpException;
    
    /**
     * Creates a request using HTTP REST to the gSDP server in order to retrieve an entity
     * from the server using GET method including some query filters
     *
     * @param feedUri the Uri to create the entity remotely via REST
     * @param authenticationInfo The oauth info to generate the security headers
     * @param parameters the parameters in order to do the filtering
     * @return the inputstream containing the data received
     * @throws IOException
     * @throws HttpException
     */
    public InputStream retrieveEntity(String feedUri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters) throws IOException, HttpException;

    /**
     * Gets the media content associated to the media URl
     * @param uri the uri of the media to get
     * @return an InputStrem with the media bytes
     * @throws IOException
     * @throws HttpException
     */
    public InputStream getMediaContentAsStream (String uri) throws IOException, HttpException;

    /**
     * Close and free all connector resources
     */
    public void close();
}

///@endcond
