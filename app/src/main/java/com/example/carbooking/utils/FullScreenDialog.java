package com.example.carbooking.utils;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.carbooking.R;

import java.util.Objects;

public class FullScreenDialog extends DialogFragment {

    private static final String TAG = FullScreenDialog.class.getSimpleName();

    private Spinner selectCarSize;

    private RadioGroup radioGroup;

    private RadioButton fiveStar, fourStar, threeStar, twoStar, oneStar;

    private String selectedCustomerRating;
    private String selectedCarSize;

    public static FullScreenDialog newInstance(){
        FullScreenDialog fullScreenDialog = new FullScreenDialog();
        return fullScreenDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        } else {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_layout, container, false);

        radioGroup = view.findViewById(R.id.radio_group);

        fiveStar = view.findViewById(R.id.five_star);
        fourStar = view.findViewById(R.id.four_star);
        threeStar = view.findViewById(R.id.three_star);
        twoStar = view.findViewById(R.id.two_star);
        oneStar = view.findViewById(R.id.one_star);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton btn = group.findViewById(id);
                RadioButton mRadioButton = (RadioButton) group.getChildAt(checkedId);
                //selectedCustomerRating = (String)mRadioButton.getText();
                selectedCustomerRating = (String)btn.getText();
            }
        });

        selectCarSize = view.findViewById(R.id.select_car_size);

        String[] allCarSizes = requireActivity().getResources().getStringArray(R.array.car_size);

        assert getActivity() != null;
        ArrayAdapter<String> carSizeArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allCarSizes);
        carSizeArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        selectCarSize.setAdapter(carSizeArrayAdapter);

        selectCarSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCarSize = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button filterSearchButton = view.findViewById(R.id.filter_search_button);
        filterSearchButton.setOnClickListener(v -> {
            if(TextUtils.isEmpty(selectedCarSize) || TextUtils.isEmpty(selectedCustomerRating)){
                Helper.displayErrorMessage(getActivity(), "No filter option is selected");
            }else{
                //make remote search with available options;
                Helper.displayErrorMessage(getActivity(), selectedCarSize + " " + selectedCustomerRating);
            }
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

}
