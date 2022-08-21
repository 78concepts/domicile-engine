package com.concepts78.domicile.engine.services;

import com.concepts78.domicile.dto.CreateTemperatureReportDto;
import com.concepts78.domicile.dto.DeviceDto;
import com.concepts78.domicile.dto.TemperatureReportDto;
import com.concepts78.domicile.model.ZigbeeDevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public TemperatureReportDto createTemperatureReport(UUID deviceId, UUID zoneId, Double value) {

        String uri = "http://localhost:8081/reports/temperature";
        ObjectMapper mapper = new ObjectMapper();

        try {
            CreateTemperatureReportDto dto = new CreateTemperatureReportDto();

            dto.setDeviceId(deviceId);
            dto.setZoneId(zoneId);
            dto.setValue(value);

            String payload = new ObjectMapper().writeValueAsString(dto);

            logger.info(String.format("Create temperature report for device: %s with value: %.2f", deviceId, value));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(), TemperatureReportDto.class);

        } catch (IOException  | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
