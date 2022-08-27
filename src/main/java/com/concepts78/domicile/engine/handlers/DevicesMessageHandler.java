package com.concepts78.domicile.engine.handlers;

import com.concepts78.domicile.model.ZigbeeDevice;
import com.concepts78.domicile.engine.mqtt.MqttService;
import com.concepts78.domicile.engine.services.DevicesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

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

            if(!devicesService.saveDevices(devices)) {
                logger.error("Unable to save the devices in the database, will not listen");
                return;
            }

            this.subscribeToDeviceTopics(devices);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToDeviceTopics(List<ZigbeeDevice> devices) {

        devices.stream().forEach(x -> {

            switch(x.getType()) {

                case "EndDevice":
                    subscribeToDeviceTopic(x.getFriendlyName());
                    break;

                case "Router":
                    if(!devicesService.deviceAllowsStateControl(x)) {
                        break;
                    }
                    subscribeToDeviceTopic(x.getFriendlyName());
                    break;

                default:
                    break;
            }

        });
    }

    private void subscribeToDeviceTopic(String friendlyName) {
        try {
            mqttService.getMqttChannelAdapter().addTopic("zigbee2mqtt/" + friendlyName, 1);
        } catch (MessagingException e) {
            if(e.getMessage() == null || !e.getMessage().endsWith("is already subscribed.")) {
                throw(e);
            }
            logger.info(e.getMessage());
        }
    }
}
