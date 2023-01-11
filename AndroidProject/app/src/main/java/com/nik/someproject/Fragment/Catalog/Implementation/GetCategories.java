package com.nik.someproject.Fragment.Catalog.Implementation;

import android.util.Log;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategories;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategory;
import com.nik.someproject.HttpManager.Implementation.HttpRequest;

import java.util.Map;

public class GetCategories implements IGetCategories, IGetCategory {
    HttpRequest request;
    public GetCategories() {
        request = new HttpRequest();
    }
    @Override
    public Map<String, String> get() {
        return request.send(Constants.host + "/category/all", "GET");
    }

    @Override
    public Map<String, String> get(int id) {
        return request.send(Constants.host + "/category/" + id, "GET");
    }
}
