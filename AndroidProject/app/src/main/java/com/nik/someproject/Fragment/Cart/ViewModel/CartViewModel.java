package com.nik.someproject.Fragment.Cart.ViewModel;

import android.util.Log;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.Fragment.Cart.Implementation.GetCart;
import com.nik.someproject.Fragment.Cart.Service.IDeleteCartProduct;
import com.nik.someproject.Fragment.Cart.Service.IGetCartProducts;

import java.util.Map;
import java.util.Observer;

public class CartViewModel {
    MyObservable subject;
    IGetCartProducts getCartProducts;
    IDeleteCartProduct deleteCartProduct;
    public CartViewModel(Observer observer) {
        subject = new MyObservable();
        GetCart g = new GetCart();
        getCartProducts = g;
        deleteCartProduct = g;
        subject.addObserver(observer);
    }

    public void delete(String hash, int item_id) {
        Map<String, String> response = deleteCartProduct.delete(hash, item_id);
        ObservObject o = new ObservObject();
        o.type = "delete";
        if (response.containsKey("status") && response.get("status").equals("true"))
            o.data = true;
        else
            o.data = false;
        subject.notifyObservers(o);
    }

    public void deleteAll(String hash) {
        Map<String, String> response = deleteCartProduct.deleteAll(hash);
        ObservObject o = new ObservObject();
        o.type = "delete";
        if (response.containsKey("status") && response.get("status").equals("true"))
            o.data = true;
        else
            o.data = false;
        subject.notifyObservers(o);
    }

    public void get(String hash) {
        Map<String, String> response = getCartProducts.get(hash);

        if (response.containsKey("status") && response.get("status").equals("true") && response.containsKey("product_ids")) {
            String[] ids = response.get("product_ids").split(",");
            String[] counts = response.get("counts").split(",");
            String[] item_ids = response.get("ids").split(",");

            int[][] data = new int[3][ids.length];
            boolean accept = false;
            for (int i = 0; i < ids.length; i++) {
                if (ids[i].equals(""))
                    continue;
                accept = true;
                data[0][i] = Integer.parseInt(ids[i]);
                data[1][i] = Integer.parseInt(counts[i]);
                data[2][i] = Integer.parseInt(item_ids[i]);
            }
            if (!accept)
                return;

            ObservObject o = new ObservObject();
            o.type = "view";
            o.data = data;
            subject.notifyObservers(o);
        }
    }
}
