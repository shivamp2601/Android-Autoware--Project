package com.example.autoware;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Book_ServiceFragment extends Fragment {


    private LinearLayout SelectCarList;
    private Switch TowingSwitch;
    private Button BookService;
    private TextView ServiceStationDetails;
    private FirebaseFirestore db;
    private EditText description;
    String OwnerUID = "";
    public Book_ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book__service, container, false);
        SelectCarList = (LinearLayout) v.findViewById(R.id.linear_layout_Car_List);
        TowingSwitch = (Switch) v.findViewById(R.id.towing_switch);
        BookService = (Button) v.findViewById(R.id.btn_book_service);
        description = (EditText) v.findViewById(R.id.edittext_cust_service_description);
        ServiceStationDetails = v.findViewById(R.id.ServiceStationDetails);
        ArrayList<CheckBox> cars = new ArrayList<CheckBox>();
        db = FirebaseFirestore.getInstance();
        Bundle b = this.getArguments();
        OwnerUID = b.getString("OwnerUID");
        db.collection("Owners").document(OwnerUID).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Owner o = documentSnapshot.toObject(Owner.class);
                        ServiceStationDetails.setText("Service Station Name: " + o.getGaragename() + "\nOwner Name: " + o.getName() + "\nAddress: " + o.getAddress() + "\nLocation: " + o.getLocation() + "\nContact: +91" + o.getPhone());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Operation Failure", Toast.LENGTH_SHORT).show();
            }
        });
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Cars").document(UID).collection("Cars").get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                          for(DocumentSnapshot document :queryDocumentSnapshots)
                          {
                              Car c = document.toObject(Car.class);
                              CheckBox cb = new CheckBox(getActivity().getApplicationContext());
                              cb.setText(c.getRegistrationNumber() + "("+ c.getBrand()+ " "+c.getModel() + ")");
                              cb.setTextSize(22);
                              
                              cars.add(cb);
                              SelectCarList.addView(cb);
                          }
                    }
                });

        BookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ServiceID  = UUID.randomUUID().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                ArrayList<String> selectedcars = new ArrayList<String>();
                for (CheckBox c : cars) {
                    if(c.isChecked())
                        selectedcars.add(c.getText().toString());
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Services service = new Services(user.getUid(),ServiceID,OwnerUID,selectedcars,TowingSwitch.isChecked(), dateFormat.format(Calendar.getInstance().getTime()),false,description.getText().toString());
                FirebaseFirestore.getInstance().collection("Services").document(ServiceID).set(service).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Service Registered Successfully", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.cust_framelayout,new Customer_Service_ListFragment());
                        ft.commit();
                    }
                });
            }
        });
        return v;

    }
}