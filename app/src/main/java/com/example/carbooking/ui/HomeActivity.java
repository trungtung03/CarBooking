package com.example.carbooking.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carbooking.R;
import com.example.carbooking.ui.fragment.BookingFragment;
import com.example.carbooking.ui.fragment.BusinessFragment;
import com.example.carbooking.utils.CustomApplication;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private Fragment bookingFragment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView profileName;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        profileName = headerView.findViewById(R.id.profile_name);

        getUserFromFirestore(db, navigationView);

        bookingFragment = new BookingFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, bookingFragment).commit();
    }

    public void checkTypeUser(NavigationView navigationView, Boolean isAdmin) {
        if (isAdmin) {
            navigationView.getMenu().findItem(R.id.action_admin).setVisible(true);
            navigationView.getMenu().findItem(R.id.action_car_manager).setVisible(true);
            invalidateOptionsMenu();
        } else {
            navigationView.getMenu().findItem(R.id.action_admin).setVisible(false);
            navigationView.getMenu().findItem(R.id.action_car_manager).setVisible(false);
            invalidateOptionsMenu();
        }

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.action_booking) {
                bookingFragment = new BookingFragment();
            } else if (id == R.id.action_admin) {
                Intent adminIntent = new Intent(HomeActivity.this, AdminActivity.class);
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(adminIntent);
            } else if (id == R.id.action_car_manager) {
                Intent adminIntent = new Intent(HomeActivity.this, CarManagerActivity.class);
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(adminIntent);
            } else if (id == R.id.action_profile) {
                new Thread(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(250);
                        Intent accountIntent = new Intent(HomeActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            } else if (id == R.id.action_about) {
                bookingFragment = new BusinessFragment();
            } else if (id == R.id.action_logout) {
                ((CustomApplication) getApplication()).getShared().setUserData("");

                Intent logoutIntent = new Intent(HomeActivity.this, SignInActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_content, bookingFragment).commit();

            @SuppressLint("CutPasteId") DrawerLayout drawer1 = findViewById(R.id.drawer_layout);
            drawer1.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getUserFromFirestore(FirebaseFirestore db, NavigationView navigationView) {
        db.collection("users").document(CustomApplication.uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                profileName.setText(user.getName());
                                checkTypeUser(navigationView, user.getAptNumber().equalsIgnoreCase("admin"));
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
