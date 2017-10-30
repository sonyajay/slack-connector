package org.mule.modules.slack.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

public class JerseySlackRequest extends SlackRequest {

    private static final String PLUS = "+";
    private static final String SPACE = " ";
    private static final URLCodec codec = new URLCodec("UTF-8");
    private final HashMap<String, InputStream> parts;

    JerseySlackRequest(String token, String apiMethod, Map<String, String> queryParams, HashMap<String, InputStream> parts) {
        super(token, apiMethod, queryParams);
        this.parts = parts;
    }

    @Override
    public String execute() {
        WebTarget webTarget = getWebTarget()
                .path(apiMethod);

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            try {
                String encode = codec.encode(entry.getValue());
                encode = encode.replace(PLUS, SPACE);
                webTarget = webTarget.queryParam(entry.getKey(), encode);
            } catch (EncoderException e) {
                throw new RuntimeException(e);
            }
        }

        if(!parts.isEmpty()){
            return sendRequestWithFile(webTarget, parts.get("file"));
        }
        return sendRequest(webTarget);
    }

    private WebTarget getWebTarget() {
        ClientConfig clientConfig = new ClientConfig().register(MultiPartFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);

        return client.target(SLACK_API_BASE_URI).queryParam("token", token);
    }

    private static String sendRequest(WebTarget webTarget) {
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String output = response.readEntity(String.class);
        ErrorHandler.verifyResponse(output);
        return output;
    }

    public static String sendRequestWithFile(WebTarget webTarget, InputStream file) {
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new StreamDataBodyPart("file", file, "file", MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Response response = webTarget.request(MediaType.MULTIPART_FORM_DATA).post(Entity.entity(multiPart, multiPart.getMediaType()));

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.readEntity(String.class);

        ErrorHandler.verifyResponse(output);

        return output;
    }
}
