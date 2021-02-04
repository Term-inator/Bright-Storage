package com.example.bright_storage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bright_storage.R;
import com.example.bright_storage.activity.LoginActivity;
import com.example.bright_storage.activity.RegisterActivity;
import com.example.bright_storage.databinding.FragmentLoginPhoneBinding;
import com.example.bright_storage.model.view.LoginParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginPhoneFragment extends Fragment {

    private FragmentLoginPhoneBinding binding;

    private final LoginParam loginParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_phone, container, false);

        binding.setLoginParam(loginParam);
        binding.btnChangeForm.setOnClickListener(v -> ((LoginActivity) requireActivity()).changeFragment());
        binding.btnLoginPhone.setOnClickListener(v -> loginPhone());

        return binding.getRoot();
    }

    private void loginPhone(){
        Log.w("LoginPhone", "loginPhone: " + loginParam);
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

}