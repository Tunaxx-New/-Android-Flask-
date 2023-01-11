package com.nik.someproject.Fragment.Chat.Implementation;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Chat.Services.IGetTechsupports;
import com.nik.someproject.HttpManager.Implementation.HttpManager;
import com.nik.someproject.HttpManager.Services.IRequest;

import java.util.HashMap;
import java.util.Map;

public class GetTechsupports implements IGetTechsupports {
    IRequest request;
    HashMap<String, String> prop;
    HashMap<String, String> param;

    public GetTechsupports() {
        request = new HttpManager();
        prop = new HashMap<String, String>();
        param = new HashMap<String, String>();
        prop.put("Content-Type", "application/x-www-form-urlencoded");
        prop.put("charset", "utf-8");
        prop.put("Accept", "*/*");
        prop.put("Accept-Encoding", "gzip, deflate, br");
        prop.put("Connection", "keep-alive");
    }

    @Override
    public Map<String, String> get(String uhash) {
        param.put("hash", uhash);
        return request.request(Constants.host + Constants.get_techs_route, param, prop, "POST");
    }
}
