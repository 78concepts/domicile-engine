package com.concepts78.domicile.engine;

import com.concepts78.domicile.engine.handlers.MessageHandler;
import com.concepts78.domicile.engine.handlers.MessageHandlerBuilder;
import com.concepts78.domicile.engine.mqtt.MqttService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@SpringBootApplication
public class DomicileEngineApplication {

    @Autowired
    MqttService mqttService;

    @Autowired
    MessageHandlerBuilder messageHandlerBuilder;

    private Logger logger = LogManager.getLogger(DomicileEngineApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DomicileEngineApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public IntegrationFlow mqttInbound() {

        return IntegrationFlows.from(mqttService.getMqttChannelAdapter())
            .handle(m -> {
                logger.info("Received message from: " + m.getHeaders().get("mqtt_receivedTopic"));

                MessageHandler messageHandler = messageHandlerBuilder.build(m);
                if(messageHandler != null) {
                    messageHandler.handle(m);
                }
            })
            .get();
    }
}