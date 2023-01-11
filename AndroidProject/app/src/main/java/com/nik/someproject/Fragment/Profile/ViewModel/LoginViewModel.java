package com.nik.someproject.Fragment.Profile.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nik.someproject.Fragment.Profile.Implementation.LoginImplementation;
import com.nik.someproject.Fragment.Profile.Services.ILogin;

import java.util.Map;

public class LoginViewModel extends ViewModel {
    MutableLiveData<String> mPhone = new MutableLiveData<>();
    MutableLiveData<String> mPassword = new MutableLiveData<>();
    MutableLiveData<String> mLoginResult = new MutableLiveData<>();
    MutableLiveData<String> mError = new MutableLiveData<>();

    ILogin loginImplementation;

    public LoginViewModel() {
        mPhone.postValue("");
        mPassword.postValue("");
        loginImplementation = new LoginImplementation();
    }

    public LiveData<String> getPhone() { return mPhone; }

    public LiveData<String> getPassword() { return mPassword; }

    public LiveData<String> getLoginResult() { return mLoginResult; }

    public LiveData<String> getError() { return mError; }

    public void login(String phone, String password) {
        Map<String, String> response = loginImplementation.login(phone, password);
        String a = "";
        for (Map.Entry<String, String> i : response.entrySet()) {
            a += i.getKey() + ' ' + i.getValue() + ' ';
        }
        mError.postValue(a);
        Log.i("LOL", a);
        mLoginResult.setValue(response.get("hash"));
    }
}
