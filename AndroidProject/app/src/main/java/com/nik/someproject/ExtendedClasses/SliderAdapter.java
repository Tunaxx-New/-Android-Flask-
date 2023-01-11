package com.nik.someproject.ExtendedClasses;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nik.someproject.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    Bitmap[] bitmaps;

    public SliderAdapter(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.image.setImageBitmap(bitmaps[position]);
    }

    @Override
    public int getCount() {
        return bitmaps.length;
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        ImageView image;

        public Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.slider_image);
        }
    }
}
