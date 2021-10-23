package com.factory.control.controller.mapper;

import com.factory.control.controller.dto.ExtruderRawTelemetryAggregatedReportDTO;
import com.factory.control.controller.dto.ExtruderRawTelemetryRecordDTO;
import com.factory.control.domain.bo.ExtruderRawTelemetryReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.temporal.Temporal;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExtruderRawTelemetryReportMapper {
    private final ExtruderMapper extruderMapper;

    public <T extends Temporal> ExtruderRawTelemetryAggregatedReportDTO<T> mapToDto(ExtruderRawTelemetryReport<T> entity) {
        return new ExtruderRawTelemetryAggregatedReportDTO<>(
                extruderMapper.fromEntityToDto(entity.getExtruder()),
                entity.getAggregationSettings(),
                entity.getTelemetry().stream().map(ExtruderRawTelemetryRecordDTO::new).collect(Collectors.toList())
        );
    }

}
