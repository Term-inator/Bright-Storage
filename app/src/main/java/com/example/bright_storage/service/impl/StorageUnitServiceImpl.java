package com.example.bright_storage.service.impl;

import android.util.Log;

import com.example.bright_storage.api.StorageUnitRequest;
import com.example.bright_storage.component.CommonModule;
import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.component.RequestModule;
import com.example.bright_storage.constant.OperationConstant;
import com.example.bright_storage.exception.BadRequestException;
import com.example.bright_storage.model.dto.StorageUnitDTO;
import com.example.bright_storage.model.entity.AccessLog;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.OperationLog;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.repository.AccessLogRepository;
import com.example.bright_storage.repository.OperationLogRepository;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.base.AbstractCrudService;
import com.example.bright_storage.util.Assert;
import com.example.bright_storage.util.StringUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import lombok.SneakyThrows;

public class StorageUnitServiceImpl extends AbstractCrudService<StorageUnit, Long> implements StorageUnitService {

    private static final String TAG = "StorageUnitServiceImpl";

    @Inject
    StorageUnitRepository storageUnitRepository;

    @Inject
    AccessLogRepository accessLogRepository;

    @Inject
    OperationLogRepository operationLogRepository;

    @Inject
    Gson gson;

    @Inject
    StorageUnitRequest storageUnitRequest;

    public StorageUnitServiceImpl() {
        super();
        DaggerServiceComponent.builder()
                .repositoryModule(new RepositoryModule())
                .requestModule(new RequestModule())
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
    public List<StorageUnitDTO> listStorageUnitByRelationId(@NotNull Long relationId) {
        try {
            return storageUnitRequest.listStorageUnitByRelationId(relationId).execute().body().getData();
        } catch (IOException e) {
            Log.e(TAG, "listStorageUnitByRelationId: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BaseResponse<Object> shareStorageUnit(@NotNull Long relationId, @NotNull Long storageUnitId) {
        try {
            Map<String, Long> param = new HashMap<>();
            param.put("relationId", relationId);
            param.put("storageId", storageUnitId);
            return storageUnitRequest.shareStorageUnit(param).execute().body();
        } catch (IOException e) {
            Log.e(TAG, "shareStorageUnit: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public StorageUnit create(StorageUnit storageUnit) {
        StorageUnit result = super.create(storageUnit);
        accessLogRepository.save(AccessLog.storageUnitModified(result.getLocalId()));

        // 保存创建记录
        OperationLog operationLog = new OperationLog();
        operationLog.setLocalId(result.getLocalId());
        operationLog.setOperationType(OperationConstant.CREATE);
        operationLog.setData(gson.toJson(storageUnit));
        operationLogRepository.save(operationLog);

        return result;
    }

    @Override
    public List<StorageUnit> create(Collection<StorageUnit> storageUnits) {
        List<StorageUnit> result = super.create(storageUnits);

        List<AccessLog> accessLogs = new ArrayList<>(result.size());
        List<OperationLog> operationLogs = new ArrayList<>(result.size());
        for(StorageUnit storageUnit: result){
            accessLogs.add(AccessLog.storageUnitModified(storageUnit.getLocalId()));
            OperationLog operationLog = new OperationLog();
            operationLog.setLocalId(storageUnit.getLocalId());
            operationLog.setOperationType(OperationConstant.CREATE);
            operationLog.setData(gson.toJson(storageUnit));
            operationLogs.add(operationLog);
        }
        accessLogRepository.save(accessLogs);
        operationLogRepository.save(operationLogs);

        return result;
    }

    @Override
    public void update(StorageUnit storageUnit) {
        Assert.notNull(storageUnit.getLocalId(), "LocalID不能为空");

        StorageUnit old = getNotNullById(storageUnit.getLocalId());

        // 保存修改记录
        Map<String, Object> diff = diff(old, storageUnit);
        diff.remove("categories");
        if(diff.size() > 0){
            OperationLog operationLog = new OperationLog();
            operationLog.setOperationType(OperationConstant.UPDATE);
            operationLog.setData(gson.toJson(diff));
            operationLog.setLocalId(storageUnit.getLocalId());
            operationLog.setRemoteId(storageUnit.getId());
            operationLogRepository.save(operationLog);
        }

        storageUnit.setLastAccessTime(new Date());
        repository.update(storageUnit);
    }

    @Override
    public void update(Collection<StorageUnit> storageUnits) {
        for (StorageUnit storageUnit : storageUnits) {
            update(storageUnit);
        }
    }

    @Override
    public void delete(StorageUnit storageUnit) {
        if(Integer.valueOf(1).equals(storageUnit.getType())){
            // 是容器
            List<StorageUnit> children = storageUnitRepository.listByParentId(storageUnit.getLocalId());
            for (StorageUnit child : children) {
                delete(child);
            }
        }
        repository.delete(storageUnit);

        // 直接对记录进行删除，简化合并操作
        operationLogRepository.deleteByLocalId(storageUnit.getLocalId());
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationType(OperationConstant.DELETE);
        operationLog.setLocalId(storageUnit.getLocalId());
        operationLog.setRemoteId(storageUnit.getId());
        operationLogRepository.save(operationLog);
    }

    @Override
    public void deleteByIdIn(Collection<Long> longs) {
        List<StorageUnit> storageUnits = repository.findByIds(longs);
        for (StorageUnit storageUnit : storageUnits) {
            delete(storageUnit);
        }
    }

    @Override
    public StorageUnit deleteById(Long aLong) {
        StorageUnit storageUnit = getNotNullById(aLong);
        delete(storageUnit);
        return storageUnit;
    }

    @SneakyThrows
    private <T> Map<String, Object> diff(T oldObj, T newObj){
        Map<String, Object> diff = new HashMap<>();
        Field[] fields = oldObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object oldVal = field.get(oldObj);
            Object newVal = field.get(newObj);
            if(oldVal == null){
                if(newVal != null){
                    diff.put(field.getName(), newVal);
                }
            } else if(!oldVal.equals(newVal)){
                diff.put(field.getName(), newVal);
            }
        }
        return diff;
    }

}
