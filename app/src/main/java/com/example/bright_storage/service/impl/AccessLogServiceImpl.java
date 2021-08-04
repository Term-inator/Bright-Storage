package com.example.bright_storage.service.impl;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.AccessLog;
import com.example.bright_storage.repository.AccessLogRepository;
import com.example.bright_storage.service.AccessLogService;
import com.example.bright_storage.service.base.AbstractCrudService;

import java.util.List;

import javax.inject.Inject;

public class AccessLogServiceImpl
        extends AbstractCrudService<AccessLog, Long>
        implements AccessLogService {

    @Inject
    AccessLogRepository accessLogRepository;

    public AccessLogServiceImpl() {
        super();
        DaggerServiceComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        super.repository = accessLogRepository;
    }

    @Override
    public List<AccessLog> listRecentAccessLog() {
        return accessLogRepository.listRecentAccessLogs();
    }


}
