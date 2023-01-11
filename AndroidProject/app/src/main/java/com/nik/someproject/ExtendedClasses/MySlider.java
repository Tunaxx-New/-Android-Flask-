package com.nik.someproject.ExtendedClasses;

import android.graphics.Bitmap;

import com.nik.someproject.Constants;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Observable;
import java.util.Observer;

public class MySlider {
    public static void addImagesToSlider(SliderView slider, Bitmap[] bitmaps) {
        SliderAdapter sliderAdapter = new SliderAdapter(bitmaps);
        slider.setSliderAdapter(sliderAdapter);
        slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        slider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        slider.startAutoCycle();
        slider.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                int pos = position;
                int count = slider.getChildCount();
                pos++;
                if (pos < count)
                    slider.setCurrentPagePosition(pos);
                else
                    slider.setCurrentPagePosition(0);
            }
        });
    }
}
