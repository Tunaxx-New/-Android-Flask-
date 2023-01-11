package com.nik.someproject.Scanner;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.R;

import java.util.Observer;
import java.util.concurrent.ExecutionException;

public class MyCamera {
    private String qrCode;
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    MyObservable endSubject;

    MyCamera(Observer observer) {
        endSubject = new MyObservable();
        endSubject.addObserver(observer);
    }

    public void start(Context context, Activity act, View root) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        previewView = root.findViewById(R.id.activity_main_previewView);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider, context, act);
            } catch (InterruptedException | ExecutionException e) {
                Toast.makeText(context, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider, Context context, Activity activity) {
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCode = _qrCode;
                endSubject.notifyObservers(qrCode);
            }

            @Override
            public void qrCodeNotFound() {

            }
        }));

        androidx.camera.core.Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)activity, cameraSelector, imageAnalysis, preview);
    }
}
