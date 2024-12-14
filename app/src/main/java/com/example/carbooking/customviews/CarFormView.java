package com.example.carbooking.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carbooking.R;
import com.example.carbooking.models.CarListObject;

import java.util.Arrays;
import java.util.List;

public class CarFormView extends LinearLayout {
    private EditText editCarName, editCarImage, editCarType, editSeatNumber,
            editMileage, editFuelType, editStatus, editPrice;
    private Spinner categorySpinner;
    private View rootView;

    public CarFormView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CarFormView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_car_form, this, true);

        categorySpinner = rootView.findViewById(R.id.categorySpinner);
        editCarName = rootView.findViewById(R.id.editCarName);
        editCarImage = rootView.findViewById(R.id.editCarImage);
        editCarType = rootView.findViewById(R.id.editCarType);
        editSeatNumber = rootView.findViewById(R.id.editSeatNumber);
        editMileage = rootView.findViewById(R.id.editMileage);
        editFuelType = rootView.findViewById(R.id.editFuelType);
        editStatus = rootView.findViewById(R.id.editStatus);
        editPrice = rootView.findViewById(R.id.editPrice);

        setupCategorySpinner(context);
    }

    private void setupCategorySpinner(Context context) {
        List<String> categories = Arrays.asList("BMW", "VolVo");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    public void setCarData(CarListObject car) {
        if (car.getCategory() != null) {
            ArrayAdapter adapter = (ArrayAdapter) categorySpinner.getAdapter();
            int position = adapter.getPosition(car.getCategory());
            categorySpinner.setSelection(position);
        }

        editCarName.setText(car.getCarName());
        editCarImage.setText(car.getCarImage());
        editCarType.setText(car.getCarType());
        editSeatNumber.setText(car.getSeatNumber());
        editMileage.setText(car.getMileage());
        editFuelType.setText(car.getFuelType());
        editStatus.setText(car.getStatus());
        editPrice.setText(String.valueOf((int) car.getPrice()));
    }

    public CarListObject getCarData() throws IllegalArgumentException {
        validateInputs();

        return new CarListObject(
                1,
                1,
                editCarName.getText().toString().trim(),
                editCarImage.getText().toString().trim(),
                editCarType.getText().toString().trim(),
                editSeatNumber.getText().toString().trim(),
                editMileage.getText().toString().trim(),
                editFuelType.getText().toString().trim(),
                editStatus.getText().toString().trim(),
                Integer.parseInt(editPrice.getText().toString().trim())
        );
    }

    private void validateInputs() throws IllegalArgumentException {
        if (editCarName.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập tên xe");
        }

        if (editPrice.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập giá xe");
        }

        try {
            int price = Integer.parseInt(editPrice.getText().toString().trim());
            if (price <= 0) {
                throw new IllegalArgumentException("Giá xe phải lớn hơn 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá xe không hợp lệ");
        }
    }

    public String getSelectedCategory() {
        return categorySpinner.getSelectedItem().toString();
    }
}