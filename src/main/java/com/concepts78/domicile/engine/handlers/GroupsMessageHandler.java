package com.concepts78.domicile.engine.handlers;

import com.concepts78.domicile.engine.services.DevicesService;
import com.concepts78.domicile.engine.services.GroupsService;
import com.concepts78.domicile.model.ZigbeeDevice;
import com.concepts78.domicile.model.ZigbeeGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GroupsMessageHandler implements MessageHandler {

    private Logger logger = LogManager.getLogger(GroupsMessageHandler.class);

    @Autowired
    private GroupsService groupsService;

    public void handle(Message message) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            List<ZigbeeGroup> groups = Arrays.stream(mapper.readValue(message.getPayload().toString(), ZigbeeGroup[].class)).toList();

            if(!groupsService.saveGroups(groups)) {
                logger.error("Unable to save the groups in the database");
                return;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
