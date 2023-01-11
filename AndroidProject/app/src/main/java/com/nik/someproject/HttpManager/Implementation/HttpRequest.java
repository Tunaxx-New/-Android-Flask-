package com.nik.someproject.HttpManager.Implementation;

import com.nik.someproject.HttpManager.Services.IRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public IRequest request;
    public HashMap<String, String> prop;
    public HashMap<String, String> param;
    public HttpRequest() {
        request = new HttpManager();
        prop = new HashMap<String, String>();
        param = new HashMap<String, String>();
        prop.put("Content-Type", "application/x-www-form-urlencoded");
        prop.put("charset", "utf-8");
        prop.put("Accept", "*/*");
        prop.put("Accept-Encoding", "gzip, deflate, br");
        prop.put("Connection", "keep-alive");
    }
    public Map<String, String> send(String url, String method) {
        return request.request(url, prop, param, method);
    }
}
