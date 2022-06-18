package com.concepts78.domicileengine.handlers;

import com.concepts78.domicileengine.model.ZigbeeDevice;
import com.concepts78.domicileengine.mqtt.MqttService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import com.concepts78.domicileengine.services.DevicesService;

import java.util.Arrays;
import java.util.List;

@Component
public class DevicesMessageHandler implements MessageHandler {

    @Autowired
    private MqttService mqttService;

    @Autowired
    private DevicesService devicesService;

    private Logger logger = LogManager.getLogger(DevicesMessageHandler.class);

    public void handle(Message message) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            List<ZigbeeDevice> devices = Arrays.stream(mapper.readValue(message.getPayload().toString(), ZigbeeDevice[].class)).toList();

            this.subscribeToDeviceTopics(devices);

            devicesService.saveDevices(devices);

       } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToDeviceTopics(List<ZigbeeDevice> devices) {

        devices.stream().forEach(x -> {
            // TODO Lights
            if (x.getType().equals("EndDevice")) {
                try {
                    mqttService.getMqttChannelAdapter().addTopic("zigbee2mqtt/" + x.getFriendlyName(), 1);
                } catch (MessagingException e) {
                    if(e.getMessage() == null || !e.getMessage().endsWith("is already subscribed.")) {
                        throw(e);
                    }
                    logger.info(e.getMessage());
                }
            }
        });
    }
}
