package com.example.carbooking.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carbooking.R;
import com.example.carbooking.models.FeatureObject;
import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureViewHolder>{

    private static final String TAG = FeatureAdapter.class.getSimpleName();

    private Context context;

    private List<FeatureObject> featureList;

    public FeatureAdapter(Context context, List<FeatureObject> featureList) {
        this.context = context;
        this.featureList = featureList;
    }

    @Override
    public FeatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_feature_layout, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeatureViewHolder holder, int position) {
        FeatureObject featureObject = featureList.get(position);

        holder.title.setText(featureObject.getFeatureTitle());
        holder.detail.setText(featureObject.getFeatureValue());

    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }
}
