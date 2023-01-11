package com.nik.someproject.Fragment.Catalog.View;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Catalog.ViewModel.CategoriesViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class CategoriesView extends MyView implements Observer {
    GridLayout grid;
    Activity activity;
    SavedData savedData;

    CategoriesViewModel categoriesViewModel;

    public CategoriesView(Activity activity, SavedData savedData) {
        super(R.layout.category_grid, activity, savedData);
        this.activity = activity;
        this.savedData = savedData;
        categoriesViewModel = new CategoriesViewModel(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        grid = view.findViewById(R.id.grid_category);

        categoriesViewModel.getError().observe(this.getViewLifecycleOwner(), new androidx.lifecycle.Observer<Boolean>() {
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

        categoriesViewModel.get();
    }

    void addCategories(int[] category_ids) {
        for (int i = 0; i < category_ids.length; i++)
            addCategory(category_ids[i]);
    }

    void addCategory(int category_id) {
        Observer viewObserver = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                View v = (View)o;

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("category_id", category_id);
                        setResult("Selected", bundle);
                        remove();
                    }
                });

                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
                grid.addView(v, gridParam);
            }
        };
        CategoryView categoryView = new CategoryView(activity, savedData, viewObserver, category_id);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(categoryView, null).commit();
    }

    @Override
    public void update(Observable observable, Object o) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        LinearLayout.LayoutParams gridParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (size.x / 2) * (((int[])o).length / 2));
        //grid.setLayoutParams(gridParam);
        addCategories((int[])o);
    }
}
