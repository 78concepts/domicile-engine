package com.concepts78.domicileengine.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Map;

public class GroupsMessageHandler implements MessageHandler {

    public void handle(Message message) {

        try {
            List<Map> groups = new ObjectMapper().readValue(message.getPayload().toString(), List.class);
            //TODO
            System.out.println(groups);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
