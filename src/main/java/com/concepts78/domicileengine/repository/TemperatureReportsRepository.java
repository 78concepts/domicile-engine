package com.concepts78.domicileengine.repository;

import com.concepts78.domicileengine.model.Device;
import com.concepts78.domicileengine.model.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TemperatureReportsRepository implements ReportsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public TemperatureReportsRepository(DataSource dataSource) {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("temperature_reports").usingGeneratedKeyColumns("id");
    }

    private Logger logger = LogManager.getLogger(TemperatureReportsRepository.class);


    public Number createReport(Long deviceId, Long zoneId, Date date, Double value) {

        Map parameters = new LinkedHashMap();

        parameters.put("device_id", deviceId);
        parameters.put("zone_id", zoneId);
        parameters.put("date", date);
        parameters.put("value", value);

        return simpleJdbcInsert.executeAndReturnKey(parameters);
    }


    public Number updateDevice(Long id, Date date, String dateCode, String friendlyName, String manufacturer, String modelId, String type, String description, boolean active) {

        Object[] parameters = new Object[] {
                date,
                date,
                dateCode,
                friendlyName,
                manufacturer,
                modelId,
                type,
                description,
                active,
                id
        };

        String query = "UPDATE DEVICES SET DATE_MODIFIED = ?, LAST_SEEN = ?, DATE_CODE = ?, FRIENDLY_NAME = ?, MANUFACTURER = ?, MODEL_ID = ?, TYPE = ?, DESCRIPTION = ?, ACTIVE = ? WHERE ID = ?";

        return this.jdbcTemplate.update(query, parameters);
    }

    public Number setInactive(Long id, Date date) {

        Object[] parameters = new Object[] {
                date,
                false,
                id
        };

        String query = "UPDATE DEVICES SET DATE_MODIFIED = ?, ACTIVE = ? WHERE ID = ?";

        return this.jdbcTemplate.update(query, parameters);
    }

    public Number setBattery(Long id, Date date, Double battery) {

        Object[] parameters = new Object[] {
                battery,
                date,
                date,
                id
        };

        String query = "UPDATE DEVICES SET BATTERY = ?, DATE_MODIFIED = ?, LAST_SEEN = ?, ACTIVE = true WHERE ID = ?";

        return this.jdbcTemplate.update(query, parameters);
    }
}
