package com.example.bright_storage.service.impl;

import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.StorageUnitService;

import org.xutils.ex.DbException;

import java.util.List;

import javax.inject.Inject;

import dagger.internal.DaggerCollections;

public class StorageUnitServiceImpl implements StorageUnitService {

    @Inject
    StorageUnitRepository storageUnitRepository;

    public StorageUnitServiceImpl() {
        DaggerServiceComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
    }

    @Override
    public List<StorageUnit> findAll() {
        try {
            return storageUnitRepository.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StorageUnit save() {
        StorageUnit storageUnit = new StorageUnit();
        storageUnit.setRemoteId(33L);
        storageUnit.setName("testN");
        try {
            return storageUnitRepository.save(storageUnit);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return storageUnit;
    }
}
