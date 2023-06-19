package com.example.autoware;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCarFragment extends Fragment {

    private EditText RegistrationNumber, Model, Brand;
    private RadioButton Petrol,Diesel,CNG_LPG;
    private String FuelType = "";
    private Button Add_car;
    public AddCarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_car, container, false);
        RegistrationNumber = (EditText) v.findViewById(R.id.RegistrationNumber);
        Model = (EditText) v.findViewById(R.id.CarModel);
        Brand = (EditText) v.findViewById(R.id.CarBrand);
        Petrol = (RadioButton) v.findViewById(R.id.Petrol);
        Diesel = (RadioButton) v.findViewById(R.id.Diesel);
        CNG_LPG = (RadioButton) v.findViewById(R.id.CNG_LPG);
        Add_car = (Button) v.findViewById(R.id.Add_car_button);

        Petrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuelType = "Petrol";
            }
        });

        Diesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuelType = "Diesel";
            }
        });

        CNG_LPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuelType = "CNG_LPG";
            }
        });

        Add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String UID = firebaseAuth.getCurrentUser().getUid();
                Car c = new Car(RegistrationNumber.getText().toString(),UID,Brand.getText().toString(),Model.getText().toString(),FuelType);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference dref = db.collection("Cars").document(UID).collection("Cars").document(RegistrationNumber.getText().toString());
                dref.set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity().getApplicationContext(), "Car Added successfully", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.cust_framelayout,new CarsFragment());
                        ft.commit();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Operation Failed, Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;
    }
}