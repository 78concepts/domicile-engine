package com.concepts78.domicileengine.handlers;

import com.concepts78.domicileengine.model.Device;
import com.concepts78.domicileengine.mqtt.MqttService;
import com.concepts78.domicileengine.repository.DevicesRepository;
import com.concepts78.domicileengine.repository.TemperatureReportsRepository;
import com.concepts78.domicileengine.services.DevicesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
public class DeviceMessageHandler implements MessageHandler {

    @Autowired
    MqttService mqttService;

    @Autowired
    DevicesService devicesService;

    @Autowired
    TemperatureReportsRepository temperatureReportsRepository;

    private Logger logger = LogManager.getLogger(DeviceMessageHandler.class);

    public void handle(Message message) {

        try {
            String reportString = message.getPayload().toString();
            if(reportString == null || reportString.equals("")) {
                return;
            }
            handleReport((String)message.getHeaders().get("mqtt_receivedTopic"), new ObjectMapper().readValue(reportString, Map.class));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void handleReport(String topic, Map report) {

        Device device = devicesService.getDeviceByFriendlyName(topic.replace("zigbee2mqtt/", ""));
        if(device == null) {
            logger.error(String.format("Device not found for topic: %s", topic));
            return;
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        Date date = Date.from(zonedDateTime.toInstant());

        if(report.get("battery") != null) {
            devicesService.setBattery(device, date, getDoubleValue(report.get("battery")));
        }

        if(device.getZoneId() == null) {
            logger.error("Device does not belong to a zone, ignoring report");
            return;
        }

        if(report.get("temperature") != null) {
            temperatureReportsRepository.createReport(device.getId(), device.getZoneId(), date, getDoubleValue(report.get("temperature")));
        }

        //TODO other types of reports
        System.out.println(report);
//        {battery=93.5, illuminance=4895, illuminance_lux=3, linkquality=255, motion_sensitivity=medium, occupancy=false, occupancy_timeout=30, temperature=13.75, update={state=idle}, update_available=false}
    }

    private Double getDoubleValue(Object value) {

        Double doubleValue;

        try {
            doubleValue = (Double)value;
        } catch(ClassCastException e) {
            doubleValue = ((Integer)value).doubleValue();
        }

        return doubleValue;
    }
}
