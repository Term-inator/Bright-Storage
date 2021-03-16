package com.example.bright_storage.repository;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.StorageUnit;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import javax.inject.Inject;

public class StorageUnitRepository extends AbstractRepository<StorageUnit, Long>{

    private static final String TAG = "RelationRepository";

    public StorageUnitRepository() {
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
    }

//    @Override
//    public List<StorageUnit> findAll() throws DbException {
//        return manager.findAll(StorageUnit.class);
//    }
//
//    @Override
//    public StorageUnit save(StorageUnit entity) throws DbException {
//        manager.save(entity);
//        System.out.println("save relation");
//        return entity;
//    }
}
