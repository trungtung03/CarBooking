package com.example.carbooking.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.carbooking.R;
import com.example.carbooking.ui.WebActivity;

import java.util.Objects;

public class InformationFragment extends Fragment {


    public InformationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        requireActivity().setTitle("");

        view.findViewById(R.id.faq).setOnClickListener(v -> {
            Intent faqIntent = new Intent(getActivity(), WebActivity.class);
            faqIntent.putExtra("INFORMATION", "faq");
            getActivity().startActivity(faqIntent);
        });

        view.findViewById(R.id.terms).setOnClickListener(v -> {
           Intent termsIntent = new Intent(getActivity(), WebActivity.class);
           termsIntent.putExtra("INFORMATION", "terms");
            getActivity().startActivity(termsIntent);
        });

        view.findViewById(R.id.policy).setOnClickListener(v -> {
            Intent policyIntent = new Intent(getActivity(),  WebActivity.class);
            policyIntent.putExtra("INFORMATION", "policy");
            getActivity().startActivity(policyIntent);
        });

        view.findViewById(R.id.third_party).setOnClickListener(v -> {
            Intent thirdPartyIntent = new Intent(getActivity(), WebActivity.class);
            thirdPartyIntent.putExtra("INFORMATION", "thirdparty");
            requireActivity().startActivity(thirdPartyIntent);
        });

        return view;
    }

}
