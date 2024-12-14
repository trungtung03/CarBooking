package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;
import com.example.carbooking.adapter.CarListAdapter;
import com.example.carbooking.customviews.CarFormView;
import com.example.carbooking.models.CarListObject;
import com.example.carbooking.utils.CustomApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarManagerActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCars;
    private Button buttonAddCar;
    private CarListAdapter carListAdapter;
    private List<CarListObject> carList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);

        recyclerViewCars = findViewById(R.id.recyclerViewCars);
        buttonAddCar = findViewById(R.id.buttonAddCar);

        carList = new ArrayList<>();
        fetchCarCategories();

        carListAdapter = new CarListAdapter(new CarListAdapter.OnCarItemClickListener() {
            @Override
            public void onEditClick(CarListObject car) {
                openCarFormDialog(car, true);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDeleteClick(CarListObject car) {
                if (car == null || car.getCategory() == null || car.getSubCategory() == null) {
                    Toast.makeText(CarManagerActivity.this, "Invalid car data!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference carRef = FirebaseDatabase.getInstance()
                        .getReference("Carcateogry")
                        .child(car.getCategory())
                        .child(car.getSubCategory());

                ProgressDialog progressDialog = new ProgressDialog(CarManagerActivity.this);
                progressDialog.setMessage("Deleting...");
                progressDialog.show();

                carRef.removeValue()
                        .addOnSuccessListener(aVoid -> {
                            progressDialog.dismiss();
                            startActivity(new Intent(CarManagerActivity.this, HomeActivity.class));
                            Toast.makeText(CarManagerActivity.this, "Car deleted successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            startActivity(new Intent(CarManagerActivity.this, HomeActivity.class));
                            Toast.makeText(CarManagerActivity.this, "Failed to delete car: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCars.setAdapter(carListAdapter);

        buttonAddCar.setOnClickListener(v -> openCarFormDialog(null, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void openCarFormDialog(@Nullable CarListObject car, boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Edit Car" : "Add Car");

        final CarFormView carFormView = new CarFormView(this);
        carFormView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        if (isEdit && car != null) {
            carFormView.setCarData(car);
        }

        builder.setView(carFormView);
        builder.setPositiveButton(isEdit ? "Update" : "Add", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                CarListObject newCar = carFormView.getCarData();
                String category = carFormView.getSelectedCategory().toUpperCase();

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Đang lưu...");
                progressDialog.show();

                DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                        .getReference("Carcateogry")
                        .child(category);

                if (isEdit) {
                    String carKey = car.getSubCategory();
                    databaseRef.child(carKey).setValue(newCar)
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                startActivity(new Intent(CarManagerActivity.this, HomeActivity.class));
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    String newKey = databaseRef.push().getKey();
                    if (newKey != null) {
                        databaseRef.child(newKey).setValue(newCar)
                                .addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    startActivity(new Intent(CarManagerActivity.this, HomeActivity.class));
                                    newCar.setSubCategory(newKey);
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCarCategories() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Carcateogry");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CarListObject> carList = new ArrayList<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.getKey();

                    for (DataSnapshot carSnapshot : categorySnapshot.getChildren()) {
                        try {
                            CarListObject car = carSnapshot.getValue(CarListObject.class);
                            if (car != null) {
                                car.setCategory(categoryName);
                                car.setSubCategory(carSnapshot.getKey());
                                carList.add(car);
                            }
                        } catch (Exception e) {
                            Log.e("Firebase", "Lỗi khi parse dữ liệu xe: " + e.getMessage());
                        }
                    }
                }

                carListAdapter.setData(carList);

                Log.d("Firebase", "Tổng số xe đã load: " + carList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Lỗi khi lấy dữ liệu: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CarManagerActivity.this, HomeActivity.class));
    }
}
