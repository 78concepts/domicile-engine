package com.concepts78.domicile.dto;

import java.util.Date;

public class UpdateZoneLastKnownTemperatureRequestDto {

    private Double value;
    private Date date;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
