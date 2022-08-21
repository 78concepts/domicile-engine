package com.concepts78.domicile.engine.services;

import com.concepts78.domicile.dto.DeviceDto;
import com.concepts78.domicile.dto.UpdateDeviceBatteryRequestDto;
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
public class DevicesService {

    private Logger logger = LogManager.getLogger(DevicesService.class);

    public DeviceDto getDeviceByFriendlyName(String friendlyName) {

        String uri = "http://localhost:8081/devices/friendly-name";
        ObjectMapper mapper = new ObjectMapper();

        logger.info(String.format("Get device with friendly name: %s", friendlyName));

        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(String.format("%s/%s", uri, friendlyName.replaceAll(" ", "%20"))))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(), DeviceDto.class);

        } catch (IOException  | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveDevices(List<ZigbeeDevice> devices) {

        try {

            String uri = "http://localhost:8081/devices";
            String payload = new ObjectMapper().writeValueAsString(devices);
            ObjectMapper mapper = new ObjectMapper();

            logger.info(String.format("Saving devices with payload: %s", payload));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(uri))
                    .PUT(HttpRequest.BodyPublishers.ofString(payload))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            List<DeviceDto> responseData = Arrays.stream(mapper.readValue(response.body(), DeviceDto[].class)).toList();

            if(responseData.size() < devices.size()) {
                logger.error("Expected the result size to be at least as large as the payload");
                return false;
            }

            return true;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean setBattery(UUID id, Date date, Double battery) {

        try {

            String uri = String.format("http://localhost:8081/devices/%s/battery", id);

            UpdateDeviceBatteryRequestDto dto = new UpdateDeviceBatteryRequestDto();
            dto.setValue(battery);
            String payload = new ObjectMapper().writeValueAsString(dto);
            ObjectMapper mapper = new ObjectMapper();

            logger.info(String.format("Updating battery for device: %s with payload: %s", id, payload));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(uri))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(payload))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            DeviceDto responseData = mapper.readValue(response.body(), DeviceDto.class);

            return true;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean deviceAllowsStateControl(ZigbeeDevice device) {
        if(device.getDefinition() == null || device.getDefinition().get("exposes") == null) {
            return false;
        }

        List<Map> exposes = ((List)device.getDefinition().get("exposes"));
        for(int i = 0; i < exposes.size(); i++) {
            List<Map> features = (List)exposes.get(i).get("features");
            if(features == null) {
                continue;
            }

            if(features
                .stream()
                .filter(x -> x.containsKey("property") && x.get("property").equals("state"))
                .findFirst()
                .isPresent()) {
                return true;
            }
        }

        return false;
    }

    public boolean isReportFromControllableDevice(Map report) {
        return report != null && report.get("state") != null;
    }

    public Double getDoubleValue(Object value) {

        try {
            return (Double)value;
        } catch(ClassCastException e) {
            try {
                return ((Integer) value).doubleValue();
            } catch(ClassCastException ee) {
                return Double.valueOf((String) value);
            }
        }
    }
}
