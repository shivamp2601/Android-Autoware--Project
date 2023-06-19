package com.example.autoware;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Owner_HomeFragment extends Fragment {

    private ImageButton account,services,analytics,spareparts;
    public Owner_HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.fragment_owner__home, container, false);
        account = (ImageButton) v.findViewById(R.id.owner_home_account_btn);
        services = (ImageButton) v.findViewById(R.id.owner_home_services_btn);
        spareparts  = (ImageButton) v.findViewById(R.id.owner_home_spareparts_btn);
        analytics = (ImageButton) v.findViewById(R.id.owner_home_analytics);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.owner_framelayout, new Owner_AccountFragment());
                ft.commit();
            }
        });

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.owner_framelayout, new Owner_ServicesFragment());
                ft.commit();
            }
        });

        spareparts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.owner_framelayout, new Owner_AdminPanelFragment());
                ft.commit();
            }
        });

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.owner_framelayout, new Owner_AnalyticsFragment());
                ft.commit();
            }
        });
        return v;
    }
}