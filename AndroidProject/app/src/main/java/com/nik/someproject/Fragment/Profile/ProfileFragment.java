package com.nik.someproject.Fragment.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.MyFragment;
import com.nik.someproject.Fragment.Profile.View.LoginView;
import com.nik.someproject.Fragment.Profile.View.ProfileView;
import com.nik.someproject.Map.MapActivity;
import com.nik.someproject.R;

public class ProfileFragment extends MyFragment {

    public ProfileFragment(Activity activity, SavedData savedData) {
        super(activity, savedData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container);

        enter();

        return root;
    }

    @Override
    public void enter() {
        if (savedData.contains(R.string.HASH_KEY)) {
            createProfileView(false);
        }
        else {
            createLoginView(false);
        }
    }

    @Override
    protected void onErrorExit() {
        enter();
    }

    private void createProfileView(boolean forceInitialization) {
        if (!isAdded())
            return;
        if (!forceInitialization)
            if (getFragmentManager() == null || getChildFragmentManager().getFragments().size() > 0)
                return;
        //
        // Creating and attaching ProfileView
        //

        ProfileView profileView = new ProfileView(activity, savedData);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_profile_container, profileView, "Profile").commit();

        //
        // The resulting No login, when profile view destroys
        // No login - user no logged in during auto hash login
        //
        getChildFragmentManager().setFragmentResultListener("No login", profileView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createLoginView(true);
            }
        });


        //
        // The resulting No server, when profile view destroys
        // No server - when server not responding on get user profile
        //
        getChildFragmentManager().setFragmentResultListener("No server", profileView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createError(result.getString("title"), result.getString("description"));
            }
        });

        //
        // Refresh profile view
        //
        getChildFragmentManager().setFragmentResultListener("Refresh", profileView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createProfileView(true);
            }
        });
    }

    private void createLoginView(boolean forceInitialization) {
        if (!isAdded())
            return;
        if (!forceInitialization)
            if (getFragmentManager() == null || getChildFragmentManager().getFragments().size() > 0)
                return;
        //
        // Creating and attaching login view
        //
        LoginView loginView = new LoginView(activity, savedData);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_profile_container, loginView, "Login").commit();

        //
        // The result listener, when loginView destroyed
        // Logged - user successfully logged in
        //
        getChildFragmentManager().setFragmentResultListener("logged", loginView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createProfileView(true);
            }
        });

        //
        // The result listener, when loginView destroyed
        // No server - server not responding on get user profile
        //
        getChildFragmentManager().setFragmentResultListener("No server", loginView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createProfileView(true);
            }
        });
    }
}
