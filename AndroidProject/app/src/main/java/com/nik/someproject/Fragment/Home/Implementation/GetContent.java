package com.nik.someproject.Fragment.Home.Implementation;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Home.Services.IGetContent;
import com.nik.someproject.HttpManager.Implementation.HttpRequest;

import java.util.Map;

public class GetContent implements IGetContent {
    HttpRequest request;
    public GetContent() {
        request = new HttpRequest();
    }

    @Override
    public Map<String, String> getSliderImages() {
        return request.send(Constants.host + "/home/contents/slider", "GET");
    }

    @Override
    public Map<String, String> getStocksImages() {
        return request.send(Constants.host + "/home/contents/stocks", "GET");
    }
}
