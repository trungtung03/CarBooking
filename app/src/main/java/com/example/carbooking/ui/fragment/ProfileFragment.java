package com.example.carbooking.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.carbooking.R;
import com.example.carbooking.ui.EditActivity;
import com.example.carbooking.ui.User;
import com.example.carbooking.utils.CustomApplication;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView name, email, phone, address;
    private ImageView edit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        assert getActivity() != null;
        getActivity().setTitle("Profile");

        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        phone = view.findViewById(R.id.user_phone);
        edit = view.findViewById(R.id.imgEdit);

        edit.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EditActivity.class);
            intent.putExtra("uid", CustomApplication.uid);
            startActivity(intent);
        });

        getUserFromFirestore(db);

        return view;
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
                                name.setText(user.getName());
                                email.setText(user.getEmail());
                                phone.setText(user.getPhoneNumber());
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
