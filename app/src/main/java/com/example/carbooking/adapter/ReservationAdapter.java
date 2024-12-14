package com.example.carbooking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carbooking.R;
import com.example.carbooking.models.CarCategoryObject;
import com.example.carbooking.models.ReservationObject;
import com.example.carbooking.ui.FullOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationViewHolder> {

    private static final String TAG = ReservationViewHolder.class.getSimpleName();

    private Context context;
    private List<ReservationObject> reservedList = new ArrayList<>();

    public ReservationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_layout, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {
        ReservationObject rObject = reservedList.get(position);

        holder.carName.setText(rObject.getCarName());
        holder.location.setText(rObject.getUserBooking());
        holder.date.setText(rObject.getPickUpDate());
        holder.time.setText(rObject.getPickUpTime());
        holder.price.setText(rObject.getPrice());
        Glide.with(context).load(rObject.getImagePath()).into(holder.pathImage);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ReservationObject> carList) {
        this.reservedList = carList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return reservedList.size();
    }
}