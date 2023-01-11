package com.nik.someproject.Fragment.Catalog.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.DataManipulation.Product;
import com.nik.someproject.Fragment.Catalog.Implementation.GetProduct;
import com.nik.someproject.Fragment.Catalog.Services.IBuyProduct;
import com.nik.someproject.Fragment.Catalog.Services.IGetProductExtended;

import java.util.Map;
import java.util.Observer;

public class ExtendedProductViewModel {
    int product_id;
    MyObservable subject;
    IGetProductExtended getProductExtended;
    IBuyProduct buyProduct;

    MutableLiveData<Integer> count = new MutableLiveData<Integer>();

    public ExtendedProductViewModel(int product_id, Observer observer) {
        this.product_id = product_id;
        subject = new MyObservable();
        subject.addObserver(observer);
        GetProduct gp = new GetProduct();
        getProductExtended = (IGetProductExtended)gp;
        buyProduct = (IBuyProduct)gp;

        count.postValue(1);
    }

    public LiveData<Integer> getCount() { return count; }
    public void setCount(int count) {
        this.count.postValue(count);
        if (count <= 0)
            this.count.postValue(1);
    }

    public void get() {
        Map<String, String> response = getProductExtended.getExtended(product_id);

        if (response.containsKey("description")) {
            Product product = new Product();
            product.id = Integer.parseInt(response.get("id"));
            product.name = response.get("name");
            product.description = response.get("description");
            product.image_type = response.get("image_type");
            product.unit = response.get("unit");
            product.currency = response.get("currency");

            if (!response.get("image_count").equals("null"))
                product.image_count = Integer.parseInt(response.get("image_count"));

            if (!response.get("price").equals("null"))
                product.price = Float.parseFloat(response.get("price"));

            if (!response.get("discount").equals("null"))
                product.discount = Float.parseFloat(response.get("discount"));

            ObservObject o = new ObservObject();
            o.type = "view";
            o.data = product;
            subject.notifyObservers(o);
        }
    }

    public void buy(String uhash, int count) {
        Map<String, String> response = buyProduct.buy(product_id, count, uhash);

        ObservObject o = new ObservObject();
        o.type = "buy";
        if (!response.get("Status").equals("-1") && response.containsKey("status"))
            o.data = response.get("status");
        else
            o.data = "false";
        subject.notifyObservers(o);
    }
}
