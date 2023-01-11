package com.nik.someproject.Fragment.Catalog.Implementation;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategories;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategory;
import com.nik.someproject.Fragment.Catalog.Services.IGetProduct;
import com.nik.someproject.Fragment.Catalog.Services.IGetProducts;
import com.nik.someproject.HttpManager.Implementation.HttpRequest;

import java.util.Map;

public class GetProducts implements IGetProducts {
    HttpRequest request;
    public GetProducts() {
        request = new HttpRequest();
    }
    @Override
    public Map<String, String> get(int category_id) {
        return request.send(Constants.host + "/products/category/" + category_id, "GET");
    }
}
