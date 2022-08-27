package com.concepts78.domicile.engine;

import com.concepts78.domicile.model.ZigbeeDevice;
import com.concepts78.domicile.engine.services.DevicesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DevicesServiceTests {

    @Autowired
    DevicesService devicesService;

    @Test
    public void whenNullDefinition_thenDeviceDoesntAllowStateControl() {
        ZigbeeDevice device = new ZigbeeDevice();
        Assertions.assertEquals(devicesService.deviceAllowsStateControl(device), false);
    }

    @Test
    public void whenNullExposes_thenDeviceDoesntAllowStateControl() {
        ZigbeeDevice device = new ZigbeeDevice();
        device.setDefinition(Map.of("test", new ArrayList()));
        Assertions.assertEquals(devicesService.deviceAllowsStateControl(device), false);
    }

    @Test
    public void whenNullFeatures_thenDeviceDoesntAllowStateControl() {
        ZigbeeDevice device = new ZigbeeDevice();
        device.setDefinition(Map.of("exposes", List.of(Map.of("test", new ArrayList()))));
        Assertions.assertEquals(devicesService.deviceAllowsStateControl(device), false);
    }

    @Test
    public void whenNullProperty_thenDeviceDoesntAllowStateControl() {
        ZigbeeDevice device = new ZigbeeDevice();
        device.setDefinition(Map.of("exposes", List.of(Map.of("features", new ArrayList()))));
        Assertions.assertEquals(devicesService.deviceAllowsStateControl(device), false);
    }

    @Test
    public void whenNullState_thenDeviceDoesntAllowStateControl() {
        ZigbeeDevice device = new ZigbeeDevice();
        device.setDefinition(Map.of("exposes", List.of(Map.of("features", List.of(Map.of("property", "test"))))));
        Assertions.assertEquals(devicesService.deviceAllowsStateControl(device), false);
    }

    @Test
    public void whenValidPropertyState_thenDeviceDoesntAllowStateControl() {
        ZigbeeDevice device = new ZigbeeDevice();
        device.setDefinition(Map.of("exposes", List.of(Map.of("features", List.of(Map.of("property", "state"))))));
        Assertions.assertEquals(devicesService.deviceAllowsStateControl(device), true);
    }

    @Test void whenIntegerValue_thenResultIsDouble() {
        Integer value = 42;
        Double result = Double.valueOf(42);
        Assertions.assertEquals(devicesService.getDoubleValue(value), result);
    }

    @Test void whenDoubleValue_thenResultIsDouble() {
        Double value = Double.valueOf(42.0);
        Double result = Double.valueOf(42.0);
        Assertions.assertEquals(devicesService.getDoubleValue(value), result);
    }

    @Test void whenInvalidStringValue_thenResultIsException() {
        String value = "abc";
        Assertions.assertThrowsExactly(NumberFormatException.class, () -> devicesService.getDoubleValue(value));
    }

    @Test void whenValidStringValue_thenResultIsDouble() {
        String value = "42.0";
        Double result = Double.valueOf(42.0);
        Assertions.assertEquals(devicesService.getDoubleValue(value), result);
    }

    @Test void whenNullReport_thenReportNotFromControllableDevice() {
        Assertions.assertEquals(devicesService.isReportFromControllableDevice(null), false);
    }

    @Test void whenNullState_thenReportNotFromControllableDevice() {
        Map report = new LinkedHashMap();
        Assertions.assertEquals(devicesService.isReportFromControllableDevice(report), false);
    }

    @Test void whenStateExists_thenReportFromControllableDevice() {
        Map report = Map.of("state", "OFF");;
        Assertions.assertEquals(devicesService.isReportFromControllableDevice(report), true);
    }
}
