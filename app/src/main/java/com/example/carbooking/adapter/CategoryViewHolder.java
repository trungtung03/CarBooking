package com.example.carbooking.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public TextView carType;
    public TextView carName;
    public RatingBar ratingBar;
    public TextView price;
    public ImageView imageView;
    public View view;
    TextView carImage;
    TextView statusText;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        carType = itemView.findViewById(R.id.car_type);
        carName = itemView.findViewById(R.id.car_name);
        ratingBar = itemView.findViewById(R.id.rating_bar);
        price = itemView.findViewById(R.id.price_per_day);
        imageView = itemView.findViewById(R.id.car_category_image);
        statusText = itemView.findViewById(R.id.status_text);
        carImage = itemView.findViewById(R.id.car_category_);
        view = itemView;
    }
}
