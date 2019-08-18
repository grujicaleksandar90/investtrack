package com.investor.utility;

import com.investor.entities.Investor;
import com.investor.enums.MembershipType;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class Util {

    public boolean validateInvestor(Investor investor) throws IllegalAccessException {

        for (Field field : investor.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(investor) == " " || field.get(investor) == null) {
                return false;
            }
        }
        return true;
    }

    public Investor resolveUpdatedInvestorFields(Investor existingInvestor, Investor updatedInvestor) throws JSONException, IllegalAccessException {

        if (updatedInvestor.getName() == null || updatedInvestor.getName() == " ") {
            updatedInvestor.setName(existingInvestor.getName());
        }
        if (updatedInvestor.getCity() == null || updatedInvestor.getCity() == " ") {
            updatedInvestor.setCity(existingInvestor.getCity());
        }
        if (updatedInvestor.getState() == null || updatedInvestor.getState() == " ") {
            updatedInvestor.setState(existingInvestor.getState());
        }
        if (updatedInvestor.getMembershipType() == null) {
            if (existingInvestor.getMembershipType() == MembershipType.PREMIUM) {
                updatedInvestor.setMembershipType(MembershipType.PREMIUM);
            } else if (existingInvestor.getMembershipType() == MembershipType.GOLD) {
                updatedInvestor.setMembershipType(MembershipType.GOLD);
            } else {
                updatedInvestor.setMembershipType(MembershipType.SILVER);
            }
        }
        if (updatedInvestor.getSignUpDate() == null || updatedInvestor.getSignUpDate() == " ") {
            updatedInvestor.setSignUpDate(existingInvestor.getSignUpDate());
        }

        return updatedInvestor;
    }

    public Map<String, String> prepareHeaders(String accept, String contentType, String apiKey) {

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, accept);
        headers.put(HttpHeaders.CONTENT_TYPE, contentType);
        headers.put("x-apiKey", apiKey);

        return headers;
    }
}
