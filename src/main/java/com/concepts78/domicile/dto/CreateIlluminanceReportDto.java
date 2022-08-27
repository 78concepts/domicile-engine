package com.concepts78.domicile.dto;

import java.util.Date;
import java.util.UUID;

public class CreateIlluminanceReportDto {

    private UUID deviceId;
    private UUID zoneId;
    private Date date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
