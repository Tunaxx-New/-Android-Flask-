package com.nik.someproject.Fragment.Home.View;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.ExtendedClasses.MySlider;
import com.nik.someproject.Fragment.Catalog.View.CategoryView;
import com.nik.someproject.Fragment.Catalog.View.ProductView;
import com.nik.someproject.Fragment.Home.ViewModel.HomeViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.Observable;
import java.util.Observer;

public class HomeView extends MyView implements Observer {
    GridLayout grid;
    SliderView slider;
    Bitmap[] bitmaps;

    HomeViewModel homeViewModel;
    public HomeView(Activity activity, SavedData savedData) {
        super(R.layout.home, activity, savedData);
        homeViewModel = new HomeViewModel(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        slider = view.findViewById(R.id.home_images);
        grid = view.findViewById(R.id.home_grid);
        homeViewModel.getGridContent();
        homeViewModel.getSliderContent();
    }

    private void prepareBitmaps(String[] image_names) {
        bitmaps = new Bitmap[image_names.length];
        for (int i = 0; i < image_names.length; i++) {
            int finalI = i;
            Observer observer = new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    bitmaps[finalI] = (Bitmap)o;
                    if (finalI == image_names.length - 1)
                        MySlider.addImagesToSlider(slider, bitmaps);
                }
            };
            com.nik.someproject.ForegroundService.DownloadImageFromInternet d = new com.nik.someproject.ForegroundService.DownloadImageFromInternet(null, observer);
            d.execute(Constants.host + Constants.image_route + image_names[i]);
        }
    }

    void addStocks(String[] image_names) {
        for (int i = 0; i < image_names.length; i++)
            addStock(image_names[i]);
    }

    void addStock(String image_name) {
        if (image_name.contains("product")) {
            int id = Integer.parseInt(image_name.split("_")[1]);
            addStockProduct(id);
        }
        else if (image_name.contains("category")) {
            int id = Integer.parseInt(image_name.split("_")[1]);
            addStockCategory(id);
        }
        else {
            addStockCommon(image_name);
        }
    }

    void addStockProduct(int id) {
        final int finalId = id;
        Observer observer = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                View v = (View)o;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int choice = 2;
                        Bundle b = new Bundle();
                        b.putInt("tab", 1);
                        b.putInt("choice", choice);
                        b.putInt("id", finalId);
                        setResult("SelectTab", b);
                    }
                });
                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
                v.setLayoutParams(gridParam);
                grid.addView(v, gridParam);
            }
        };
        ProductView productView = new ProductView(activity, savedData, observer, id, -1);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(productView, null).commit();
    }

    void addStockCategory(int id) {
        final int finalId = id;
        Observer observer = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                View v = (View)o;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int choice = 1;
                        Bundle b = new Bundle();
                        b.putInt("tab", 1);
                        b.putInt("choice", choice);
                        b.putInt("id", finalId);
                        setResult("SelectTab", b);
                    }
                });
                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
                v.setLayoutParams(gridParam);
                grid.addView(v, gridParam);
            }
        };
        CategoryView categoryView = new CategoryView(activity, savedData, observer, id);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(categoryView, null).commit();
    }

    void addStockCommon(String image_name) {
        Observer observer = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                View v = (View)o;
                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
                v.setLayoutParams(gridParam);
                grid.addView(v, gridParam);
            }
        };
        StockView stockView = new StockView(activity, savedData, observer, image_name);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(stockView, null).commit();
    }

    @Override
    public void update(Observable observable, Object o) {
        ObservObject obj = (ObservObject)o;
        if (obj.type.equals("slider")) {
            prepareBitmaps((String[])obj.data);
        }
        else if (obj.type.equals("grid")) {
            addStocks((String[])obj.data);
        }
    }
}
