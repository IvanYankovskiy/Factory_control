package com.factory.control.service;

import com.factory.control.domain.entities.Extruder;
import com.factory.control.domain.entities.ExtruderTelemetry;
import com.factory.control.repository.ExtruderTelemetryRepository;
import com.factory.control.service.aggregation.AggregationSettings;
import com.factory.control.service.aggregation.TimePeriodBasedAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtruderTelemetryDomainService {
    private final TimePeriodBasedAggregator<OffsetDateTime, ExtruderTelemetry, ExtruderTelemetry> extruderTelemetryAggregator;
    private final DeviceCrudServiceAbstract<Extruder, Integer> extruderDeviceService;
    private final ExtruderTelemetryRepository extruderTelemetryRepository;

    public List<ExtruderTelemetry> aggregate(String uuid, AggregationSettings<OffsetDateTime> timeSettings) {
        Extruder extruder = extruderDeviceService.selectByUuid(uuid);
        List<ExtruderTelemetry> savedTelemetry = extruderTelemetryRepository
                .findTelemetriesInPeriod(extruder.getId(), timeSettings.getFrom(), timeSettings.getTo())
                .orElse(Collections.emptyList());
        return extruderTelemetryAggregator.aggregate(savedTelemetry, timeSettings.getWindowValue(), timeSettings.getWindowUnit());
    }
}