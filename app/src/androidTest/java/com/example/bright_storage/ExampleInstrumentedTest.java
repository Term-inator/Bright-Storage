package com.example.bright_storage;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.entity.StorageUnitCategory;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.repository.StorageUnitCategoryRepository;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Before
    public void before(){
        storageUnitService = new StorageUnitServiceImpl();
        categoryService = new CategoryServiceImpl();
        storageUnitCategoryRepository = new StorageUnitCategoryRepository();
    }

    @Test
    public void insertCategory(){
        Category c1 = new Category(null, null, "nihaoC");
        Category c2 = new Category(null, null, "cate2");
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
        storageUnit.setName("su with categories");
        storageUnit.setAmount(4);
        storageUnit.setNote("u with categories");
        storageUnit.setCategories(new HashSet<>(categoryService.listAll()));

        storageUnitService.create(storageUnit);

    }

    @Test
    public void queryStorageUnit(){
        StorageUnitQuery query = new StorageUnitQuery();
        query.setName("cate");
        List<StorageUnit> res = storageUnitService.query(query);
//        List<StorageUnit> res = storageUnitService.listAll();
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