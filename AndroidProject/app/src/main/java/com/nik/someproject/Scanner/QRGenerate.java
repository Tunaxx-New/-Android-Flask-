package com.nik.someproject.Scanner;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import androidmads.library.qrgenearator.QRGContents;

import androidmads.library.qrgenearator.QRGEncoder;

public class QRGenerate {
    private QRGEncoder qrgEncoder;

    public Bitmap createQr(String QRvalue, Context context) {
        Bitmap bitmap;
        try {
            WindowManager manager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(QRvalue, null, QRGContents.Type.TEXT, smallerDimension);
            qrgEncoder.setColorBlack(Color.BLACK);
            qrgEncoder.setColorWhite(Color.WHITE);
            System.out.println(qrgEncoder.getTitle());
            try {
                bitmap = qrgEncoder.getBitmap();
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
