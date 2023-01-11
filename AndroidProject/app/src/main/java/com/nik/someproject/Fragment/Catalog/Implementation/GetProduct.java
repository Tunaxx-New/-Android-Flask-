package com.nik.someproject.Fragment.Catalog.Implementation;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Catalog.Services.IBuyProduct;
import com.nik.someproject.Fragment.Catalog.Services.IGetProduct;
import com.nik.someproject.Fragment.Catalog.Services.IGetProductExtended;
import com.nik.someproject.Fragment.Catalog.Services.IGetProducts;
import com.nik.someproject.HttpManager.Implementation.HttpRequest;

import java.util.Map;

public class GetProduct implements IGetProduct, IGetProductExtended, IBuyProduct {
    HttpRequest request;
    public GetProduct() {
        request = new HttpRequest();
    }
    @Override
    public Map<String, String> get(int id) {
        return request.send(Constants.host + "/products/id/" + id, "GET");
    }

    @Override
    public Map<String, String> getExtended(int id) {
        return request.send(Constants.host + "/products/id/extended/" + id, "GET");
    }

    @Override
    public Map<String, String> buy(int id, int count, String hash) {
        request.prop.put("hash", hash);
        request.prop.put("product_id", String.valueOf(id));
        request.prop.put("count", String.valueOf(count));
        return request.send(Constants.host + "/cart/add", "POST");
    }
}
