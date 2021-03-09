package com.example.bright_storage.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bright_storage.R;
import com.example.bright_storage.databinding.ActivityLoginBinding;
import com.example.bright_storage.fragment.LoginPasswordFragment;
import com.example.bright_storage.fragment.LoginPhoneFragment;
import com.example.bright_storage.model.view.LoginParam;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;

    private Fragment[] fragments;

    private int activeFragment;

    public void changeFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layout_fragment, fragments[activeFragment]);
        transaction.commit();
        activeFragment = (activeFragment + 1) & 1; // 01循环
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView(){
        LoginParam loginParam = new LoginParam();
        fragments = new Fragment[]{
                new LoginPhoneFragment(loginParam),
                new LoginPasswordFragment(loginParam)};
        activeFragment = 0;
        changeFragment();
    }
}