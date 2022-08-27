package com.concepts78.domicile.engine.services;

import com.concepts78.domicile.dto.GroupDto;
import com.concepts78.domicile.model.ZigbeeGroup;
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
public class GroupsService {

    private Logger logger = LogManager.getLogger(GroupsService.class);

    public boolean saveGroups(List<ZigbeeGroup> groups) {

        try {

            String uri = "http://localhost:8081/groups";
            String payload = new ObjectMapper().writeValueAsString(groups);
            ObjectMapper mapper = new ObjectMapper();

            logger.info(String.format("Saving groups with payload: %s", payload));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(uri))
                    .PUT(HttpRequest.BodyPublishers.ofString(payload))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            List<GroupDto> responseData = Arrays.stream(mapper.readValue(response.body(), GroupDto[].class)).toList();

            if(responseData.size() < groups.size()) {
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
}
