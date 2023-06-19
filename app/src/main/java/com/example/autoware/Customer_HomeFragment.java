package com.example.autoware;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Customer_HomeFragment extends Fragment {

    private ImageButton account,services,logout,cars;
    public Customer_HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer__home, container, false);
        account = (ImageButton) v.findViewById(R.id.cust_home_account_btn);
        services = (ImageButton) v.findViewById(R.id.cust_home_services_btn);
        cars  = (ImageButton) v.findViewById(R.id.cust_home_cars_btn);
        logout = (ImageButton) v.findViewById(R.id.cust_home_logout);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cust_framelayout, new Cust_AccountFragment());
                ft.commit();
            }
        });

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cust_framelayout, new Customer_Service_ListFragment());
                ft.commit();
            }
        });

        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cust_framelayout, new CarsFragment());
                ft.commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent it = new Intent(getActivity(), MainActivity.class);
                startActivity(it);
                getActivity().finish();
            }
        });

        return v;
    }
}