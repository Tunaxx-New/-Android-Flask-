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
import com.nik.someproject.DataManipulation.Category;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.ForegroundService.DownloadImageFromInternet;
import com.nik.someproject.Fragment.Catalog.ViewModel.CategoryViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class CategoryView extends MyView implements Observer {
    TextView name;
    ImageView image;
    MyObservable subject;

    CategoryViewModel categoryViewModel;

    public CategoryView(Activity activity, SavedData savedData, Observer observer, int category_id) {
        super(R.layout.category, activity, savedData);
        subject = new MyObservable();
        subject.addObserver(observer);
        categoryViewModel = new CategoryViewModel(category_id, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        subject.notifyObservers(view);
        name = view.findViewById(R.id.category_name);
        image = view.findViewById(R.id.category_image);
        categoryViewModel.get();
    }

    @Override
    public void update(Observable observable, Object o) {
        Category category = (Category)o;
        name.setText(category.name);
        new DownloadImageFromInternet(image, null).execute(Constants.host + Constants.image_route + category.image_name);
    }

}
