package com.nik.someproject.Fragment.Cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Cart.View.CartView;
import com.nik.someproject.Fragment.Catalog.View.ProductsView;
import com.nik.someproject.Fragment.MyFragment;
import com.nik.someproject.R;

public class CartFragment extends MyFragment {
    boolean isError;
    public CartFragment(Activity activity, SavedData savedData) {
        super(activity, savedData);
        this.isError = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.cart_fragment, container);
        enter();
        return root;
    }

    @Override
    public void enter() {
        createCartView(true);
        isError = false;
    }

    public void createCartView(boolean forceInitialization) {
        if (!isCreateFragment(forceInitialization))
            return;

        CartView productsView = new CartView(activity, savedData);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_cart_container, productsView, "cart").commit();

        getChildFragmentManager().setFragmentResultListener("Refresh", productsView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                enter();
            }
        });
    }
}
