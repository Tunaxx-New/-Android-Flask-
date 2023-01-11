package com.nik.someproject.Fragment.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Catalog.View.ProductsView;
import com.nik.someproject.Fragment.Home.View.HomeView;
import com.nik.someproject.Fragment.MyFragment;
import com.nik.someproject.R;

import java.util.Observer;

public class HomeFragment extends MyFragment {
    boolean isError;
    MyObservable subject;
    public HomeFragment(Activity activity, SavedData savedData, Observer observer) {
        super(activity, savedData);
        subject = new MyObservable();
        subject.addObserver(observer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment, container);
        enter();
        return root;
    }

    @Override
    public void enter() {
        createHomeView(isError);
        isError = false;
    }

    public void createHomeView(boolean forceInitialization) {
        if (!isCreateFragment(forceInitialization))
            return;

        HomeView homeView = new HomeView(activity, savedData);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_home_container, homeView, "home").commit();

        getChildFragmentManager().setFragmentResultListener("No server", homeView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createError(result.getString("title"), result.getString("description"));
                isError = true;
            }
        });

        getChildFragmentManager().setFragmentResultListener("SelectTab", homeView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                //
                // 0: Tab
                // 1: Category/Product
                // 2: id
                //
                int[] args = new int[3];
                args[0] = result.getInt("tab");
                args[1] = result.getInt("choice");
                args[2] = result.getInt("id");
                subject.notifyObservers(args);
            }
        });
    }
}
