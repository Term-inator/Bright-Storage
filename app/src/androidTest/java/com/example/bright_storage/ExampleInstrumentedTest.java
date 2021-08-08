package com.example.bright_storage;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.entity.StorageUnitCategory;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.model.support.Pageable;
import com.example.bright_storage.repository.StorageUnitCategoryRepository;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;

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
    StorageUnitRepository storageUnitRepository;

    @Inject
    CategoryService categoryService;

    @Inject
    StorageUnitCategoryRepository storageUnitCategoryRepository;


    @Before
    public void before(){
        storageUnitService = new StorageUnitServiceImpl();
        storageUnitRepository = new StorageUnitRepository();
        categoryService = new CategoryServiceImpl();
        storageUnitCategoryRepository = new StorageUnitCategoryRepository();
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
        StorageUnit storageUnit = new StorageUnit();
        storageUnit.setName("7778888@@@@@@");
        storageUnit.setAmount(7);
        storageUnit.setNote("777");
        storageUnit.setCategories(new HashSet<>(categoryService.listAll()));

        storageUnitService.create(storageUnit);
        int i = 0;

    }

    @Test
    public void queryRecentActiveStorageUnit(){
        List<StorageUnit> storageUnits = storageUnitService.listRecentActiveStorageUnit();
        List<StorageUnit> storageUnits1 = storageUnitService.listLongestVisitedStorageUnits(null, 3);
        List<StorageUnit> storageUnits2 = storageUnitRepository.listLongestVisitedStorageUnits(null, 3);
        int i = 0;
    }

    @Test
    public void queryStorageUnit(){
        StorageUnitQuery query = new StorageUnitQuery();
//        query.setName("cate");
//        query.setAmount(3);
        Pageable pageable = new Pageable();
        pageable.getOrderByList().clear();
        pageable.getOrderByList().add(new Selector.OrderBy("amount", true));
        List<StorageUnit> res = storageUnitService.query(query, pageable);
        for(StorageUnit storageUnit: res){
            Log.i(TAG, "query storage unit: " + storageUnit);
        }
    }

    @Test
    public void insertSUC(){
        List<StorageUnitCategory> suc = storageUnitCategoryRepository.findAll();
        for (StorageUnitCategory storageUnitCategory : suc) {
            Log.i(TAG, "insertSUC: suc" + storageUnitCategory);
        }

    }
}