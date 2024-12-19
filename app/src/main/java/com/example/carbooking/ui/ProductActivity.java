package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carbooking.R;

public class ProductActivity extends AppCompatActivity {

    private static final String TAG = ProductActivity.class.getSimpleName();

    private ImageView carImage;

    private TextView mileage, fuelType, engine, numOfSeats, fuelEconomy, airCondition, hourlyPrice, dailyPrice;

    private ImageView favoriteImage;

    private RatingBar ratingBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getIntent().getStringExtra("name"));

        carImage = findViewById(R.id.header_image);

        mileage = findViewById(R.id.mileage);
        fuelType = findViewById(R.id.fuel_type);
        engine = findViewById(R.id.engine);
        numOfSeats = findViewById(R.id.num_of_seats);
        fuelEconomy = findViewById(R.id.fuel_economy);
        airCondition = findViewById(R.id.air_condition);
        hourlyPrice = findViewById(R.id.hourly_price);
        dailyPrice = findViewById(R.id.daily_price);

        favoriteImage = findViewById(R.id.favorite);

        ratingBar = findViewById(R.id.rating_bar);

        dailyPrice.setText(getIntent().getIntExtra("price", 0) + "");
        Glide.with(ProductActivity.this).load(getIntent().getStringExtra("image")).into(carImage);
        Button bookingButton = findViewById(R.id.continue_booking);
        bookingButton.setOnClickListener(v -> {
            Intent bookingIntent = new Intent(ProductActivity.this, BookingActivity.class);
            bookingIntent.putExtra("price", dailyPrice.getText().toString());
            bookingIntent.putExtra("hour", hourlyPrice.getText().toString());
            bookingIntent.putExtra("total", Integer.parseInt(dailyPrice.getText().toString()) * Integer.parseInt(hourlyPrice.getText().toString()));
            bookingIntent.putExtra("image", getIntent().getStringExtra("image"));
            bookingIntent.putExtra("name", getIntent().getStringExtra("name"));
            bookingIntent.putExtra("carKey", getIntent().getStringExtra("carKey"));
            bookingIntent.putExtra("carType", getIntent().getStringExtra("carType"));
            startActivity(bookingIntent);
        });

    }

}
