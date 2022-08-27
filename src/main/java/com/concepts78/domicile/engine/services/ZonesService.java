package com.concepts78.domicile.engine.services;

import com.concepts78.domicile.dto.*;
import com.concepts78.domicile.model.ZigbeeDevice;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class ZonesService {

    private Logger logger = LogManager.getLogger(ZonesService.class);

    public boolean updateZoneWithTemperatureReport(UUID id, Date date, Double temperature) {

        try {

            String uri = String.format("http://localhost:8081/zones/%s/temperature", id);

            UpdateZoneLastKnownTemperatureRequestDto dto = new UpdateZoneLastKnownTemperatureRequestDto();
            dto.setValue(temperature);
            dto.setDate(date);

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String payload = mapper.writeValueAsString(dto);

            logger.info(String.format("Updating temperature for zone: %s with payload: %s", id, payload));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(uri))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(payload))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ZoneDto responseData = mapper.readValue(response.body(), ZoneDto.class);

            return true;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    // TODO use date
    public boolean updateZoneWithOccupancyReport(UUID id, Date date, Boolean occupancy) {

        try {

            String uri = String.format("http://localhost:8081/zones/%s/occupancy", id);

            UpdateZoneLastKnownOccupancyRequestDto dto = new UpdateZoneLastKnownOccupancyRequestDto();
            dto.setValue(occupancy);
            dto.setDate(date);

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String payload = mapper.writeValueAsString(dto);

            logger.info(String.format("Updating occupancy for zone: %s with payload: %s", id, payload));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(uri))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(payload))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ZoneDto responseData = mapper.readValue(response.body(), ZoneDto.class);

            return true;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

}
