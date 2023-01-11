package com.nik.someproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.FragmentAdapter;
import com.nik.someproject.Scanner.ScannerActivity;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private SavedData savedData;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;

    ImageView qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent ipIntent = new Intent(MainActivity.this, IPActivity.class);
        startActivityForResult(ipIntent, 1);
    }

    public void main() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.INTERNET }, 0);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.FOREGROUND_SERVICE }, 0);
        }

        //Log.i("LOL", getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE).getString(String.valueOf(R.string.HASH_KEY), null));
        //SavedData sd = new SavedData(this);
        //Log.i("LOL", sd.get(String.valueOf(R.string.HASH_KEY)));

        savedData = new SavedData(this);
        savedData.save(R.string.RELOGIN_CHAT_KEY, "logged");

        tabLayout = (TabLayout)findViewById(R.id.TabLayout);
        viewPager = (ViewPager2)findViewById(R.id.ViewPager);

        fragmentAdapter = new FragmentAdapter(this, 5, this, savedData, this);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
                fragmentAdapter.enter(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                fragmentAdapter.clear(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        qr = findViewById(R.id.qr_button);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            main();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        int[] data = (int[])o;
        viewPager.setCurrentItem(data[0], true);
        tabLayout.selectTab(tabLayout.getTabAt(data[0]));
        fragmentAdapter.selectInCatalog(data[1], data[2], data[0]);
    }
}