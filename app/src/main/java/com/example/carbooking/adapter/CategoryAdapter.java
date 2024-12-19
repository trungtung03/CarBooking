package com.example.carbooking.adapter;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
        if ("Booked".equalsIgnoreCase(carListObject.getStatus())) {
            holder.view.setEnabled(false);
            holder.imageView.setVisibility(View.GONE);
            holder.carImage.setVisibility(View.VISIBLE);
            holder.statusText.setVisibility(View.VISIBLE);
        } else {
            holder.statusText.setVisibility(View.GONE);
            holder.carImage.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        }
        holder.view.setOnClickListener(v -> {
            holder.view.setEnabled(true);
            Intent carIntent = new Intent(context, ProductActivity.class);
            carIntent.putExtra("price", (int) carListObject.getPrice());
            carIntent.putExtra("name", carListObject.getCarName());
            carIntent.putExtra("image", carListObject.getCarImage());
            carIntent.putExtra("carKey", carListObject.getSubCategory());
            carIntent.putExtra("carType", carListObject.getCategory());
            Log.d("TAG__", "onBindViewHolder: " + carListObject.getSubCategory() + "  " + carListObject.getCategory());
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
