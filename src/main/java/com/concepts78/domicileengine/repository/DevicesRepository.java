package com.concepts78.domicileengine.repository;

import com.concepts78.domicileengine.model.Device;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DevicesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public DevicesRepository(DataSource dataSource) {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("devices").usingGeneratedKeyColumns("id");
    }

    private Logger logger = LogManager.getLogger(DevicesRepository.class);

    public Optional<Device> getDeviceByIeeeAddress(String ieeeAddress) {

        String query = "SELECT D.* FROM DEVICES D WHERE D.IEEE_ADDRESS = ?";

        try {
            return Optional.of((Device) jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper(Device.class), new Object[] {ieeeAddress}));
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch(Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }

    public Optional<Device> getDeviceByFriendlyName(String friendlyName) {

        String query = "SELECT D.* FROM DEVICES D WHERE D.FRIENDLY_NAME = ?";

        try {
            return Optional.of((Device) jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper(Device.class), new Object[] {friendlyName}));
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch(Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }

    public List<Device> findAllNotInList(List<String> ieeeAddresses) {

        if(ieeeAddresses.size() > 0) {

            String query = String.format("SELECT D.* FROM DEVICES D WHERE D.IEEE_ADDRESS NOT IN (%s)", ieeeAddresses.stream().map(x -> "?").collect(Collectors.joining(", ")));

            try {
                return jdbcTemplate.query(query, new BeanPropertyRowMapper(Device.class), ieeeAddresses.toArray());
            } catch (Exception e) {
                logger.error(e);
                return new ArrayList<>();
            }
        } else {
            return findAll();
        }
    }

    public List<Device> findAll() {

        String query = "SELECT D.* FROM DEVICES D";

        try {
            return jdbcTemplate.query(query, new BeanPropertyRowMapper(Device.class));
        } catch (Exception e) {
            logger.error(e);
            return new ArrayList<>();
        }
    }

    public Number createDevice(UUID uuid, Date date, String ieeeAddress, String dateCode, String friendlyName, String manufacturer, String modelId, String type, String description, boolean active) {

        Map parameters = new LinkedHashMap();

        parameters.put("uuid", uuid);
        parameters.put("ieee_address", ieeeAddress);
        parameters.put("date_created", date);
        parameters.put("date_modified", date);
        parameters.put("date_code", dateCode);
        parameters.put("friendly_name", friendlyName);
        parameters.put("manufacturer", manufacturer);
        parameters.put("model_id", modelId);
        parameters.put("last_seen", date);
        parameters.put("type", type);
        parameters.put("description", description);
        parameters.put("active", active);

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
