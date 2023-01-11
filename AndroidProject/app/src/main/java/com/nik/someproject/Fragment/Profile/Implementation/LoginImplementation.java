package com.nik.someproject.Fragment.Profile.Implementation;

import android.util.Log;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Profile.Services.ILogin;
import com.nik.someproject.HttpManager.Implementation.HttpManager;
import com.nik.someproject.HttpManager.Services.IRequest;
import com.nik.someproject.R;

import java.util.HashMap;
import java.util.Map;

public class LoginImplementation implements ILogin {
    IRequest request;

    public LoginImplementation() {
        request = new HttpManager();
    }

    @Override
    public Map<String, String> login(String phone, String password) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("phone", phone);
        param.put("password", password);

        HashMap<String, String> prop = new HashMap<String, String>();

        prop.put("Content-Type", "application/x-www-form-urlencoded");
        prop.put("charset", "utf-8");
        prop.put("Accept", "*/*");
        prop.put("Accept-Encoding", "gzip, deflate, br");
        prop.put("Connection", "keep-alive");
        return request.request(Constants.host + Constants.login_route, param, prop, "POST");
    }
}
