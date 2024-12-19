package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    private EditText etName, etStreetAddress, etAptNumber, etCity, etZipCode, etEmail, etPassword, etPhoneNumber;
    private Button btnSave, btnDelete;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        etStreetAddress = findViewById(R.id.etStreetAddress);
        etAptNumber = findViewById(R.id.etAptNumber);
        etCity = findViewById(R.id.etCity);
        etZipCode = findViewById(R.id.etZipCode);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        etPassword = findViewById(R.id.etPassword);

        loadUserData();

        btnSave.setOnClickListener(v -> saveUser());

        btnDelete.setOnClickListener(v -> deleteUser());
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            DocumentReference userRef = db.collection("users").document(Objects.requireNonNull(getIntent().getStringExtra("uid")));

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    etName.setText(user.getName());
                    etStreetAddress.setText(user.getStreetAddress());
                    etAptNumber.setText(user.getAptNumber());
                    etCity.setText(user.getCity());
                    etZipCode.setText(user.getZipCode());
                    etEmail.setText(user.getEmail());
                    etPhoneNumber.setText(user.getPhoneNumber());
                    etPassword.setText(user.getPassword());

                    btnDelete.setVisibility(View.VISIBLE);
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
        String password = etPassword.getText().toString();

        if (mAuth.getCurrentUser() != null) {
            String userId = getIntent().getStringExtra("uid");
            User user = new User(userId, "", name, email, phoneNumber, streetAddress, aptNumber, city, zipCode, password);

            assert userId != null;
            db.collection("users").document(userId).set(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UserActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserActivity.this, AdminActivity.class));
                    })
                    .addOnFailureListener(e -> Toast.makeText(UserActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void deleteUser() {
        if (mAuth.getCurrentUser() != null) {
            String userId = getIntent().getStringExtra("uid");
            assert userId != null;
            db.collection("users").document(userId).delete()
                    .addOnSuccessListener(aVoid -> {
                        mAuth.getCurrentUser().delete()
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(UserActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserActivity.this, AdminActivity.class));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(UserActivity.this, "Error deleting user from Firebase Auth: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(UserActivity.this, "Error deleting user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
