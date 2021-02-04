package com.example.bright_storage.model.view;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import com.example.bright_storage.BR;

import lombok.ToString;

@ToString
public class RegisterParam extends BaseObservable {

    private String password;

    private String repeatPassword;

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
        notifyPropertyChanged(BR.repeatPassword);
    }
}
