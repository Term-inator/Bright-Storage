package com.example.bright_storage.service;

import com.example.bright_storage.model.entity.AccessLog;
import com.example.bright_storage.service.base.CrudService;

import java.util.List;

public interface AccessLogService extends CrudService<AccessLog, Long> {

    List<AccessLog> listRecentAccessLog();
}
