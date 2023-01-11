package com.nik.someproject.ForegroundService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.ObservObject;

import java.io.InputStream;
import java.util.Observer;

//https://www.tutorialspoint.com/how-do-i-load-an-imageview-by-url-on-android
public class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    MyObservable subject;

    public DownloadImageFromInternet(ImageView imageView, Observer observer) {
        this.imageView = imageView;
        subject = new MyObservable();
        if (observer != null)
            subject.addObserver(observer);
    }

    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (imageView != null)
            imageView.setImageBitmap(result);
        subject.notifyObservers(result);
    }
}
