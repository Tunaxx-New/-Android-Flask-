package com.nik.someproject.Fragment.Catalog.ViewModel;

import com.nik.someproject.DataManipulation.Category;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.Product;
import com.nik.someproject.Fragment.Catalog.Implementation.GetCategories;
import com.nik.someproject.Fragment.Catalog.Implementation.GetProduct;
import com.nik.someproject.Fragment.Catalog.Services.IGetCategory;
import com.nik.someproject.Fragment.Catalog.Services.IGetProduct;

import java.util.Map;
import java.util.Observer;

public class ProductViewModel {
    int product_id;
    MyObservable subject;

    IGetProduct getProduct;

    public ProductViewModel(int product_id, Observer observer) {
        this.product_id = product_id;
        getProduct = new GetProduct();
        subject = new MyObservable();
        subject.addObserver(observer);
    }

    public void get() {
        Map<String, String> response = getProduct.get(product_id);

        if (response.containsKey("id")) {
            Product product = new Product();
            product.id = Integer.parseInt(response.get("id"));
            product.name = response.get("name");
            product.image_type = response.get("image_type");
            product.currency = response.get("currency");

            if (!response.get("price").equals("null"))
                product.price = Float.parseFloat(response.get("price"));

            if (!response.get("discount").equals("null"))
                product.discount = Float.parseFloat(response.get("discount"));

            subject.notifyObservers(product);
        }
    }
}
