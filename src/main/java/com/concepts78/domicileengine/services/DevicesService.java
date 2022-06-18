package com.concepts78.domicileengine.services;

import com.concepts78.domicileengine.model.Device;
import com.concepts78.domicileengine.model.ZigbeeDevice;
import com.concepts78.domicileengine.repository.DevicesRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DevicesService {

    private DevicesRepository devicesRepository;

    public DevicesService(DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    public Device getDeviceByFriendlyName(String friendlyName) {
        Optional<Device> deviceOptional = devicesRepository.getDeviceByFriendlyName(friendlyName);
        return deviceOptional.isPresent() ? deviceOptional.get() : null;
    }

    public void saveDevices(List<ZigbeeDevice> devices) {

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        Date date = Date.from(zonedDateTime.toInstant());

        devices.stream().forEach(x -> {

            Optional<Device> deviceOptional = devicesRepository.getDeviceByIeeeAddress(x.getIeeeAddress());
            if (deviceOptional.isPresent()) {

                Device device = deviceOptional.get();

                updateDeviceFromNetwork(device, x, date);

                devicesRepository.updateDevice(device.getId(), date, device.getDateCode(), device.getFriendlyName(), device.getManufacturer(), device.getModelId(), device.getType(), device.getDescription(), device.isActive());

            } else {

                Device device = new Device();

                device.setUuid(UUID.randomUUID());
                device.setIeeeAddress(x.getIeeeAddress());
                device.setDateCreated(date);

                updateDeviceFromNetwork(device, x, date);

                Number id = devicesRepository.createDevice(device.getUuid(), date, device.getIeeeAddress(), device.getDateCode(), device.getFriendlyName(), device.getManufacturer(), device.getModelId(), device.getType(), device.getDescription(), device.isActive());

                if (id.longValue() > 0) {
                    device.setId(id.longValue());
                }
            }
        });

        devicesRepository.findAllNotInList(devices.stream().map(x -> x.getIeeeAddress()).collect(Collectors.toList()))
                .stream().forEach(x -> {
                    devicesRepository.setInactive(x.getId(), date);
                });
    }

    private void updateDeviceFromNetwork(Device device, ZigbeeDevice payload, Date date) {

        device.setDateModified(date);
        device.setDateCode(payload.getDateCode());
        device.setFriendlyName(payload.getFriendlyName());
        device.setManufacturer(payload.getManufacturer());
        device.setModelId(payload.getModelId());
        device.setLastSeen(date);
        device.setType(payload.getType());
        device.setActive(true);

        if (payload.getDefinition() != null) {
            if(payload.getDefinition().get("description") != null) {
                device.setDescription((String) payload.getDefinition().get("description"));
            }

            //TODO
//            System.out.println(payload.getDefinition().get("exposes"));
//
//                    {description=Aqara temperature, humidity and pressure sensor,
//                            exposes=[
//                        {access=1, description=Remaining battery in %, name=battery, property=battery, type=numeric, unit=%, value_max=100, value_min=0},
//                        {access=1, description=Measured temperature value, name=temperature, property=temperature, type=numeric, unit=°C},
//                        {access=1, description=Measured relative humidity, name=humidity, property=humidity, type=numeric, unit=%},
//                        {access=1, description=The measured atmospheric pressure, name=pressure, property=pressure, type=numeric, unit=hPa},
//                        {access=1, description=Voltage of the battery in millivolts, name=voltage, property=voltage, type=numeric, unit=mV},
//                        {access=1, description=Link quality (signal strength), name=linkquality, property=linkquality, type=numeric, unit=lqi, value_max=255, value_min=0}
//                        ],
//                        model=WSDCGQ11LM, options=[
//                        {access=2, description=Number of digits after decimal point for temperature, takes into effect on next report of device., name=temperature_precision, property=temperature_precision, type=numeric, value_max=3, value_min=0},
//                        {access=2, description=Calibrates the temperature value (absolute offset), takes into effect on next report of device., name=temperature_calibration, property=temperature_calibration, type=numeric},
//                        {access=2, description=Number of digits after decimal point for humidity, takes into effect on next report of device., name=humidity_precision, property=humidity_precision, type=numeric, value_max=3, value_min=0}, {access=2, description=Calibrates the humidity value (absolute offset), takes into effect on next report of device., name=humidity_calibration, property=humidity_calibration, type=numeric}, {access=2, description=Number of digits after decimal point for pressure, takes into effect on next report of device., name=pressure_precision, property=pressure_precision, type=numeric,
//                            value_max=3, value_min=0}, {access=2, description=Calibrates the pressure value (absolute offset), takes into effect on next report of device., name=pressure_calibration, property=pressure_calibration, type=numeric}],
//                            supports_ota=false, vendor=Xiaomi}

        }


    }

    public boolean setBattery(Device device, Date date, Double battery) {

        return devicesRepository.setBattery(device.getId(), date, battery).intValue() > 0;
    }
}
