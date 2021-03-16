package com.example.bright_storage.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bright_storage.databinding.ActivityRegisterBinding;
import com.example.bright_storage.model.param.RegisterParam;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private RegisterParam registerParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerParam = new RegisterParam();
        binding.setRegisterParam(registerParam);
        binding.btnRegister.setOnClickListener(v -> Log.w("", "onCreate: " + registerParam.getPassword()));

    }
}