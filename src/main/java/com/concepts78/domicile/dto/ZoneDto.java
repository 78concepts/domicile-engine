package com.concepts78.domicile.dto;

import java.util.Date;
import java.util.UUID;

public class ZoneDto {

    private UUID uuid;
    private Date dateCreated;
    private Date dateModified;
    private String name;
    private Double lastKnownTemperature;
    private Date lastKnownTemperatureDate;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLastKnownTemperature() {
        return lastKnownTemperature;
    }

    public void setLastKnownTemperature(Double lastKnownTemperature) {
        this.lastKnownTemperature = lastKnownTemperature;
    }

    public Date getLastKnownTemperatureDate() {
        return lastKnownTemperatureDate;
    }

    public void setLastKnownTemperatureDate(Date lastKnownTemperatureDate) {
        this.lastKnownTemperatureDate = lastKnownTemperatureDate;
    }
}
