package com.factory.control.repository.device;

import com.factory.control.domain.entities.device.Device;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends DeviceBaseRepository<Device, Integer> {
}
