package com.nik.someproject.Fragment;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Cart.CartFragment;
import com.nik.someproject.Fragment.Catalog.CatalogFragment;
import com.nik.someproject.Fragment.Chat.ChatFragment;
import com.nik.someproject.Fragment.Home.HomeFragment;
import com.nik.someproject.Fragment.Profile.ProfileFragment;

import java.util.ArrayList;
import java.util.Observer;

//
// Main layout adapter Managing the Fragments
//
public class FragmentAdapter extends FragmentStateAdapter {
    int count;
    Activity activity;
    SavedData savedData;

    ArrayList<MyFragment> myFragments;
    ArrayList<Fragment> fragments;
    final HomeFragment f1;
    final CatalogFragment f2;
    final CartFragment f3;
    final ChatFragment f4;
    final ProfileFragment f5;


    public FragmentAdapter(FragmentActivity fa, int count, Activity activity, SavedData savedData, Observer observer) {
        super(fa);
        this.count = count;
        this.activity = activity;
        this.savedData = savedData;

        this.myFragments = new ArrayList<MyFragment>();
        this.fragments = new ArrayList<Fragment>();
        f1 = new HomeFragment(activity, savedData, observer);
        f2 = new CatalogFragment(activity, savedData);
        f3 = new CartFragment(activity, savedData);
        f4 = new ChatFragment(activity, savedData);
        f5 = new ProfileFragment(activity, savedData);
        this.myFragments.add(f1);
        this.myFragments.add(f2);
        this.myFragments.add(f3);
        this.myFragments.add(f4);
        this.myFragments.add(f5);
        this.fragments.add(f1);
        this.fragments.add(f2);
        this.fragments.add(f3);
        this.fragments.add(f4);
        this.fragments.add(f5);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position >= 0 && position < fragments.size())
            return fragments.get(position);
        return new Fragment();
    }



    public void clear(int position) {
        myFragments.get(position).removeError();
    }

    public void selectInCatalog(int choice, int id, int tab) {
        myFragments.get(tab).select(choice, id);
    }

    public void enter(int position) {
        myFragments.get(position).enter();
    }

    @Override
    public int getItemCount() {
        return count;
    }
}
