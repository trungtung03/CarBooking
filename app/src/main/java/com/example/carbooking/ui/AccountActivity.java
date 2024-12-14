package com.example.carbooking.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.carbooking.R;
import com.example.carbooking.ui.fragment.ProfileFragment;
import com.example.carbooking.ui.fragment.ReservationFragment;
import com.google.android.material.navigation.NavigationView;

public class AccountActivity extends AppCompatActivity {

    private final NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragments = null;
        if (item.getItemId() == R.id.navigation_profile) {
            fragments = new ProfileFragment();
        } else if (item.getItemId() == R.id.navigation_reservation) {
            fragments = new ReservationFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        assert fragments != null;
        ft.replace(R.id.content, fragments).commit();

        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }

        Fragment fragment = new ProfileFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment).commit();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
