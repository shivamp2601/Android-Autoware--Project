package com.example.autoware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class AddCarActivity extends AppCompatActivity {

    private EditText RegistrationNumber, Model, Brand;
    private String FuelType = "";
    private Button Add_car;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        RegistrationNumber = (EditText) findViewById(R.id.RegistrationNumber);
        Model = (EditText) findViewById(R.id.CarModel);
        Brand = (EditText) findViewById(R.id.CarBrand);
        Add_car = (Button) findViewById(R.id.Add_car_button);
        Add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String UID = firebaseAuth.getCurrentUser().getUid();
                Car c = new Car(RegistrationNumber.getText().toString(),UID,Brand.getText().toString(),Model.getText().toString(),FuelType);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference dref = db.collection("Cars").document(UID);
                dref.set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddCarActivity.this, "Car Added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(AddCarActivity.this,CustomerHomeActivity.class);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCarActivity.this, "Operation Failed, Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void SetFuelType(View v)
    {
        RadioButton rd = (RadioButton)v;
        FuelType = rd.getText().toString();

    }
}