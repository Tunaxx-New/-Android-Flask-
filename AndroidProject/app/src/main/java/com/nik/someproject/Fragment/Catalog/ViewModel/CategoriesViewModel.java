package com.nik.someproject.Fragment.Catalog.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.Fragment.Catalog.Implementation.GetCategories;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategories;

import java.util.Map;
import java.util.Observer;

public class CategoriesViewModel {
    MyObservable subject;
    IGetCategories getCategories;
    MutableLiveData<Boolean> isError = new MutableLiveData<>();
    public CategoriesViewModel(Observer observer) {
        subject = new MyObservable();
        subject.addObserver(observer);
        getCategories = new GetCategories();
    }

    public LiveData<Boolean> getError() { return isError; }

    public void get() {
        Map<String, String> response = getCategories.get();

        if (response.get("Status").equals("-1")) {
            isError.postValue(true);
            return;
        }

        if (response.containsKey("ids")) {
            String[] string_ids = response.get("ids").toString().split(",");
            int[] ids = new int[string_ids.length];

            for (int i = 0; i < ids.length; i++)
                ids[i] = Integer.parseInt(string_ids[i]);
            subject.notifyObservers(ids);
        }

    }
}
