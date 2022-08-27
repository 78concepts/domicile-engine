package com.concepts78.domicile.engine.services;

import com.concepts78.domicile.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class ReportsService {

    private Logger logger = LogManager.getLogger(ReportsService.class);

    private String apiHost = "http://localhost:8081";
    private String apiPath = "reports";

    public TemperatureReportDto createTemperatureReport(UUID deviceId, UUID zoneId, Date date, Double value) {

        String type = "temperature";
        String uri = String.format("%s/%s/%s", apiHost, apiPath, type);

        CreateTemperatureReportDto dto = new CreateTemperatureReportDto();

        dto.setDeviceId(deviceId);
        dto.setZoneId(zoneId);
        dto.setDate(date);
        dto.setValue(value);

        return (TemperatureReportDto)createReport(uri, deviceId, dto, type, TemperatureReportDto.class);
    }

    public IlluminanceReportDto createIlluminanceReport(UUID deviceId, UUID zoneId, Date date, Double value, Double valueLux) {

        String type = "illuminance";
        String uri = String.format("%s/%s/%s", apiHost, apiPath, type);

        CreateIlluminanceReportDto dto = new CreateIlluminanceReportDto();

        dto.setDeviceId(deviceId);
        dto.setZoneId(zoneId);
        dto.setDate(date);
        dto.setValue(value);
        dto.setValueLux(value);

        return (IlluminanceReportDto)createReport(uri, deviceId, dto, type, IlluminanceReportDto.class);
    }

    public OccupancyReportDto createOccupancyReport(UUID deviceId, UUID zoneId, Date date, Boolean value) {

        String type = "occupancy";
        String uri = String.format("%s/%s/%s", apiHost, apiPath, type);

        CreateOccupancyReportDto dto = new CreateOccupancyReportDto();

        dto.setDeviceId(deviceId);
        dto.setZoneId(zoneId);
        dto.setDate(date);
        dto.setValue(value);

        return (OccupancyReportDto) createReport(uri, deviceId, dto , type, OccupancyReportDto.class);
    }


    private Object createReport(String uri, UUID deviceId, Object dto, String type, Class clazz) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String payload = mapper.writeValueAsString(dto);

            logger.info(String.format("Create %s report for device: %s with payload: %s", type, deviceId, payload));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(), clazz);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
