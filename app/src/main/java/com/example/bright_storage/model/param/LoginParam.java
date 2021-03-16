package com.example.bright_storage.model.param;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import com.example.bright_storage.BR;

import lombok.ToString;

@ToString
public class LoginParam extends BaseObservable {

    private String phone;

    private String password;

    private String code;

    @Bindable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        notifyPropertyChanged(BR.code);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
