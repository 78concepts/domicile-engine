package com.concepts78.domicileengine.handlers;

import com.concepts78.domicileengine.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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

    public MessageHandler build(Message message) {

        switch(message.getHeaders().get("mqtt_receivedTopic").toString()) {

            case "zigbee2mqtt/bridge/devices":
                return devicesMessageHandler;
            case "zigbee2mqtt/bridge/groups":
                return new GroupsMessageHandler();
            default:
                return deviceMessageHandler;
        }
    }
}
