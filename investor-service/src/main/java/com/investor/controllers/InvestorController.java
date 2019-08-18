package com.investor.controllers;

import com.common.exceptions.CommonClientException;
import com.investor.entities.Investor;
import com.investor.repositories.InvestorRepository;
import com.investor.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/")
public class InvestorController {

    @Autowired
    InvestorRepository investorRepository;

    @Autowired
    Util util;

    @RequestMapping("/investors")
    public ResponseEntity getAllInvestors(@RequestHeader(HttpHeaders.ACCEPT) String accept,
                                          @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                          @RequestHeader("x-apiKey") String apiKey) throws IOException {

        return investorRepository.getAllInvestors(util.prepareHeaders(accept, contentType, apiKey));
    }

    @RequestMapping("/investors/{id}")
    public ResponseEntity getInvestorById(@PathVariable("id") String id, @RequestHeader(HttpHeaders.ACCEPT) String accept,
                                          @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                          @RequestHeader("x-apiKey") String apiKey) throws IOException {

        return investorRepository.getInvestorById(util.prepareHeaders(accept, contentType, apiKey), id);
    }

    @RequestMapping(value = "/investors", method = RequestMethod.POST)
    public ResponseEntity createInvestor(@RequestBody Investor investorBody, @RequestHeader(HttpHeaders.ACCEPT) String accept,
                                         @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                         @RequestHeader("x-apiKey") String apiKey) throws IllegalAccessException, IOException {

        if (util.validateInvestor(investorBody)) {
            return investorRepository.createInvestor(util.prepareHeaders(accept, contentType, apiKey), investorBody);
        } else {
            throw new CommonClientException("All fields must be populated.");
        }
    }

    @RequestMapping(value = "/investors/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateInvestor(@PathVariable String id, @RequestBody Investor investorBody,
                                         @RequestHeader(HttpHeaders.ACCEPT) String accept,
                                         @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                         @RequestHeader("x-apiKey") String apiKey) throws IOException {

        return investorRepository.updateInvestor(util.prepareHeaders(accept, contentType, apiKey), investorBody, id);
    }

    @RequestMapping(value = "/investors/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteInvestor(@PathVariable String id, @RequestHeader(HttpHeaders.ACCEPT) String accept,
                                         @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                         @RequestHeader("x-apiKey") String apiKey) throws IOException {

        return investorRepository.deleteInvestor(util.prepareHeaders(accept, contentType, apiKey), id);
    }
}
