package com.nik.someproject.Fragment.Catalog.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.Fragment.Catalog.Implementation.GetCategories;
import com.nik.someproject.Fragment.Catalog.Implementation.GetProducts;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategories;
import com.nik.someproject.Fragment.Catalog.Services.IGetProducts;

import java.util.Map;
import java.util.Observer;

public class ProductsViewModel {
    MyObservable subject;
    IGetProducts getProducts;
    int category_id;
    MutableLiveData<Boolean> isError = new MutableLiveData<>();
    public ProductsViewModel(int category_id, Observer observer) {
        subject = new MyObservable();
        subject.addObserver(observer);
        getProducts = new GetProducts();
        this.category_id = category_id;
    }

    public LiveData<Boolean> getError() { return isError; }

    public void get() {
        if (category_id == -1)
            return;
        Map<String, String> response = getProducts.get(category_id);

        if (response.get("Status").equals("-1")) {
            isError.postValue(true);
            return;
        }

        if (response.containsKey("status") && response.get("status").equals("true") && response.containsKey("ids")) {
            String[] string_ids = response.get("ids").split(",");
            int[] ids = new int[string_ids.length];

            for (int i = 0; i < ids.length; i++)
                ids[i] = Integer.parseInt(string_ids[i]);
            subject.notifyObservers(ids);
        }

    }
}
