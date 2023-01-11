package com.nik.someproject.HttpManager.Services;

import org.json.JSONObject;

import java.util.Map;

public interface IRequest {
    Map<String, String> request(String url, Map<String, String> params, Map<String, String> properties, String method);
}
