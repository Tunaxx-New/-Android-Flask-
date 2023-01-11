package com.nik.someproject.Fragment.Profile.Implementation;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.User;
import com.nik.someproject.Fragment.Profile.Services.IUser;
import com.nik.someproject.HttpManager.Implementation.HttpManager;
import com.nik.someproject.HttpManager.Services.IRequest;

import java.util.HashMap;
import java.util.Map;

public class UserImplementation implements IUser {
    IRequest request;
    HashMap<String, String> param;
    HashMap<String, String> prop;

    public UserImplementation() {
        request = new HttpManager();
        param = new HashMap<String, String>();
        prop = new HashMap<String, String>();
        prop.put("Content-Type", "application/x-www-form-urlencoded");
        prop.put("charset", "utf-8");
        prop.put("Accept", "*/*");
        prop.put("Accept-Encoding", "gzip, deflate, br");
        prop.put("Connection", "keep-alive");
    }

    @Override
    public Map<String, String> get(String hash) {
        param.put("hash", hash);

        return request.request(Constants.host + Constants.get_user_route, param, prop, "POST");
    }

    @Override
    public Map<String, String> set(String hash, User user) {
        param.put("hash", hash);
        param.put("new_name", user.name);
        param.put("new_surname", user.surname);
        param.put("new_email", user.email);
        param.put("new_phone", user.phone);
        return request.request(Constants.host + Constants.change_user_route, param, prop, "POST");
    }
}
