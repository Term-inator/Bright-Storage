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
import com.example.bright_storage.databinding.FragmentLoginPasswordBinding;
import com.example.bright_storage.model.view.LoginParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginPasswordFragment extends Fragment {

    private static final String TAG = "LoginPasswordFragment";

    private FragmentLoginPasswordBinding binding;

    private final LoginParam loginParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_password, container, false);

        binding.setLoginParam(loginParam);
        binding.btnChangeForm.setOnClickListener(v -> ((LoginActivity) requireActivity()).changeFragment());
        binding.btnLoginPassword.setOnClickListener(v -> loginPassword());

        return binding.getRoot();
    }

    private void loginPassword(){
        Log.w(TAG, "loginPassword: " + loginParam);
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }
}