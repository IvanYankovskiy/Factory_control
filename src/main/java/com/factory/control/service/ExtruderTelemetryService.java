package com.factory.control.service;

import com.factory.control.controller.dto.ExtruderTelemetryDTO;
import com.factory.control.controller.mapper.ExtruderTelemetryMapper;
import com.factory.control.domain.entities.Device;
import com.factory.control.domain.entities.Extruder;
import com.factory.control.domain.entities.ExtruderTelemetry;
import com.factory.control.repository.DeviceBaseRepository;
import com.factory.control.repository.ExtruderTelemetryRepository;
import com.factory.control.service.exception.DeviceIsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class ExtruderTelemetryService {

    private final ExtruderTelemetryMapper extruderTelemetryMapper;

    private final ExtruderTelemetryRepository repository;

    private final DeviceBaseRepository<Extruder, Long> deviceRepository;

    @Autowired
    public ExtruderTelemetryService(ExtruderTelemetryMapper extruderTelemetryMapper,
                                    ExtruderTelemetryRepository repository,
                                    DeviceBaseRepository<Extruder, Long> deviceRepository) {
        this.repository = repository;
        this.extruderTelemetryMapper = extruderTelemetryMapper;
        this.deviceRepository = deviceRepository;
    }

    public String saveTelemetry(String uuid, ExtruderTelemetryDTO telemetryDTO) {
        Device device = deviceRepository.findByUuid(uuid);
        if (Objects.isNull(device)) {
            throw new DeviceIsNotFoundException(uuid);
        }
        ExtruderTelemetry extruderTelemetry = extruderTelemetryMapper.fromDtoToEntity(telemetryDTO);
        extruderTelemetry.setDevice(device);
        extruderTelemetry.setTime(OffsetDateTime.now());
        repository.saveAndFlush(extruderTelemetry);
        return "ok";
    }
}
