package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;
import com.example.carbooking.adapter.CategoryAdapter;
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

public class CarCategoryActivity extends AppCompatActivity {

    private CategoryAdapter mAdapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_category);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Toyota Cars");

        Spinner carOptionSpinner = findViewById(R.id.select_car_option);
        String[] optionSelect = getResources().getStringArray(R.array.car_option);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionSelect);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carOptionSpinner.setAdapter(spinnerArrayAdapter);

        RecyclerView carRecycler = findViewById(R.id.cars_in_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        carRecycler.setLayoutManager(linearLayoutManager);
        carRecycler.setHasFixedSize(true);
        getTextData();
        mAdapter = new CategoryAdapter(CarCategoryActivity.this);
        carRecycler.setAdapter(mAdapter);
    }

    private void getTextData() {
        DatabaseReference databaseRef =
                FirebaseDatabase.getInstance()
                        .getReference("Carcateogry")
                        .child(Objects.requireNonNull(CustomApplication.type).toUpperCase());

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CarListObject> carData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CarListObject carCategory = snapshot.getValue(CarListObject.class);
                    if (carCategory != null) {
                        Log.d("TAG__", "onDataChange: " + snapshot.getKey());
                        carCategory.setCategory(Objects.requireNonNull(CustomApplication.type).toUpperCase());
                        carCategory.setSubCategory(snapshot.getKey());
                        carData.add(carCategory);
                    }
                }
                mAdapter.setData(carData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error getting data: " + databaseError.getMessage());
            }
        });
    }
}
