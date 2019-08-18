package com.investor.utility;

import com.common.exceptions.CommonClientException;
import com.common.exceptions.CommonInternalServerErrorException;
import com.common.exceptions.CommonNotFoundException;
import com.common.exceptions.CommonUnauthorizedException;
import com.investor.config.InvestorConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class HttpUtility {

    @Autowired
    InvestorConfig config;

    public HttpUriRequest buildGetByIdRequest(Map<String, String> headers, String id) {

        HttpUriRequest request = RequestBuilder.get()
                .setUri(config.getDbEndpoint() + "/" + id)
                .setHeader(HttpHeaders.CONTENT_TYPE, headers.get(HttpHeaders.CONTENT_TYPE))
                .setHeader(HttpHeaders.ACCEPT, headers.get(HttpHeaders.ACCEPT))
                .setHeader("x-apiKey", headers.get("x-apiKey"))
                .build();

        return request;
    }

    public HttpUriRequest buildPostRequest(Map<String, String> headers,
                                           String investorString) throws UnsupportedEncodingException {

        HttpUriRequest request = RequestBuilder.post()
                .setUri(config.getDbEndpoint())
                .setHeader(HttpHeaders.CONTENT_TYPE, headers.get(HttpHeaders.CONTENT_TYPE))
                .setHeader(HttpHeaders.ACCEPT, headers.get(HttpHeaders.ACCEPT))
                .setHeader("x-apiKey", headers.get("x-apiKey"))
                .setEntity(new StringEntity(investorString))
                .build();

        return request;
    }

    public HttpUriRequest buildPutRequest(Map<String, String> headers, String id,
                                          String investorString) throws UnsupportedEncodingException {

        HttpUriRequest request = RequestBuilder.put()
                .setUri(config.getDbEndpoint() + "/" + id)
                .setHeader(HttpHeaders.CONTENT_TYPE, headers.get(HttpHeaders.CONTENT_TYPE))
                .setHeader(HttpHeaders.ACCEPT, headers.get(HttpHeaders.ACCEPT))
                .setHeader("x-apiKey", headers.get("x-apiKey"))
                .setEntity(new StringEntity(investorString))
                .build();

        return request;
    }

    public HttpUriRequest buildGetRequest(Map<String, String> headers) {

        HttpUriRequest request = RequestBuilder.get()
                .setUri(config.getDbEndpoint())
                .setHeader(HttpHeaders.CONTENT_TYPE, headers.get(HttpHeaders.CONTENT_TYPE))
                .setHeader(HttpHeaders.ACCEPT, headers.get(HttpHeaders.ACCEPT))
                .setHeader("x-apiKey", headers.get("x-apiKey"))
                .build();

        return request;
    }

    public HttpUriRequest buildDeleteRequest(Map<String, String> headers, String id) {

        HttpUriRequest request = RequestBuilder.delete()
                .setUri(config.getDbEndpoint() + "/" + id)
                .setHeader(HttpHeaders.CONTENT_TYPE, headers.get(HttpHeaders.CONTENT_TYPE))
                .setHeader(HttpHeaders.ACCEPT, headers.get(HttpHeaders.ACCEPT))
                .setHeader("x-apiKey", headers.get("x-apiKey"))
                .build();

        return request;
    }

    public ResponseHandler handleResponse() {

        ResponseHandler<String> putResponseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else if (status == 403) {
                throw new CommonUnauthorizedException("You api key is invalid!");
            } else if (status == 400) {
                throw new CommonClientException(EntityUtils.toString(response.getEntity()));
            } else {
                throw new CommonInternalServerErrorException("Internal Server error, please try again.");
            }
        };
        return putResponseHandler;
    }

    public String executeGetRequest(CloseableHttpClient httpclient, HttpUriRequest request,
                                    ResponseHandler handler,
                                    String message) throws IOException {

        String responseBody = (String) httpclient.execute(request, handler);
        if (!responseBody.contains("name")) {
            throw new CommonNotFoundException(message);
        }
        return responseBody;
    }

    public static String executeCommonRequest(CloseableHttpClient httpclient, HttpUriRequest request,
                                              ResponseHandler handler) throws IOException {

        return (String) httpclient.execute(request, handler);

    }
}
