package com.nik.someproject.Fragment.Catalog.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.DataManipulation.Product;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.ExtendedClasses.MySlider;
import com.nik.someproject.ForegroundService.DownloadImageFromInternet;
import com.nik.someproject.ExtendedClasses.SliderAdapter;
import com.nik.someproject.Fragment.Catalog.ViewModel.ExtendedProductViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.MainActivity;
import com.nik.someproject.R;
import com.nik.someproject.Scanner.GenerateQRActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Observable;
import java.util.Observer;

public class ExtendedProductView extends MyView implements Observer {
    SliderView slider;
    Bitmap[] bitmaps;

    TextView id;
    TextView name;
    TextView description;
    TextView price;
    TextView discount;

    TextView count;
    Button addCount;
    Button subCount;
    Button buy;
    int product_id;

    ExtendedProductViewModel extendedProductViewModel;
    public ExtendedProductView(Activity activity, SavedData savedData, int product_id) {
        super(R.layout.product_extended, activity, savedData);
        this.product_id = product_id;
        extendedProductViewModel = new ExtendedProductViewModel(product_id, this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        slider = view.findViewById(R.id.eproduct_images);
        id = view.findViewById(R.id.eproduct_id);
        name = view.findViewById(R.id.eproduct_name);
        description = view.findViewById(R.id.eproduct_description);
        price = view.findViewById(R.id.eproduct_price);
        discount = view.findViewById(R.id.eproduct_discount);

        ImageView qr = view.findViewById(R.id.eproduct_qr);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GenerateQRActivity.class);
                intent.putExtra("data", Integer.toString(product_id));
                startActivity(intent);
            }
        });

        buy = view.findViewById(R.id.eproduct_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extendedProductViewModel.buy(savedData.get(R.string.HASH_KEY), Integer.parseInt(count.getText().toString()));
            }
        });

        count = view.findViewById(R.id.eproduct_count);
        extendedProductViewModel.getCount().observe(this.getViewLifecycleOwner(), new androidx.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                count.setText(Integer.toString(integer));
            }
        });

        addCount = view.findViewById(R.id.eproduct_add_count);
        addCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extendedProductViewModel.setCount(Integer.parseInt(count.getText().toString()) + 1);
            }
        });

        subCount = view.findViewById(R.id.eproduct_sub_count);
        subCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extendedProductViewModel.setCount(Integer.parseInt(count.getText().toString()) - 1);
            }
        });

        extendedProductViewModel.get();
    }

    public void animateBuy(int color1, int color2, boolean start) {
        ValueAnimator animator = new ValueAnimator();
        animator = animator.ofInt(getResources().getColor(color1), getResources().getColor(color2));
        animator.setDuration(100L);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                buy.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
            }
        });
        final boolean s = start;
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if (s)
                    animateBuy(color2, color1, false);
            }
        });
        animator.start();
    }

    public void setView(Product product) {
        id.setText("ID: " + Integer.toString(product.id));
        name.setText(product.name);
        description.setText(product.description);
        price.setText(Float.toString(product.price) + " " + product.currency + " / " + product.unit);
        discount.setText("-" + Float.toString(product.discount * 100) + "%");
    }

    private void prepareBitmaps(int id, int count, String image_type) {
        bitmaps = new Bitmap[count];
        for (int i = 0; i < count; i++) {
            int finalI = i;
            Observer observer = new Observer() {
                @Override
                public void update(Observable observable, Object o) {
                    bitmaps[finalI] = (Bitmap)o;
                    if (finalI == count - 1)
                        MySlider.addImagesToSlider(slider, bitmaps);
                }
            };
            com.nik.someproject.ForegroundService.DownloadImageFromInternet d = new com.nik.someproject.ForegroundService.DownloadImageFromInternet(null, observer);
            if (i != 0)
                d.execute(Constants.host + Constants.image_route + "product_" + id + "_" + i + image_type);
            else
                d.execute(Constants.host + Constants.image_route + "product_" + id + "_preview" + image_type);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        ObservObject obj = (ObservObject)o;
        if (obj.type.equals("view")) {
            Product product = (Product) obj.data;
            prepareBitmaps(product.id, product.image_count, product.image_type);
            setView(product);
        }
        else if (obj.type.equals("buy")) {
            boolean result = Boolean.parseBoolean(String.valueOf(obj.data));
            if (result)
                animateBuy(R.color.dark_blue, R.color.green, true);
            else
                animateBuy(R.color.dark_blue, R.color.red, true);
        }
    }
}
