package com.example.bright_storage.service.impl;

import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.base.AbstractCrudService;

import javax.inject.Inject;

public class StorageUnitServiceImpl extends AbstractCrudService<StorageUnit, Long> implements StorageUnitService {

    @Inject
    StorageUnitRepository storageUnitRepository;

    public StorageUnitServiceImpl() {
        super();
        DaggerServiceComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        super.repository = storageUnitRepository;
    }

    @Override
    public StorageUnit create(StorageUnit storageUnit) {
        return super.create(storageUnit);
    }

    @Override
    public void update(StorageUnit storageUnit) {
        super.update(storageUnit);
    }

}
