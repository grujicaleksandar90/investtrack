package com.investor.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investor.config.InvestorConfig;
import com.investor.entities.Investor;
import com.investor.enums.MembershipType;
import com.investor.utility.HttpUtility;
import com.investor.utility.Util;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class InvestorRepository {

    @Autowired
    InvestorConfig config;

    @Autowired
    Util util;

    @Autowired
    HttpUtility httpUtility;

    Logger logger = LoggerFactory.getLogger(InvestorRepository.class);

    public ResponseEntity getAllInvestors(Map<String, String> headers) throws IOException {

        String responseBody = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpUriRequest request = httpUtility.buildGetRequest(headers);

            logger.info("Executing request: {}", request.getRequestLine());

            ResponseHandler<String> responseHandler = httpUtility.handleResponse();
            responseBody = httpUtility.executeGetRequest(httpclient, request, responseHandler, "Not found");
            logger.info("Response: {}", responseBody);
        }
        return new ResponseEntity(responseBody, HttpStatus.OK);
    }

    public ResponseEntity getInvestorById(Map<String, String> headers, String id) throws IOException {

        String responseBody = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpUriRequest request = httpUtility.buildGetByIdRequest(headers, id);

            logger.info("Executing request " + request.getRequestLine());

            ResponseHandler<String> responseHandler = httpUtility.handleResponse();
            responseBody = httpUtility.executeGetRequest(httpclient, request, responseHandler,
                    "Investor with id " + id + " does not exists.");
            logger.info("Response: {}", responseBody);
        }
        return new ResponseEntity(responseBody, HttpStatus.OK);

    }

    public ResponseEntity createInvestor(Map<String, String> headers, Investor investor) throws IOException {

        String responseBody = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            ObjectMapper mapper = new ObjectMapper();
            String investorString = mapper.writeValueAsString(investor);

            HttpUriRequest request = httpUtility.buildPostRequest(headers, investorString);

            logger.info("Executing request " + request.getRequestLine());

            ResponseHandler<String> responseHandler = httpUtility.handleResponse();
            responseBody = HttpUtility.executeCommonRequest(httpclient, request, responseHandler);
            logger.info("Response: {}", responseBody);
        }
        return new ResponseEntity(responseBody, HttpStatus.CREATED);
    }

    public ResponseEntity updateInvestor(Map<String, String> headers, Investor updatedInvestor, String id) throws IOException {

        String responseBody = null;
        Investor investorToShow = null;
        String existingInvestor = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // First get investor by id
            HttpUriRequest getRequest = httpUtility.buildGetByIdRequest(headers, id);

            logger.info("Executing request: {}", getRequest.getRequestLine());

            ResponseHandler<String> getResponseHandler = httpUtility.handleResponse();
            existingInvestor = httpUtility.executeGetRequest(httpclient, getRequest, getResponseHandler,
                    "Investor with id " + id + " does not exists.");

            ObjectMapper mapper = new ObjectMapper();

            Investor existingInvestorFromJson = mapper.readValue(existingInvestor, Investor.class);
            // Need to convert existionInvestor jsonString to Object of Investor
            String investorString = mapper.writeValueAsString(util.resolveUpdatedInvestorFields(existingInvestorFromJson, updatedInvestor));

            HttpUriRequest putRequest = httpUtility.buildPutRequest(headers, id, investorString);

            logger.info("Executing request: {}", putRequest.getRequestLine());

            ResponseHandler<String> putResponseHandler = httpUtility.handleResponse();
            responseBody = HttpUtility.executeCommonRequest(httpclient, putRequest, putResponseHandler);
            JSONObject jsonObject = new JSONObject(responseBody);
            investorToShow = Investor.builder().name(jsonObject.getString("name")).city(jsonObject.getString("city"))
                    .state(jsonObject.getString("state"))
                    .signUpDate(jsonObject.getString("signUpDate"))
                    .build();
            if (jsonObject.get("membershipType") == "premium") {
                investorToShow.setMembershipType(MembershipType.PREMIUM);
            } else if (jsonObject.get("membershipType") == "gold") {
                investorToShow.setMembershipType(MembershipType.GOLD);
            } else {
                investorToShow.setMembershipType(MembershipType.SILVER);
            }
            logger.info("Response: {}", responseBody);
        } catch (JSONException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(investorToShow, HttpStatus.OK);
    }

    public ResponseEntity deleteInvestor(Map<String, String> headers, String id) throws IOException {

        String responseBody = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpUriRequest request = httpUtility.buildDeleteRequest(headers, id);

            logger.info("Executing request: {}", request.getRequestLine());

            ResponseHandler<String> responseHandler = httpUtility.handleResponse();
            responseBody = HttpUtility.executeCommonRequest(httpclient, request, responseHandler);

            logger.info("Response : {}", responseBody);
        }
        return new ResponseEntity("Investor Deleted", HttpStatus.OK);
    }
}
