package com.example.bright_storage;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.OperationLog;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.entity.StorageUnitCategory;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.model.support.Pageable;
import com.example.bright_storage.repository.OperationLogRepository;
import com.example.bright_storage.repository.StorageUnitCategoryRepository;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.SyncService;
import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.example.bright_storage.service.impl.SyncServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xutils.db.Selector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";

    @Inject
    StorageUnitService storageUnitService;

    @Inject
    CategoryService categoryService;

    @Inject
    StorageUnitCategoryRepository storageUnitCategoryRepository;

    @Inject
    OperationLogRepository operationLogRepository;

    @Inject
    SyncService syncService;

    @Before
    public void before(){
        storageUnitService = new StorageUnitServiceImpl();
        categoryService = new CategoryServiceImpl();
        storageUnitCategoryRepository = new StorageUnitCategoryRepository();
        operationLogRepository = new OperationLogRepository();
        syncService = new SyncServiceImpl();
    }

    @Test
    public void insertCategory(){
        Category c1 = new Category(null, null, "食品");
        Category c2 = new Category(null, null, "书籍");
        categoryService.create(c1);
        categoryService.create(c2);
    }

    @Test
    public void queryCategory(){
        List<Category> res = categoryService.listAll();
        for(Category ca:res){
            Log.i(TAG, "findCategory: " + ca);
        }
    }

    @Test
    public void insertStorageUnit(){
        List<Category> categories = categoryService.listAll();
        for(int i=0;i<5;i++){
            StorageUnit storageUnit = new StorageUnit();
            storageUnit.setName(i + "@@@@@@");
            storageUnit.setAmount(i*10);
            storageUnit.setNote("Note: " + i);
            storageUnit.setCategories(new HashSet<>(categories));
            storageUnitService.create(storageUnit);

        }
    }

    @Test
    public void updateStorageUnit(){
        StorageUnit storageUnit = storageUnitService.getById(20L);
        storageUnit.setAmount(1233);
        storageUnit.setName("Changed");
        storageUnitService.update(storageUnit);
    }

    @Test
    public void updateStorageUnitSetParent(){
        StorageUnit parent = storageUnitService.getById(1L);
        StorageUnit storageUnit = storageUnitService.getById(4L);
        storageUnit.setParent(parent);
        storageUnitService.update(storageUnit);
    }

    @Test
    public void deleteStorageUnit(){
        storageUnitService.deleteById(17L);
    }

    @Test
    public void deleteOperationLog(){
        operationLogRepository.delete(operationLogRepository.findAll());
    }

    @After
    public void after(){
        // 查看数据用
        List<StorageUnit> storageUnits = storageUnitService.listAll();
        List<OperationLog> operationLogs = operationLogRepository.findAll();
    }
}