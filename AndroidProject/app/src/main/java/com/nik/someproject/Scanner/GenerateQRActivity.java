package com.nik.someproject.Scanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nik.someproject.R;

public class GenerateQRActivity extends AppCompatActivity {

    ImageView code;
    Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_generate);


        QRGenerate qrGenerator = new QRGenerate();
        Bitmap image = qrGenerator.createQr(getIntent().getStringExtra("data"), getApplicationContext());
        code = findViewById(R.id.scanner_generate_code_image);
        code.setImageBitmap(image);

        back = findViewById(R.id.scanner_generate_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
