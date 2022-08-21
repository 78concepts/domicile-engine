package com.concepts78.domicile.dto;

import java.util.Date;
import java.util.UUID;

public class DeviceDto {

    private UUID uuid;
    private String ieeeAddress;
    private Date dateCreated;
    private Date dateModified;
    private String dateCode;
    private String friendlyName;
    private UUID zoneId;
    private String manufacturer;
    private String modelId;
    private String description;
    private Date lastSeen;
    private String type;
    private Float battery;
    private boolean active;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getIeeeAddress() {
        return ieeeAddress;
    }

    public void setIeeeAddress(String ieeeAddress) {
        this.ieeeAddress = ieeeAddress;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
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

    public UUID getZoneId() {
        return zoneId;
    }

    public void setZoneId(UUID zoneId) {
        this.zoneId = zoneId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getBattery() {
        return battery;
    }

    public void setBattery(Float battery) {
        this.battery = battery;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
