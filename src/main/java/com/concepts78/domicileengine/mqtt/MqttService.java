package com.concepts78.domicileengine.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter;

    public MqttService() {
        this.mqttPahoMessageDrivenChannelAdapter = createMqttPahoMessageDrivenChannelAdapter();
    }

    private MqttPahoClientFactory createMqttClientFactory() {

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

        //TODO
        String password = "maE!qdyDAr8umAUCYHBz8YMnhh4deV6G";
        String[] serverUris = { "tcp://192.168.20.18:1883" };

        mqttConnectOptions.setUserName("admin");
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(serverUris);

        DefaultMqttPahoClientFactory mqttPahoClientFactory = new DefaultMqttPahoClientFactory();
        mqttPahoClientFactory.setConnectionOptions(mqttConnectOptions);

        return mqttPahoClientFactory;
    }

    private MqttPahoMessageDrivenChannelAdapter createMqttPahoMessageDrivenChannelAdapter() {
        return new MqttPahoMessageDrivenChannelAdapter(
                //TODO
                "domicile-zigbee-random-string",
                this.createMqttClientFactory(),
                "zigbee2mqtt/bridge/devices",
                "zigbee2mqtt/bridge/groups"
        );
    }

    public MqttPahoMessageDrivenChannelAdapter getMqttChannelAdapter() {
        return this.mqttPahoMessageDrivenChannelAdapter;
    }
}
