package com.nik.someproject.Fragment.Cart.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Cart.ViewModel.CartViewModel;
import com.nik.someproject.Fragment.Catalog.View.ProductView;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class CartView extends MyView implements Observer {
    GridLayout grid;
    Button deleteAll;
    CartViewModel cartViewModel;
    public CartView(Activity activity, SavedData savedData) {
        super(R.layout.cart, activity, savedData);
        cartViewModel = new CartViewModel(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        grid = view.findViewById(R.id.grid_cart);
        deleteAll = view.findViewById(R.id.cart_delete_all);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartViewModel.deleteAll(savedData.get(R.string.HASH_KEY));
            }
        });
        cartViewModel.get(savedData.get(R.string.HASH_KEY));
    }

    void addProducts(int[] item_ids, int[] product_ids, int[] product_counts) {
        for (int i = 0; i < product_ids.length; i++)
            addProduct(item_ids[i], product_ids[i], product_counts[i]);
    }

    void addProduct(int item_id, int product_id, int count) {
        Observer viewObserver = new Observer() {
            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public void update(Observable observable, Object o) {
                View v = (View)o;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.RIGHT;
                lp.gravity |= Gravity.BOTTOM;
                Button delete = new Button(activity.getApplicationContext());
                delete.setLayoutParams(lp);
                delete.setBackgroundResource(R.drawable.red_button);
                delete.setCompoundDrawablesWithIntrinsicBounds(null, null, null, activity.getDrawable(R.drawable.ic_baseline_clear_24));
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cartViewModel.delete(savedData.get(R.string.HASH_KEY), item_id);
                    }
                });
                ((FrameLayout)v.findViewById(R.id.product_view)).addView(delete);

                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
                grid.addView(v, gridParam);
            }
        };
        ProductView productView = new ProductView(activity, savedData, viewObserver, product_id, count);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(productView, "item_id_cart" + item_id).commit();
    }

    @Override
    public void update(Observable observable, Object o) {
        ObservObject obj = (ObservObject)o;
        if (obj.type.equals("view")) {
            int[][] data = (int[][])obj.data;
            addProducts(data[2], data[0], data[1]);
        }
        else if (obj.type.equals("delete")) {
            if ((boolean)obj.data) {
                setResult("Refresh", null);
                remove();
            }
        }
    }
}
