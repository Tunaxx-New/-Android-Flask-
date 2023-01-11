package com.nik.someproject.Fragment.Cart.Implementation;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Cart.Service.IDeleteCartProduct;
import com.nik.someproject.Fragment.Cart.Service.IGetCartProducts;
import com.nik.someproject.HttpManager.Implementation.HttpRequest;

import java.util.Map;

public class GetCart implements IGetCartProducts, IDeleteCartProduct {
    HttpRequest request;
    public GetCart() {
        request = new HttpRequest();
    }
    @Override
    public Map<String, String> get(String hash) {
        request.prop.put("hash", String.valueOf(hash));
        return request.send(Constants.host + "/cart/items", "POST");
    }

    @Override
    public Map<String, String> delete(String hash, int id) {
        request.prop.put("hash", String.valueOf(hash));
        request.prop.put("item_id", String.valueOf(id));
        return request.send(Constants.host + "/cart/delete", "POST");
    }

    @Override
    public Map<String, String> deleteAll(String hash) {
        request.prop.put("hash", String.valueOf(hash));
        return request.send(Constants.host + "/cart/delete_all", "POST");
    }
}
