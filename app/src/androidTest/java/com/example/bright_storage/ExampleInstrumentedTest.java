package com.example.bright_storage;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";

    @Inject
    StorageUnitService storageUnitService;

    @Before
    public void before(){
        storageUnitService = new StorageUnitServiceImpl();
    }

    public void insert(){
        for(int i=0;i<5;i++){
            StorageUnit storageUnit = new StorageUnit();
            storageUnit.setName("leftlike: " + i);
            storageUnit.setAmount(i + 4);
            storageUnit.setNote("rightlike: " + i);
            storageUnitService.create(storageUnit);
        }
    }

    public List<StorageUnit> query(){
        StorageUnitQuery query = new StorageUnitQuery();
        query.setName("hi");
        return storageUnitService.query(query);
    }

    @Test
    public void useAppContext() {
        List<StorageUnit> storageUnitList = query();
        for(StorageUnit storageUnit: storageUnitList){
            Log.i(TAG, "useAppContext: " + storageUnit);
        }
        Log.i(TAG, "useAppContext: " + storageUnitService.count());
    }
}