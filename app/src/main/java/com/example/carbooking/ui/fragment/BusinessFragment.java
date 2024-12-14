package com.example.carbooking.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.carbooking.R;
import com.example.carbooking.utils.Helper;

public class BusinessFragment extends Fragment {

    private static final String TAG = BusinessFragment.class.getSimpleName();

    private TextView businessName;
    private TextView businessAddress;
    private TextView name;
    private TextView description;
    private TextView email;
    private TextView phone;


    public BusinessFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business, container, false);
        getActivity().setTitle("About Us");

        businessName = view.findViewById(R.id.business_name);
        businessAddress = view.findViewById(R.id.business_address);
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);

        return view;
    }

}
