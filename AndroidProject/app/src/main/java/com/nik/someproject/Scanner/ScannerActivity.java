package com.nik.someproject.Scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class ScannerActivity extends AppCompatActivity implements Observer {
    private MyCamera camera;
    private View rooted;
    private boolean found = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        camera = new MyCamera(this);
        rooted = findViewById(android.R.id.content).getRootView();
        requestCamera();
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            camera.start(getBaseContext(), this, rooted);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camera.start(getBaseContext(), this, rooted);
            } else {
                Toast.makeText(getApplicationContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (found) {
            finish();
            return;
        }

        found = true;

        Intent intent = new Intent(ScannerActivity.this, ProductFoundActivity.class);
        intent.putExtra("id", (String)o);
        startActivity(intent);
        finish();
    }
}
