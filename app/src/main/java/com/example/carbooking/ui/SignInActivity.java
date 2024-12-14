package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbooking.R;
import com.example.carbooking.utils.CustomApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String REQUIRED_FIELDS_MESSAGE = "All fields are mandatory";
    private static final String CREDENTIALS_INCORRECT_MESSAGE = "Email or Password Incorrect";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String REGISTRATION_SUCCESSFUL_MESSAGE = "Registration Successful. Please Login";
    private Button loginButton;
    private EditText loginEmail;
    private EditText loginPassword;
    private FirebaseAuth firebaseAuth;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        loginButton = findViewById(R.id.btn_login);
        Button registerButton = findViewById(R.id.btn_register);
        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
        TextView forgot = findViewById(R.id.forgotten_password);

        firebaseAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgot.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.btn_login) {
           loginUser();
       } else if (v.getId() == R.id.btn_register) {
           registerUser();
       } else if (v.getId() == R.id.forgotten_password) {
           Intent forgottenIntent = new Intent(SignInActivity.this, ForgottenActivity.class);
           startActivity(forgottenIntent);
       }
    }

    private void loginUser() {

        final String eemail = loginEmail.getText().toString().trim();
        String ppassword = loginPassword.getText().toString().trim();

        if (eemail.isEmpty()) {
            loginEmail.setError(getString(R.string.input_error_email));
            loginEmail.requestFocus();
            return;
        }

        if (ppassword.isEmpty()) {
            loginPassword.setError(getString(R.string.input_error_password));
            loginPassword.requestFocus();
            return;
        }
        makeFieldsNonEditable();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("email", eemail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot userSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String storedPassword = userSnapshot.getString("password");

                        if (storedPassword != null && storedPassword.equals(ppassword)) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                            CustomApplication.uid = userSnapshot.getString("id");
                            openHomeScreen();
                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                            makeFieldsEditable();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email not found!", Toast.LENGTH_SHORT).show();
                        makeFieldsEditable();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void registerUser() {
        //loginUserProgress.setVisibility(View.GONE);
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loginEmail.setText(data.getStringExtra(EMAIL_KEY));
            loginPassword.setText(data.getStringExtra(PASSWORD_KEY));
            Toast.makeText(this, REGISTRATION_SUCCESSFUL_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void makeFieldsNonEditable() {
        loginEmail.setEnabled(false);
        loginPassword.setEnabled(false);
        loginButton.setEnabled(false);
    }

    private void makeFieldsEditable() {
        loginEmail.setEnabled(true);
        loginPassword.setEnabled(true);
        loginButton.setEnabled(true);
    }

    private void openHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}