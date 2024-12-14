package com.example.carbooking.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;

public class ListingViewHolder extends RecyclerView.ViewHolder {

    public ImageView carImage;
    public TextView carName;
    public View view;


    public ListingViewHolder(View itemView) {
        super(itemView);

        carImage = (ImageView)itemView.findViewById(R.id.car_category_image);
        carName = (TextView)itemView.findViewById(R.id.car_category_name);
        view = itemView;
    }
}