package com.concepts78.domicile.engine.handlers;

import com.concepts78.domicile.dto.DeviceDto;
import com.concepts78.domicile.engine.mqtt.MqttService;
import com.concepts78.domicile.engine.services.DevicesService;
import com.concepts78.domicile.engine.services.ReportsService;
import com.concepts78.domicile.engine.services.ZonesService;
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
    ReportsService reportsService;

    @Autowired
    ZonesService zonesService;

    private Logger logger = LogManager.getLogger(DeviceMessageHandler.class);

    public void handle(Message message) {

        //TODO
//        System.out.println(message.getHeaders().get("mqtt_duplicate"));

        try {
            String reportString = message.getPayload().toString();
            if (reportString == null || reportString.equals("")) {
                return;
            }
            handleReport((String) message.getHeaders().get("mqtt_receivedTopic"), new ObjectMapper().readValue(reportString, Map.class));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void handleReport(String topic, Map report) {

        DeviceDto device = devicesService.getDeviceByFriendlyName(topic.replace("zigbee2mqtt/", ""));
        if (device == null) {
            logger.error(String.format("Device not found for topic: %s", topic));
            return;
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        Date date = Date.from(zonedDateTime.toInstant());

        if (report.get("battery") != null) {
            devicesService.setBattery(device.getUuid(), date, devicesService.getDoubleValue(report.get("battery")));
        }

        //TODO Save device state
        if (devicesService.isReportFromControllableDevice(report)) {
            logger.info(report);
            logger.info(report.get("brightness"));
            logger.info(report.get("color"));
            logger.info(report.get("state"));
        }

        if (device.getZoneId() == null) {
            logger.error(String.format("Device does not belong to a zone, ignoring report: %s", device.getUuid()));
            return;
        }

        if (report.get("temperature") != null && isEnvironmentSensor(report)) {
            Double value = devicesService.getDoubleValue(report.get("temperature"));
            reportsService.createTemperatureReport(device.getUuid(), device.getZoneId(), date, value);
            zonesService.updateZoneWithTemperatureReport(device.getZoneId(), date, value);
        }

        //TODO Humidity / pressure

        if (report.get("illuminance") != null) {
            Double value = devicesService.getDoubleValue(report.get("illuminance"));
            Double valueLux = devicesService.getDoubleValue(report.get("illuminance_lux"));
            reportsService.createIlluminanceReport(device.getUuid(), device.getZoneId(), date, value, valueLux);
            //TODO update zone
//            zonesService.updateZoneWithTemperatureReport(device.getZoneId(), date, devicesService.getDoubleValue(report.get("temperature")));
        }

        if (report.get("occupancy") != null) {
            Boolean value = devicesService.getBooleanValue(report.get("occupancy"));
            reportsService.createOccupancyReport(device.getUuid(), device.getZoneId(), date, value);
            zonesService.updateZoneWithOccupancyReport(device.getZoneId(), date, value);
        }
            //TODO other types of reports
//        { illuminance=4895, illuminance_lux=3, linkquality=255, motion_sensitivity=medium, occupancy=false, occupancy_timeout=30, temperature=13.75, update={state=idle}, update_available=false}
//        {brightness=254, color={x=0.4739, y=0.2104}, color_mode=xy, color_temp=500, linkquality=255, state=OFF}
    }

    private boolean isEnvironmentSensor(Map report) {
        return report.get("temperature") != null && report.get("illuminance") == null;
    }
}