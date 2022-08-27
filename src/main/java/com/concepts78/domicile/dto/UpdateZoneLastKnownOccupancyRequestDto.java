package com.concepts78.domicile.dto;

import java.util.Date;

public class UpdateZoneLastKnownOccupancyRequestDto {

    private Boolean value;
    private Date date;

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
