package com.example.carbooking.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carbooking.R;
import com.example.carbooking.models.CarCategoryObject;
import com.example.carbooking.ui.CarCategoryActivity;
import com.example.carbooking.utils.CustomApplication;

import java.util.ArrayList;
import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingViewHolder> {

    private static final String TAG = ListingAdapter.class.getSimpleName();

    private final Context context;

    private List<CarCategoryObject> carList = new ArrayList<>();


    public ListingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_category_layout, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListingViewHolder holder, int position) {
        CarCategoryObject carCategoryObject = carList.get(position);

        holder.carName.setText(carCategoryObject.getImageName());
        Glide.with(context).load(carCategoryObject.getImagePath()).into(holder.carImage);
        holder.view.setOnClickListener(v -> {
            Intent carCategoryIntent = new Intent(context, CarCategoryActivity.class);
            carCategoryIntent.putExtra("type", holder.carName.getText());
            CustomApplication.type = holder.carName.getText().toString();
            context.startActivity(carCategoryIntent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CarCategoryObject> carList) {
        this.carList = carList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (!carList.isEmpty()) {
            return carList.size();
        } else return 0;
    }
}
