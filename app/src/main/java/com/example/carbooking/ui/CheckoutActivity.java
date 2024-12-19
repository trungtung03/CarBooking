package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.carbooking.R;
import com.example.carbooking.models.ReservationObject;
import com.example.carbooking.utils.CustomApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = CheckoutActivity.class.getSimpleName();

    private ImageView carImage;

    private TextView carName, pickUpAddress, rentalDate, rentalTime, rentalCost, orderNumber,
            contactNumber, rentalStatus;

    Calendar calendar = Calendar.getInstance();
    int currentYear = calendar.get(Calendar.YEAR);
    int currentMonth = calendar.get(Calendar.MONTH);
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Payment Confirmation");

        carImage = findViewById(R.id.image_path);

        carName = findViewById(R.id.car_name);
        pickUpAddress = findViewById(R.id.car_location);
        rentalDate = findViewById(R.id.pick_up_date);
        rentalTime = findViewById(R.id.pick_up_time);
        rentalCost = findViewById(R.id.rental_price);

        orderNumber = findViewById(R.id.order_number);
        contactNumber = findViewById(R.id.contact_number);
        rentalStatus = findViewById(R.id.status);

        rentalDate.setText(currentDay + " " + currentMonth + ", " + currentYear);
        rentalTime.setText(String.format("%02d:%02d:%02d", hour, minute, second));
        carName.setText(getIntent().getStringExtra("name"));

        orderNumber.setText("NPH" + generateRandomNumber());
        rentalCost.setText("$" + getIntent().getStringExtra("total"));
        Glide.with(CheckoutActivity.this).load(getIntent().getStringExtra("image")).into(carImage);

        String carCategory = getIntent().getStringExtra("carKey");

        pushReservation(new ReservationObject(
                carName.getText().toString(),
                getIntent().getStringExtra("email") + " \n" + getIntent().getStringExtra("nameUser"),
                rentalDate.getText().toString(),
                rentalTime.getText().toString(),
                getIntent().getStringExtra("total"),
                getIntent().getStringExtra("image")
        ));

        updateStatus(carCategory, "Booked");

        Button backToMenuButton = findViewById(R.id.back_to_menu);
        backToMenuButton.setOnClickListener(v -> {
            Intent menuIntent = new Intent(CheckoutActivity.this, HomeActivity.class);
            startActivity(menuIntent);
        });
    }

    @SuppressLint("DefaultLocale")
    public static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000000);
        return String.format("%08d", randomNumber);
    }

    public void pushReservation(ReservationObject reservation) {
        String reservationId = CustomApplication.uid;
        DatabaseReference bookingsRef = mDatabase.child("Booking").child("booking");

        String reservationIds = bookingsRef.push().getKey();
        if (reservationId != null) {
            assert reservationIds != null;
            mDatabase.child("Booking").child("booking").child(reservationId).child(reservationIds).setValue(reservation)
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Reservation successfully added to Booking");
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Error adding reservation: " + e.getMessage());
                    });
        } else {
            System.out.println("Failed to generate a new Reservation ID");
        }
    }

    public void updateStatus(String carCategory, String status) {
        DatabaseReference carRef = mDatabase.child("Carcateogry").child(Objects.requireNonNull(getIntent().getStringExtra("carType"))).child(carCategory);
        carRef.child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Car status updated to " + status);
                })
                .addOnFailureListener(e -> {
                    System.out.println("Failed to update car status: " + e.getMessage());
                });
    }
}
