package com.example.bright_storage.repository;

import android.util.Log;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.exception.DBException;
import com.example.bright_storage.model.entity.OperationLog;
import com.example.bright_storage.model.support.BaseResponse;
import com.google.gson.Gson;

import org.xutils.ex.DbException;

public class OperationLogRepository extends AbstractRepository<OperationLog, Long> {

    private static final String TAG = "OperationLogRepository";

    public OperationLogRepository() {
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        createTableIfNotExists();
    }

    public void deleteByLocalId(Long localId){
        try {
            delete(manager.selector(OperationLog.class)
                    .where("local_id", "=", localId)
                    .findAll());
        } catch (DbException e) {
            Log.e(TAG, "deleteByLocalId: ", e);
            throw new DBException(e.getMessage());
        }
    }

}
