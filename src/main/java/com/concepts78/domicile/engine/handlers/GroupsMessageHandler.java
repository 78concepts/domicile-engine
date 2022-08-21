package com.concepts78.domicile.engine.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GroupsMessageHandler implements MessageHandler {

    private Logger logger = LogManager.getLogger(GroupsMessageHandler.class);

    public void handle(Message message) {

        try {
            List<Map> groups = new ObjectMapper().readValue(message.getPayload().toString(), List.class);
            //TODO
            logger.info(groups);
            // Read the list of groups
            // Get the groups from the DB
            // If the group doesn't exist, then create a new group
            // If the group does exist then update
            // Devices
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
