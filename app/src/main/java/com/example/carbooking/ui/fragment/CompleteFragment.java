package com.example.carbooking.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;
import com.example.carbooking.adapter.ReservationAdapter;
import com.example.carbooking.models.ReservationObject;
import com.example.carbooking.ui.User;
import com.example.carbooking.utils.CustomApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompleteFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReservationAdapter mAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CompleteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete, container, false);

        recyclerView = view.findViewById(R.id.upcoming);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new ReservationAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        getUserFromFirestore(db);
        return view;
    }

    public void getTestData() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Booking").child("booking").child(CustomApplication.uid);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ReservationObject> carData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Firebase", "Data: " + snapshot.getValue());

                    ReservationObject carCategory = snapshot.getValue(ReservationObject.class);
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

    private void getAllBookingsData() {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Booking").child("booking");

        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ReservationObject> allReservations = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot reservationSnapshot : userSnapshot.getChildren()) {
                        ReservationObject reservation = reservationSnapshot.getValue(ReservationObject.class);
                        if (reservation != null) {
                            allReservations.add(reservation);
                        }
                    }
                }

                mAdapter.setData(allReservations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error getting data: " + databaseError.getMessage());
            }
        });
    }

    private void getUserFromFirestore(FirebaseFirestore db) {
        db.collection("users").document(CustomApplication.uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                if (user.getAptNumber().equalsIgnoreCase("admin")) {
                                    getAllBookingsData();
                                } else {
                                    getTestData();
                                }
                            }
                        } else {
                            Toast.makeText(requireActivity(), "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Lỗi: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
