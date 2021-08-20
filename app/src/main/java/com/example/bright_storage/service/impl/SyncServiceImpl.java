package com.example.bright_storage.service.impl;

import android.util.Log;

import com.example.bright_storage.api.SyncRequest;
import com.example.bright_storage.component.CommonModule;
import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.component.RequestModule;
import com.example.bright_storage.constant.OperationConstant;
import com.example.bright_storage.exception.BadRequestException;
import com.example.bright_storage.model.entity.OperationLog;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.repository.OperationLogRepository;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.SyncService;
import com.example.bright_storage.util.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class SyncServiceImpl implements SyncService {

    private static final String TAG = "SyncServiceImpl";

    @Inject
    SyncRequest syncRequest;

    @Inject
    OperationLogRepository operationLogRepository;

    @Inject
    StorageUnitRepository storageUnitRepository;

    @Inject
    Gson gson;

    public SyncServiceImpl() {
        DaggerServiceComponent.builder()
                .requestModule(new RequestModule())
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
    }

    @Override
    public void push() {
        // 选择所有本地存储的记录作为新纪录
        // 上传成功后删除所有记录
        List<OperationLog> all = operationLogRepository.findAll();
        List<OperationLog> merged = merge(all);
        try {
            List<OperationLog> response = syncRequest.push(merged).execute().body().getData();
            for (OperationLog operationLog : response) {
                // 新建数据保存服务端ID
                if (OperationConstant.CREATE.equals(operationLog.getOperationType())){
                    StorageUnit storageUnit = storageUnitRepository.findById(operationLog.getLocalId());
                    storageUnit.setId(operationLog.getRemoteId());
                    storageUnitRepository.update(storageUnit);
                }
            }
            operationLogRepository.delete(all);
        } catch (IOException e) {
            Log.e(TAG, "push: ", e);
            throw new BadRequestException(e.getMessage());
        }

    }

    @Override
    public void pull() {

    }

    /**
     * 客户端合并操作:
     * 只有更改操作需要合并
     * @param operationLogs
     * @return
     */
    private List<OperationLog> merge(List<OperationLog> operationLogs){
        Map<Long, Map> toModify = new HashMap<>();
        Map<Long, Map> logIds = new HashMap<>(); // 标记要加入到合并之后序列中的操作ID

        // 对数据进行合并
        for (OperationLog operationLog : operationLogs) {
            Long id = operationLog.getLocalId(); // 客户端合并以localId为准

            if (operationLog.getOperationType().equals(OperationConstant.DELETE)){
                // 删除操作直接标记
                if(operationLog.getRemoteId() != null){
                    // 忽略对未同步数据进行删除
                    logIds.put(operationLog.getId(), null);
                }
            } else {
                Map data = gson.fromJson(operationLog.getData(), Map.class);
                Map toBeMerged = toModify.get(id);
                if(toBeMerged == null){
                    toModify.put(id, data);
                    logIds.put(operationLog.getId(), data);
                } else {
                    toBeMerged.putAll(data);
                }
            }
        }

        // 按次序整合新的数据
        List<OperationLog> merged = new ArrayList<>(logIds.size());
        for (OperationLog operationLog : operationLogs) {
            if(OperationConstant.DELETE.equals(operationLog.getOperationType())){
                merged.add(operationLog);
                continue;
            }
            Map data = logIds.get(operationLog.getId());
            if(data != null){
                operationLog.setData(gson.toJson(data));
                merged.add(operationLog);
            }
        }

        return merged;
    }
}
