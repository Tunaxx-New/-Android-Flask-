package com.nik.someproject.Fragment.Catalog.ViewModel;

import android.util.Log;

import com.nik.someproject.DataManipulation.Category;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.Fragment.Catalog.Implementation.GetCategories;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategory;
import java.util.Map;
import java.util.Observer;

public class CategoryViewModel {
    int category_id;
    MyObservable subject;

    IGetCategory getCategory;

    public CategoryViewModel(int category_id, Observer observer) {
        this.category_id = category_id;
        getCategory = new GetCategories();
        subject = new MyObservable();
        subject.addObserver(observer);
    }

    public void get() {
        Map<String, String> response = getCategory.get(category_id);

        if (response.containsKey("name")) {
            Category category = new Category();
            category.name = response.get("name");
            category.description = response.get("description");
            category.image_name = response.get("image_name");

            subject.notifyObservers(category);
        }
    }
}
