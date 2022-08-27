package com.concepts78.domicile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupMemberDto {

    private Integer endpoint;

    @JsonProperty("ieee_address")
    private String ieeeAddress;

    public Integer getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Integer endpoint) {
        this.endpoint = endpoint;
    }

    public String getIeeeAddress() {
        return ieeeAddress;
    }

    public void setIeeeAddress(String ieeeAddress) {
        this.ieeeAddress = ieeeAddress;
    }

}
