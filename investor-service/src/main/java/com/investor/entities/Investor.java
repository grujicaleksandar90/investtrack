package com.investor.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.investor.enums.MembershipType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Investor {

    private String name;

    private String state;

    private String city;

    private MembershipType membershipType;

    private String signUpDate;

}
