package com.factory.control.service;

import com.factory.control.domain.entities.device.Device;
import com.factory.control.repository.device.DeviceBaseRepository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public abstract class DeviceCrudServiceAbstract<E extends Device, ID extends Serializable> {

    private final DeviceBaseRepository<E, ID> repository;

    public DeviceCrudServiceAbstract(DeviceBaseRepository<E, ID> repository) {
        this.repository = repository;
    }

    public DeviceBaseRepository<E, ID> getRepository() {
        return this.repository;
    }

    public List<E> selectAll() {
        return getRepository().findAll();
    }

    public E selectByToken(String token) {
        return getRepository().findByToken(token);
    }

    public E create(E newDevice) {
        beforeCreate(newDevice);
        return getRepository().save(newDevice);
    }

    public E update(E existedDevice, E deviceWish) {
        beforeUpdate(existedDevice, deviceWish);
        return getRepository().save(existedDevice);
    }

    abstract String getType();

    protected void beforeCreate(E newDevice) {
        newDevice.setToken(UUID.randomUUID().toString());
    }

    protected void beforeUpdate(E existedDevice, E deviceWish) {
        existedDevice.setName(deviceWish.getName());
        existedDevice.setDescription(deviceWish.getDescription());
    }

}
