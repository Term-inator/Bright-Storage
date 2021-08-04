package com.example.bright_storage.service.impl;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.AccessLog;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.entity.StorageUnitCategory;
import com.example.bright_storage.repository.AccessLogRepository;
import com.example.bright_storage.repository.StorageUnitCategoryRepository;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.base.AbstractCrudService;
import com.example.bright_storage.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class StorageUnitServiceImpl extends AbstractCrudService<StorageUnit, Long> implements StorageUnitService {

    @Inject
    StorageUnitRepository storageUnitRepository;

    @Inject
    AccessLogRepository accessLogRepository;

    public StorageUnitServiceImpl() {
        super();
        DaggerServiceComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        super.repository = storageUnitRepository;
    }

    @Override
    public List<StorageUnit> listByCategoriesIn(Collection<Category> categories) {
        return storageUnitRepository.listByCategoriesIn(categories);
    }

    @Override
    public List<StorageUnit> listRecentActiveStorageUnit(String name) {
        List<AccessLog> recentLogs = accessLogRepository.listRecentAccessLogs();
        List<Long> storageUnitIds = new ArrayList<>(recentLogs.size());
        for (AccessLog recentLog : recentLogs) {
            storageUnitIds.add(recentLog.getStorageUnitId());
        }
        List<StorageUnit> storageUnits = storageUnitRepository.findByIds(storageUnitIds);
        if(StringUtil.hasText(name)){
            List<StorageUnit> filteredStorageUnits = new ArrayList<>();
            for (StorageUnit storageUnit : storageUnits) {
                if(storageUnit.getName().contains(name)){
                    filteredStorageUnits.add(storageUnit);
                }
            }
            return filteredStorageUnits;
        }
        return storageUnits;
   }

    @Override
    public List<StorageUnit> listRecentActiveStorageUnit() {
        return listRecentActiveStorageUnit("");
    }

    @Override
    public List<StorageUnit> listLongestVisitedStorageUnits() {
        return storageUnitRepository.listLongestVisitedStorageUnits(null, null);
    }

    @Override
    public List<StorageUnit> listLongestVisitedStorageUnits(Integer storageUnitType, Integer limit) {
        return storageUnitRepository.listLongestVisitedStorageUnits(storageUnitType, limit);
    }

    @Override
    public StorageUnit create(StorageUnit storageUnit) {
        StorageUnit result = super.create(storageUnit);
        accessLogRepository.save(AccessLog.storageUnitModified(result.getLocalId()));
        return result;
    }

    @Override
    public List<StorageUnit> create(Collection<StorageUnit> storageUnits) {
        List<StorageUnit> result = super.create(storageUnits);

        List<AccessLog> accessLogs = new ArrayList<>(result.size());
        for(StorageUnit storageUnit: result){
            accessLogs.add(AccessLog.storageUnitModified(storageUnit.getLocalId()));
        }
        accessLogRepository.save(accessLogs);

        return result;
    }

    @Override
    public void update(StorageUnit storageUnit) {
        storageUnit.setLastAccessTime(new Date());
        super.update(storageUnit);
    }

    @Override
    public void update(Collection<StorageUnit> storageUnits) {
        Date now = new Date();
        for (StorageUnit storageUnit : storageUnits) {
            storageUnit.setLastAccessTime(now);
        }
        super.update(storageUnits);
    }

}
