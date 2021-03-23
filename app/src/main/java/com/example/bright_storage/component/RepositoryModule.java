package com.example.bright_storage.component;

import android.util.Log;

import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.repository.CategoryRepository;
import com.example.bright_storage.repository.StorageUnitCategoryRepository;
import com.example.bright_storage.repository.StorageUnitRepository;

import org.xutils.DbManager;
import org.xutils.x;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lombok.SneakyThrows;

@Module
public class RepositoryModule {

    private static final String TAG = "RepositoryModule";

    private final DbManager.DaoConfig daoConfig;

    private final DbManager dbManager;

    @SneakyThrows
    public RepositoryModule(){
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("brightstorage.db")
                .setDbVersion(3)
                .setAllowTransaction(true)
                .setDbOpenListener(db -> {
                    db.getDatabase().enableWriteAheadLogging();
                })
                .setDbUpgradeListener((db, oldVersion, newVersion) -> {
                    db.dropTable(Category.class);
                    db.dropTable(StorageUnit.class);
                })
                .setTableCreateListener((db, table) -> {
                    Log.i(TAG, "onCreate: " + table.getName());
                });
        this.daoConfig = daoConfig;
        this.dbManager = x.getDb(daoConfig);
    }

    @Singleton
    @Provides
    public DbManager providerDbManager(){
        return dbManager;
    }

    @Singleton
    @Provides
    public StorageUnitRepository providerStorageUnitRepository(){
        return new StorageUnitRepository();
    }

    @Singleton
    @Provides
    public CategoryRepository providerCategoryRepository(){
        return new CategoryRepository();
    }

    @Singleton
    @Provides
    public StorageUnitCategoryRepository providerStorageUnitCategoryRepository(){
        return new StorageUnitCategoryRepository();
    }
}
