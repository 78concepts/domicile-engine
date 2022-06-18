package com.concepts78.domicileengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ZigbeeDevice {

    @JsonProperty("ieee_address")
    private String ieeeAddress;

    @JsonProperty("date_code")
    private String dateCode;

    @JsonProperty("friendly_name")
    private String friendlyName;

    private String manufacturer;

    @JsonProperty("model_id")
    private String modelId;

    private String type;

    private Map definition;

    public String getIeeeAddress() {
        return ieeeAddress;
    }

    public void setIeeeAddress(String ieeeAddress) {
        this.ieeeAddress = ieeeAddress;
    }

    public String getDateCode() {
        return dateCode;
    }

    public void setDateCode(String dateCode) {
        this.dateCode = dateCode;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getDefinition() {
        return definition;
    }

    public void setDefinition(Map definition) {
        this.definition = definition;
    }
}
