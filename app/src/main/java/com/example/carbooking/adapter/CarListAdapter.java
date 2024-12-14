package com.example.carbooking.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carbooking.models.CarListObject;
import com.example.carbooking.R;

import java.util.ArrayList;
import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private List<CarListObject> carList = new ArrayList<>();
    private OnCarItemClickListener listener;

    public interface OnCarItemClickListener {
        void onEditClick(CarListObject car);
        void onDeleteClick(CarListObject car);
    }

    public CarListAdapter(OnCarItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarListObject car = carList.get(position);

        holder.carName.setText(car.getCarName());
        holder.carType.setText(car.getCarType());
        holder.carPrice.setText("Price: $" + (int) car.getPrice());
        Glide.with(holder.itemView.getContext()).load(car.getCarImage()).into(holder.carImage);

        holder.editButton.setOnClickListener(v -> listener.onEditClick(car));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(car));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carName, carType, carPrice;
        ImageView carImage;
        Button editButton, deleteButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.textCarName);
            carType = itemView.findViewById(R.id.textCarType);
            carPrice = itemView.findViewById(R.id.textCarPrice);
            carImage = itemView.findViewById(R.id.imageCar);
            editButton = itemView.findViewById(R.id.buttonEditCar);
            deleteButton = itemView.findViewById(R.id.buttonDeleteCar);
        }
    }

    public void setData(List<CarListObject> data) {
        this.carList = data;
        notifyDataSetChanged();
    }
}
