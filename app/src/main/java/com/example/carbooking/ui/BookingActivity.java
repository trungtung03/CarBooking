package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.carbooking.R;
import com.example.carbooking.customviews.DateBlock;
import com.example.carbooking.utils.CustomApplication;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

public class BookingActivity extends AppCompatActivity {

    private static final String TAG = BookingActivity.class.getSimpleName();

    private ImageView carImage;

    private TextView pickUpLocation;

    private DateBlock startDateBlock, endDateBlock;

    private String startDay, startMonth, startTime, endDay, endMonth, endTime;

    private CheckBox skiRack, carSeat, navigation;

    private TextView dailyPrice, extraHour, totalAmount;

    private TextView name, address, email, phone;
    private CountDownTimer timer;
    private final long sessionDuration = 5 * 60 * 1000L;

    private RadioButton payPal, creditCard, payNow;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Calendar calendar = Calendar.getInstance();
    int currentYear = calendar.get(Calendar.YEAR);
    int currentMonth = calendar.get(Calendar.MONTH);
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

    private int daysDifference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carImage = findViewById(R.id.car_image);

        pickUpLocation = findViewById(R.id.pick_up_address);
        endDateBlock = findViewById(R.id.destination_date);
        startDateBlock = findViewById(R.id.pick_up_date);

        startDateBlock.setTextContent(startDateBlock.findViewById(R.id.day_in_text), String.valueOf(currentDay));
        startDateBlock.setTextContent(startDateBlock.findViewById(R.id.month_in_text), String.valueOf(currentMonth).toUpperCase());
        startDateBlock.setTextContent(startDateBlock.findViewById(R.id.time_in_text), String.valueOf(currentYear));
        endDateBlock.setTextContent(endDateBlock.findViewById(R.id.day_in_text), String.valueOf(currentDay));
        endDateBlock.setTextContent(endDateBlock.findViewById(R.id.month_in_text), String.valueOf((currentMonth + 1)).toUpperCase());
        endDateBlock.setTextContent(endDateBlock.findViewById(R.id.time_in_text), String.valueOf(currentYear));

        endDateBlock.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar currentCalendar = Calendar.getInstance();
                        long currentTimeInMillis = currentCalendar.getTimeInMillis();

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        long selectedTimeInMillis = selectedCalendar.getTimeInMillis();

                        long differenceInMillis = selectedTimeInMillis - currentTimeInMillis;
                        daysDifference = (int) (differenceInMillis / (1000 * 60 * 60 * 24));
                        extraHour.setText(daysDifference + "");

                        endDateBlock.setTextContent(endDateBlock.findViewById(R.id.day_in_text), String.valueOf(dayOfMonth));
                        endDateBlock.setTextContent(endDateBlock.findViewById(R.id.month_in_text), String.valueOf((month + 1)).toUpperCase());
                        endDateBlock.setTextContent(endDateBlock.findViewById(R.id.time_in_text), String.valueOf(year));

                        totalAmount.setText((Integer.parseInt(dailyPrice.getText().toString()) * daysDifference) + "");
                    },
                    currentYear, currentMonth, currentDay
            );

            datePickerDialog.show();
        });

        skiRack = findViewById(R.id.ski_rack);
        carSeat = findViewById(R.id.child_car_seat);
        navigation = findViewById(R.id.navigation_system);

        dailyPrice = findViewById(R.id.daily_price_booking);
        extraHour = findViewById(R.id.extra_hour);
        totalAmount = findViewById(R.id.total_amount);

        name = findViewById(R.id.customer_name);
        address = findViewById(R.id.customer_address);
        email = findViewById(R.id.customer_email);
        phone = findViewById(R.id.customer_phone);

        payPal = findViewById(R.id.pay_with_pay_pal);
        creditCard = findViewById(R.id.pay_with_credit_card);
        payNow = findViewById(R.id.pay_later);

        extraHour.setText(0 + "");
        dailyPrice.setText(getIntent().getStringExtra("price"));
        totalAmount.setText(getIntent().getIntExtra("total", 0) + "");
        name.setText(getIntent().getStringExtra("name"));
        Glide.with(BookingActivity.this).load(getIntent().getStringExtra("image")).into(carImage);

        Button payNowButton = findViewById(R.id.pay_now);
        payNowButton.setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel();
            }
            Intent checkoutIntent = new Intent(BookingActivity.this, CheckoutActivity.class);
            checkoutIntent.putExtra("total", totalAmount.getText().toString());
            checkoutIntent.putExtra("image", getIntent().getStringExtra("image"));
            checkoutIntent.putExtra("name", getIntent().getStringExtra("name"));
            checkoutIntent.putExtra("email", email.getText().toString());
            checkoutIntent.putExtra("nameUser", name.getText().toString());
            checkoutIntent.putExtra("carKey", getIntent().getStringExtra("carKey"));
            checkoutIntent.putExtra("carType", getIntent().getStringExtra("carType"));
            startActivity(checkoutIntent);
        });

        getUserFromFirestore(db);

        startBookingSession();
    }

    private void startBookingSession() {
        timer = new CountDownTimer(sessionDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                TextView timerTextView = findViewById(R.id.timerTextView);
                timerTextView.setText("Thời gian còn lại: " + minutes + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(BookingActivity.this, "Hết thời gian phiên, quay về trang chủ!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
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
                                email.setText(user.getEmail());
                                name.setText(user.getName());
                                phone.setText(user.getPhoneNumber());
                                address.setText(user.getStreetAddress());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
