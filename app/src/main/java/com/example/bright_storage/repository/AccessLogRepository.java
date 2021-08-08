package com.example.bright_storage.repository;


import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.exception.DBException;
import com.example.bright_storage.model.entity.AccessLog;

import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AccessLogRepository extends AbstractRepository<AccessLog, Long>{

    public AccessLogRepository() {
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        createTableIfNotExists();
    }

    public List<AccessLog> deleteByStorageUnitId(Long id){
        try{
            List<AccessLog> logs = manager.selector(AccessLog.class)
                    .where("storage_unit_id", "=", id)
                    .findAll();
            delete(logs);
            return logs;
        } catch (Exception e){
            throw new DBException(e.getMessage());
        }
    }

    public List<AccessLog> listRecentAccessLogs(){
        try {
            Calendar monthAgo = Calendar.getInstance();
            monthAgo.add(Calendar.MONTH, -1);
            List<DbModel> dbModels = manager.selector(AccessLog.class)
                    .select("*, COUNT(storage_unit_id) as _count")
                    .where("create_time", ">", monthAgo.getTime())
                    .groupBy("storage_unit_id")
                    .orderBy("_count", true)
                    .findAll();
            List<AccessLog> recentLogs = new ArrayList<>(dbModels.size());
            for(DbModel dbModel: dbModels){
                long now = System.currentTimeMillis();
                AccessLog accessLog = new AccessLog();
                accessLog.setId(dbModel.getLong("id", 0L));
                accessLog.setStorageUnitId(dbModel.getLong("storage_unit_id", 0L));
                accessLog.setAccessType(dbModel.getInt("access_type", 0));
                accessLog.setCreateTime(dbModel.getDate("create_time", now));
                accessLog.setUpdateTime(dbModel.getDate("update_time", now));
                recentLogs.add(accessLog);
            }
            return recentLogs;
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }
}
