package com.concepts78.domicileengine.repository;

import java.util.Date;

public interface ReportsRepository {

    Number createReport(Long deviceId, Long zoneId, Date date, Double value);
}
