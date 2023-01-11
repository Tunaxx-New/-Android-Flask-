package com.nik.someproject.Scanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Catalog.View.ExtendedProductView;
import com.nik.someproject.R;

public class ProductFoundActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_product);

        SavedData savedData = new SavedData(this);
        savedData.save(R.string.RELOGIN_CHAT_KEY, "logged");
        int id = Integer.parseInt(getIntent().getStringExtra("id"));
        
        ExtendedProductView extendedProductView = new ExtendedProductView(this, savedData, id);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_scanner_product, extendedProductView, "scanner_product_ext").commit();

        Button back = findViewById(R.id.scanner_product_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
