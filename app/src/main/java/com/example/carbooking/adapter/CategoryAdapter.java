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
import com.example.carbooking.models.CarListObject;
import com.example.carbooking.ui.ProductActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private static final String TAG = CategoryAdapter.class.getSimpleName();

    private final Context context;
    private List<CarListObject> carList = new ArrayList<>();

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_car_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        CarListObject carListObject = carList.get(position);
        holder.carType.setText(carListObject.getCarType());
        holder.carName.setText(carListObject.getCarName());
        holder.price.setText("$" + (int) carListObject.getPrice());
        Glide.with(context).load(carListObject.getCarImage()).into(holder.imageView);

        holder.view.setOnClickListener(v -> {
            Intent carIntent = new Intent(context, ProductActivity.class);
            carIntent.putExtra("price", (int) carListObject.getPrice());
            carIntent.putExtra("name", carListObject.getCarName());
            carIntent.putExtra("image", carListObject.getCarImage());
            context.startActivity(carIntent);
        });
    }

    public void setData(List<CarListObject> carList) {
        this.carList = carList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }
}
