package com.factory.control.repository;

import com.factory.control.domain.entities.Device;
import com.factory.control.domain.entities.ExtruderTelemetry;
import com.factory.control.domain.entities.ExtruderTelemetryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExtruderTelemetryRepository extends JpaRepository<ExtruderTelemetry, Integer> {

    @Query("select et from ExtruderTelemetry et where et.device.id = :device_id and et.time >= :from and et.time < :to order by et.time")
    Optional<List<ExtruderTelemetry>> findTelemetriesInPeriod(@Param("device_id") Integer deviceId, @Param("from") OffsetDateTime startDateTime, @Param("to") OffsetDateTime endDateTime);

    @Query("select new com.factory.control.domain.entities.ExtruderTelemetryDto(et.device.id, et.counter, et.time) " +
            "from ExtruderTelemetry et " +
            "where " +
            "et.device.id = :device_id " +
            "and et.time >= :from and et.time < :to " +
            "order by et.time")
    Slice<ExtruderTelemetryDto> findTelemetryProjectionsInPeriod(
            @Param("device_id") Integer deviceId,
            @Param("from") OffsetDateTime startDateTime,
            @Param("to") OffsetDateTime endDateTime,
            Pageable pageable
    );

    @Query("select distinct(et.device) from ExtruderTelemetry et where et.time >= :from and et.time < :to ")
    List<Device> selectDistinctDevicesForPeriod(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);

}
