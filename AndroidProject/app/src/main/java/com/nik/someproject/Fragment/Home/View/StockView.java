package com.nik.someproject.Fragment.Home.View;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Catalog.ViewModel.ProductViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class StockView extends MyView {
    ImageView image;
    String image_name;
    MyObservable subject;
    public StockView(Activity activity, SavedData savedData, Observer observer, String image_name) {
        super(R.layout.stock, activity, savedData);
        subject = new MyObservable();
        subject.addObserver(observer);
        this.image_name = image_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        subject.notifyObservers(view);
        image = view.findViewById(R.id.stock_image);
        FrameLayout l = view.findViewById(R.id.stock_view);
        new com.nik.someproject.ForegroundService.DownloadImageFromInternet(image, null).execute(Constants.host + Constants.image_route + image_name);
    }
}
