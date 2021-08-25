package com.factory.control.service.report;

import com.factory.control.controller.dto.ExtruderTelemetryReportDTO;
import com.factory.control.controller.dto.ExtruderTelemetryReportTotalDTO;
import com.factory.control.domain.entities.ExtruderTelemetryReport;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExtruderFinalReportGenerator {

    public ExtruderTelemetryReportTotalDTO generateReport(List<ExtruderTelemetryReport> reports,
                                                          OffsetDateTime from,
                                                          OffsetDateTime to) {
        ExtruderTelemetryReportTotalDTO results = new ExtruderTelemetryReportTotalDTO();
        results.setFrom(from);
        results.setTo(to);
        Map<OffsetDateTime, ExtruderTelemetryReportDTO> details = new LinkedHashMap<>(reports.size() * 15 / 10);
        for (OffsetDateTime currentFrom = from; currentFrom.isBefore(to); currentFrom = currentFrom.plusHours(1)) {
            ExtruderTelemetryReportDTO newReport = new ExtruderTelemetryReportDTO();
            newReport.setStartOfPeriod(currentFrom);
            newReport.setEndOfPeriod(currentFrom.plusHours(1));
            details.put(currentFrom, newReport);
        }
        for (ExtruderTelemetryReport report : reports) {
            OffsetDateTime currentFrom = report.getTime().truncatedTo(ChronoUnit.HOURS);
            if (currentFrom.isBefore(from) || currentFrom.isAfter(to) || currentFrom.isEqual(to)) {
                continue;
            }
            results.addLength(report.getLength());
            results.addWeight(report.getWeight());
            details.computeIfPresent(currentFrom, (key, value) -> {
                value.addLength(report.getLength());
                value.addWeight(report.getWeight());
                return value;
            });
        }
        Comparator<ExtruderTelemetryReportDTO> reportComparator = Comparator
                .comparing(ExtruderTelemetryReportDTO::getStartOfPeriod).reversed();
        results.setDetails(
                details
                        .values()
                        .stream()
                        .sorted(reportComparator)
                        .collect(Collectors.toList())
        );
        return results;
    }
}