package com.example.bright_storage.model.param;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.bright_storage.BR;

import lombok.ToString;

@ToString
public class RegisterParam extends BaseObservable {

    private String phone;

    private String code;

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

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.code);
    }

    @Bindable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        notifyPropertyChanged(BR.code);
    }
}
