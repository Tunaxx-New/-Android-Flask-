package com.nik.someproject.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.nik.someproject.DataManipulation.SavedData;

//
// Class for common Fragment Views
//
public class MyView extends Fragment {
    protected Activity activity;
    protected SavedData savedData;

    public MyView(@LayoutRes int contentLayoutId, Activity activity, SavedData savedData) {
        super(contentLayoutId);
        this.activity = activity;
        this.savedData = savedData;
    }

    //
    // Set the result of finished view
    //
    protected void setResult(String result, Bundle bundle) {
        getParentFragmentManager().setFragmentResult(result, bundle);
    }

    //
    // Removing view, destroying by itself
    //
    protected void remove() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }
}
