package com.example.carbooking.ui.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;
import com.example.carbooking.adapter.ListingAdapter;
import com.example.carbooking.models.CarCategoryObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private ListingAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        RecyclerView bookingRecyclerView = view.findViewById(R.id.car_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        bookingRecyclerView.setLayoutManager(gridLayoutManager);
        bookingRecyclerView.setHasFixedSize(true);
        getTextData();
        mAdapter = new ListingAdapter(getActivity());
        bookingRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void getTextData() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Carbooking");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CarCategoryObject> carData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CarCategoryObject carCategory = snapshot.getValue(CarCategoryObject.class);
                    if (carCategory != null) {
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
