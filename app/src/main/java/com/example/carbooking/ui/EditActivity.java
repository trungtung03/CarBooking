package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private EditText etName, etStreetAddress, etAptNumber, etCity, etZipCode, etEmail, etPhoneNumber;
    private Button btnSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etNameProfile);
        etStreetAddress = findViewById(R.id.etStreetAddressProfile);
        etAptNumber = findViewById(R.id.etAptNumberProfile);
        etCity = findViewById(R.id.etCityProfile);
        etZipCode = findViewById(R.id.etZipCodeProfile);
        etEmail = findViewById(R.id.etEmailProfile);
        etPhoneNumber = findViewById(R.id.etPhoneNumberProfile);
        btnSave = findViewById(R.id.btnSaveProfile);

        loadUserData();

        btnSave.setOnClickListener(v -> saveUser());
    }

    private void loadUserData() {
        Log.d("TAG__", "loadUserData: 333333333333" + mAuth.getCurrentUser());
        if (mAuth.getCurrentUser() != null) {
            DocumentReference userRef = db.collection("users").document(Objects.requireNonNull(getIntent().getStringExtra("uid")));
            Log.d("TAG__", "loadUserData: 22222222222");
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                Log.d("TAG__", "loadUserData: " + "user.getName()");
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    Log.d("TAG__", "loadUserData: " + "cxzzzzzzvc");
                    assert user != null;
                    Log.d("TAG__", "loadUserData: " + user.getName());
                    etName.setText(user.getName());
                    etStreetAddress.setText(user.getStreetAddress());
                    etAptNumber.setText(user.getAptNumber());
                    etCity.setText(user.getCity());
                    etZipCode.setText(user.getZipCode());
                    etEmail.setText(user.getEmail());
                    etPhoneNumber.setText(user.getPhoneNumber());
                    password = user.getPassword();
                }
            });
        }
    }

    private void saveUser() {
        String name = etName.getText().toString();
        String streetAddress = etStreetAddress.getText().toString();
        String aptNumber = etAptNumber.getText().toString();
        String city = etCity.getText().toString();
        String zipCode = etZipCode.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        if (mAuth.getCurrentUser() != null) {
            String userId = getIntent().getStringExtra("uid");
            User user = new User(userId, "", name, email, phoneNumber, streetAddress, aptNumber, city, zipCode, password);

            assert userId != null;
            db.collection("users").document(userId).set(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditActivity.this, AccountActivity.class));
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}