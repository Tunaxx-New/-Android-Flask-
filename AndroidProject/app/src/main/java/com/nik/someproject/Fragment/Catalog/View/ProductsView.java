package com.nik.someproject.Fragment.Catalog.View;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Catalog.ViewModel.ProductsViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class ProductsView extends MyView implements Observer {
    GridLayout grid;
    Activity activity;
    SavedData savedData;

    ProductsViewModel productsViewModel;

    public ProductsView(Activity activity, SavedData savedData, int category_id) {
        super(R.layout.product_grid, activity, savedData);
        this.activity = activity;
        this.savedData = savedData;
        productsViewModel = new ProductsViewModel(category_id, this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        grid = view.findViewById(R.id.grid_product);

        productsViewModel.getError().observe(this.getViewLifecycleOwner(), new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                if (!b)
                    return;
                Bundle errorBundle = new Bundle();
                errorBundle.putString("title", "Server error");
                errorBundle.putString("description", "Server is not available in current time");
                setResult("No server", errorBundle);
                remove();
            }
        });

        productsViewModel.get();
    }

    void addProducts(int[] product_ids) {
        for (int i = 0; i < product_ids.length; i++)
            addProduct(product_ids[i]);
    }

    void addProduct(int product_id) {
        Observer viewObserver = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                View v = (View)o;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("product_id", product_id);
                        setResult("Selected", bundle);
                        remove();
                    }
                });

                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
                gridParam.height = GridLayout.LayoutParams.WRAP_CONTENT;
                gridParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
                grid.addView(v, gridParam);
            }
        };
        ProductView productView = new ProductView(activity, savedData, viewObserver, product_id, -1);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(productView, null).commit();
    }

    @Override
    public void update(Observable observable, Object o) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        LinearLayout.LayoutParams gridParam;
        int ids_len = ((int[])o).length;
        if (ids_len >= 2)
            gridParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (size.x / 2) * ids_len / 2);
        else
            gridParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //grid.setLayoutParams(gridParam);
        addProducts(((int[])o));
    }
}
