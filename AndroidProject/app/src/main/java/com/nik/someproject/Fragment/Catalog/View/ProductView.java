package com.nik.someproject.Fragment.Catalog.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.Product;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.ForegroundService.DownloadImageFromInternet;
import com.nik.someproject.Fragment.Catalog.ViewModel.ProductViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class ProductView extends MyView implements Observer {
    TextView name;
    TextView count;
    ImageView image;
    MyObservable subject;

    ProductViewModel productViewModel;
    int countInt;

    public ProductView(Activity activity, SavedData savedData, Observer observer, int product_id, int countInt) {
        super(R.layout.product, activity, savedData);
        subject = new MyObservable();
        subject.addObserver(observer);
        productViewModel = new ProductViewModel(product_id, this);
        this.countInt = countInt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        subject.notifyObservers(view);
        name = view.findViewById(R.id.product_name);
        count = view.findViewById(R.id.product_count);
        image = view.findViewById(R.id.product_image);
        productViewModel.get();
    }

    @Override
    public void update(Observable observable, Object o) {
        Product product = (Product)o;
        name.setText(product.name);
        if (countInt != -1) {
            count.setText(Integer.toString(countInt) + " " + product.unit);
            count.setVisibility(View.VISIBLE);
        }
        new DownloadImageFromInternet(image, null).execute(Constants.host + Constants.image_route + "product_" + product.id + "_preview" + product.image_type);
    }

}
