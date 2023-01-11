package com.nik.someproject.Fragment.Catalog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Catalog.View.CategoriesView;
import com.nik.someproject.Fragment.Catalog.View.ExtendedProductView;
import com.nik.someproject.Fragment.Catalog.View.ProductsView;
import com.nik.someproject.Fragment.Chat.View.ChatSelectView;
import com.nik.someproject.Fragment.MyFragment;
import com.nik.someproject.R;

public class CatalogFragment extends MyFragment {
    boolean isError;
    Button back;
    String currentFragment;
    int currentCategory;

    int choice = -1;
    int id = -1;

    public CatalogFragment(Activity activity, SavedData savedData) {
        super(activity, savedData);
        this.isError = false;
        currentFragment = "none";
        currentCategory = -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.catalog_fragment, container);
        back = root.findViewById(R.id.catalog_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCurrentFragment();
            }
        });
        enter();

        return root;
    }

    public void deleteCurrentFragment() {
        if (currentFragment.equals("none") || currentFragment.equals("category_select"))
            return;

        Fragment fragment = getChildFragmentManager().findFragmentByTag(currentFragment);
        if (fragment == null)
            return;

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.remove(fragment).commit();

        if (currentFragment.equals("product_extended")) {
            if (currentCategory != -1)
                createProductsView(true, currentCategory);
            else
                createCategoiresView(true);
        }
        else if (currentFragment.equals("product_select")) {
            createCategoiresView(true);
        }
    }

    @Override
    public void enter() {
        if (choice != -1 && id != -1) {
            select(choice, id);
            return;
        }
        createCategoiresView(isError);
        isError = false;
    }

    @Override
    protected void onErrorExit() {
        enter();
    }

    @Override
    public void select(int choice, int id) {
        this.choice = choice;
        this.id = id;
        if (choice == 1) {
            createProductsView(true, id);
        }
        else if (choice == 2) {
            createExtendedProductView(true, id);
        }
    }

    public void createCategoiresView(boolean forceInitialization) {
        if (!isAdded())
            return;
        if (!forceInitialization)
            if (getFragmentManager() == null || getChildFragmentManager().getFragments().size() > 0)
                return;

        CategoriesView categoriesView = new CategoriesView(activity, savedData);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_catalog_container, categoriesView, "category_select").commit();
        currentFragment = "category_select";

        getChildFragmentManager().setFragmentResultListener("No server", categoriesView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createError(result.getString("title"), result.getString("description"));
                isError = true;
            }
        });

        getChildFragmentManager().setFragmentResultListener("Selected", categoriesView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createProductsView(true, result.getInt("category_id"));
            }
        });

        this.choice = -1;
        this.id = -1;
    }

    public void createProductsView(boolean forceInitialization, int category_id) {
        if (!isCreateFragment(forceInitialization))
            return;
        ProductsView productsView = new ProductsView(activity, savedData, category_id);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_catalog_container, productsView, "product_select").commit();
        currentFragment = "product_select";
        currentCategory = category_id;

        getChildFragmentManager().setFragmentResultListener("No server", productsView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createError(result.getString("title"), result.getString("description"));
                isError = true;
            }
        });

        getChildFragmentManager().setFragmentResultListener("Selected", productsView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createExtendedProductView(true, result.getInt("product_id"));
            }
        });

        this.choice = -1;
        this.id = -1;
    }

    public void createExtendedProductView(boolean forceInitialization, int product_id) {
        if (!isCreateFragment(forceInitialization))
            return;

        ExtendedProductView extendedProductView = new ExtendedProductView(activity, savedData, product_id);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_product_container, extendedProductView, "product_extended").commit();

        currentFragment = "product_extended";

        this.choice = -1;
        this.id = -1;
    }
}
