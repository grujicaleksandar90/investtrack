package com.investor.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MembershipType {

    PREMIUM("premium"),
    GOLD("gold"),
    SILVER("silver");

    @JsonValue
    private String membershipType;

    MembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getMembershipType() {
        return membershipType;
    }
}
