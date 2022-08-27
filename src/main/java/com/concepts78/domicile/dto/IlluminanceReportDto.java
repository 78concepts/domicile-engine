package com.concepts78.domicile.dto;

import java.util.Date;
import java.util.UUID;

public class IlluminanceReportDto {

    private UUID deviceId;
    private UUID zoneId;
    private Date dateCreated;
    private Date dateModified;
    private Double value;
    private Double valueLux;

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getZoneId() {
        return zoneId;
    }

    public void setZoneId(UUID zoneId) {
        this.zoneId = zoneId;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValueLux() {
        return valueLux;
    }

    public void setValueLux(Double valueLux) {
        this.valueLux = valueLux;
    }
}
