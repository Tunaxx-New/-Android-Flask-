package com.nik.someproject.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.R;

//
// This class created to common all custom Fragments
// with error creating
//
public class MyFragment extends Fragment {
    protected Activity activity;
    protected SavedData savedData;

    ErrorView errorView;

    public MyFragment(Activity activity, SavedData savedData) {
        this.activity = activity;
        this.savedData = savedData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle b = new Bundle();
        super.onCreate(b);
    }

    public void select(int choice, int id) {}

    //
    // Method creating error view, and listening on error exit button
    // @param1: String title - title of error
    // @param2: String description - description of error
    protected void createError(String title, String description) {
        errorView = new ErrorView(title, description);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_error_container, errorView, "Error").commit();
        getFragmentManager().setFragmentResultListener("exit", errorView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                onErrorExit();
            }
        });
    }

    protected void createLocalError(String title, String description, int container_id) {
        errorView = new ErrorView(title, description);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(container_id, errorView, "Error").commit();
        getFragmentManager().setFragmentResultListener("exit", errorView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                onErrorExit();
            }
        });
    }

    public void enter() {}

    public void removeError() {
        if (!isCreateFragment(false))
            return;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (errorView != null)
            ft.remove(errorView).commit();
    }

    //
    // Method to override, used in createError result listener
    //
    protected void onErrorExit() {}

    //
    // Method to check if fragment container filled and do need to create new
    //
    protected boolean isCreateFragment(boolean forceInitialization) {
        if (!isAdded())
            return false;
        if (!forceInitialization)
            return getFragmentManager() != null && getChildFragmentManager().getFragments().size() <= 0;
        return true;
    }
}
