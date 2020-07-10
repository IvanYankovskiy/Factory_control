package com.factory.control.dao_tests;

import com.factory.control.configuration.PostgresSharedContainer;
import com.factory.control.domain.Device;
import com.factory.control.domain.DeviceType;
import com.factory.control.domain.ExtruderTelemetry;
import com.factory.control.repository.DeviceRepository;
import com.factory.control.repository.ExtruderTelemetryReportRepository;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExtruderTelemetryReportRepositoryDAOTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresSharedContainer.getInstance();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ExtruderTelemetryReportRepository repository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    @Transactional
    public void test_selectByDeviceAndTimeBeforeAndTimeAfter() {
        Device device = createDevice();

        OffsetDateTime now = OffsetDateTime.now();
        ExtruderTelemetry entity1 = repository.save(new ExtruderTelemetry()
                .setDeviceId(device)
                .setCounter(80)
                .setDensity(BigDecimal.valueOf(1.0))
                .setDiameter(BigDecimal.valueOf(1.75))
                .setTime(now.minusMinutes(125))
        );
        ExtruderTelemetry entity2 = repository.save(new ExtruderTelemetry()
                .setDeviceId(device)
                .setCounter(45)
                .setDensity(BigDecimal.valueOf(1.0))
                .setDiameter(BigDecimal.valueOf(1.75))
                .setTime(now.minusMinutes(90))
        );
        ExtruderTelemetry entity3 = repository.save(new ExtruderTelemetry()
                .setDeviceId(device)
                .setCounter(45)
                .setDensity(BigDecimal.valueOf(1.0))
                .setDiameter(BigDecimal.valueOf(1.75))
                .setTime(now.minusMinutes(38))
        );
        repository.flush();

        //when
        List<ExtruderTelemetry> actual = repository.findExtruderTelemetriesByDeviceIdIsAndTimeAfterAndTimeBeforeOrderByTime(
                device, now.minusMinutes(120), now);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(Lists.newArrayList(entity2, entity3), actual);
    }

    private Device createDevice() {
        Device device = new Device()
                .setName("test device")
                .setToken(UUID.randomUUID().toString())
                .setDescription("description")
                .setDeviceType(DeviceType.EXTRUDER);
        device = testEntityManager.merge(device);
        testEntityManager.flush();
        return device;
    }

}