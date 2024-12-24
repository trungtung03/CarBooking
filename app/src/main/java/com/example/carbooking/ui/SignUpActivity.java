package com.example.carbooking.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DUPLICATE_USER_MESSAGE = "User with this email already exist.";
    private static final String USERS_KEY = "users";
    private static final String REQUIRED_FIELDS_MESSAGE = "All fields are required";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String PASSWORD_LENGTH_MESSAGE = "Password must be at least 6 chars long";
    private Button signup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFullName;
    private EditText editTextStreetAddress;
    private EditText editTextPhoneNumber;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private TextView ssignin;


    private CreditCard creditCard = null;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = findViewById(R.id.register_user);
        editTextEmail = findViewById(R.id.user_email);
        editTextPassword = findViewById(R.id.user_password);
        editTextFullName = findViewById(R.id.name);
        editTextStreetAddress = findViewById(R.id.address_street);
        editTextCity = findViewById(R.id.address_city);
        editTextZipCode = findViewById(R.id.address_zip_code);
        editTextPhoneNumber = findViewById(R.id.phone_number);
        ssignin = findViewById(R.id.link_to_login);
        signup.setOnClickListener(this);
        ssignin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");

    }

    private void registerUser() {
        final String name = editTextFullName.getText().toString().trim();
        final String street_address = editTextStreetAddress.getText().toString().trim();
        final String city = editTextCity.getText().toString().trim();
        final String zipCode = editTextZipCode.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (name.isEmpty()) {
            editTextFullName.setError(getString(R.string.input_error_name));
            editTextFullName.requestFocus();
            return;
        }
        if (street_address.isEmpty()) {
            editTextStreetAddress.setError(getString(R.string.input_error));
            editTextStreetAddress.requestFocus();
            return;
        }
        if (city.isEmpty()) {
            editTextCity.setError(getString(R.string.input_error));
            editTextCity.requestFocus();
            return;
        }
        if (zipCode.isEmpty()) {
            editTextZipCode.setError(getString(R.string.input_error));
            editTextZipCode.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$";
        if (!password.matches(passwordPattern)) {
            editTextPassword.setError(getString(R.string.input_error_password_strength));
            editTextPassword.requestFocus();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                        User newUser = new User(uid, phoneNumber, name, email,
                                phoneNumber, street_address, "",
                                city, zipCode,
                                hashPasswordWithBcrypt(editTextPassword.getText().toString())
                        );
                        newUser.setCreditCard(creditCard);

                        saveUserToFirestore(db, newUser);
                    } else {
                        handleRegistrationError(task.getException());
                    }
                });
    }

    private String hashPasswordWithBcrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    private void saveUserToFirestore(FirebaseFirestore db, User user) {
        db.collection("users").document(user.getId())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        openLoginActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleRegistrationError(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(getApplicationContext(), "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Lỗi: " + Objects.requireNonNull(exception).getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.link_to_login) {
            Intent haveacc = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(haveacc);
        }
        if (v.getId() == R.id.register_user) {
            registerUser();
        }
    }

    private void openLoginActivity() {
        Intent returnToLogin = new Intent();
        returnToLogin.putExtra(EMAIL_KEY, editTextEmail.getText().toString());
        returnToLogin.putExtra(PASSWORD_KEY, editTextPassword.getText().toString());
        setResult(Activity.RESULT_OK, returnToLogin);
        finish();
    }
}

