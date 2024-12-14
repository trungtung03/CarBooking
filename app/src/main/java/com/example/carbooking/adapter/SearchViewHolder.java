package com.example.carbooking.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;

public class SearchViewHolder extends RecyclerView.ViewHolder{

    public ImageView carImage;

    public TextView carName;
    public TextView carPrice;
    public TextView carFeature;
    public TextView carDuration;

    public View view;


    public SearchViewHolder(View itemView) {
        super(itemView);

        carImage = itemView.findViewById(R.id.car_rental_image);

        carName = itemView.findViewById(R.id.car_rental_name);
        carPrice = itemView.findViewById(R.id.car_rental_price);
        carFeature = itemView.findViewById(R.id.car_rental_options);
        carDuration = itemView.findViewById(R.id.car_rental_duration);

        view = itemView;
    }
}
