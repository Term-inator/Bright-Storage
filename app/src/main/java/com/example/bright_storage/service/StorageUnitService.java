package com.example.bright_storage.service;

import com.example.bright_storage.model.entity.StorageUnit;

import java.util.List;

public interface StorageUnitService {

    StorageUnit save();

    List<StorageUnit> findAll();
}
