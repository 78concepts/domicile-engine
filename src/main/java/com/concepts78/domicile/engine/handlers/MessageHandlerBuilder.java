package com.concepts78.domicile.engine.handlers;

import com.concepts78.domicile.engine.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerBuilder {

    @Autowired
    MqttService mqttService;

    @Autowired
    DevicesMessageHandler devicesMessageHandler;

    @Autowired
    DeviceMessageHandler deviceMessageHandler;

    @Autowired
    GroupsMessageHandler groupsMessageHandler;

    public MessageHandler build(Message message) {

        switch(message.getHeaders().get("mqtt_receivedTopic").toString()) {

            case "zigbee2mqtt/bridge/devices":
                return devicesMessageHandler;
            case "zigbee2mqtt/bridge/groups":
                return groupsMessageHandler;
            default:
                return deviceMessageHandler;
        }
    }
}
