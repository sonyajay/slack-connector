package org.mule.modules.slack.client;

import org.mule.module.http.internal.ParameterMap;
import org.mule.module.http.internal.domain.ByteArrayHttpEntity;
import org.mule.module.http.internal.domain.HttpEntity;
import org.mule.module.http.internal.domain.InputStreamHttpEntity;
import org.mule.module.http.internal.domain.MultipartHttpEntity;
import org.mule.module.http.internal.domain.request.HttpRequestBuilder;
import org.mule.module.http.internal.domain.response.HttpResponse;
import org.mule.module.http.internal.multipart.HttpPart;
import org.mule.module.http.internal.request.HttpClient;
import org.mule.util.IOUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MuleHttpClientSlackRequest extends SlackRequest {

    private static final String TOKEN_PARAM = "token";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    private final HashMap<String, InputStream> parts;
    private final HttpClient httpClient;

    MuleHttpClientSlackRequest(String token, String apiMethod, Map<String, String> queryParams, HashMap<String, InputStream> parts, HttpClient httpClient) {
        super(token, apiMethod, queryParams);
        this.parts = parts;
        this.httpClient = httpClient;
    }

    @Override
    public String execute() {

        try {
            ParameterMap parameterMap = new ParameterMap();
            parameterMap.put(TOKEN_PARAM, token);
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                parameterMap.put(entry.getKey(), entry.getValue());
            }

            String method = getMethod();

            HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder()
                    .setUri(SLACK_API_BASE_URI + apiMethod)
                    .setMethod(method)
                    .setQueryParams(parameterMap);

            if(!parts.isEmpty()){
                configureMultiPart(httpRequestBuilder);
            }

            HttpResponse response = httpClient.send(httpRequestBuilder.build(), 10000, false, null);
            HttpEntity entity = response.getEntity();
            String stringResponse = null;
            if (entity instanceof InputStreamHttpEntity) {
                stringResponse = IOUtils.toString(((InputStreamHttpEntity) entity).getInputStream());
            } else if (entity instanceof ByteArrayHttpEntity) {
                stringResponse = IOUtils.toString(((ByteArrayHttpEntity) entity).getContent(), "UTF-8");
            }

            if (response.getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getReasonPhrase());
            }

            ErrorHandler.verifyResponse(stringResponse);

            return stringResponse;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void configureMultiPart(HttpRequestBuilder httpRequestBuilder) {
        ArrayList<HttpPart> httpParts = new ArrayList<>();
        for (Map.Entry<String, InputStream> entry : parts.entrySet()) {
            byte[] bytes = IOUtils.toByteArray(entry.getValue());
            httpParts.add(new HttpPart(entry.getKey(), entry.getKey(), bytes, APPLICATION_OCTET_STREAM, bytes.length));
        }
        httpRequestBuilder.setEntity(new MultipartHttpEntity(httpParts));
    }

    private String getMethod() {
        String method;
        if(parts.isEmpty()){
            method = GET;
        } else {
            method = POST;
        }
        return method;
    }
}
