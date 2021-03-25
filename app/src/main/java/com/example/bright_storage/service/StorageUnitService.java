package com.example.bright_storage.service;

import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.service.base.AbstractCrudService;
import com.example.bright_storage.service.base.CrudService;

import java.util.Collection;
import java.util.List;

public interface StorageUnitService extends CrudService<StorageUnit, Long> {

    List<StorageUnit> listByCategoriesIn(Collection<Category> categories);
}
