package com.example.autoware;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Customer_ServicesFragment extends Fragment {

    EditText city;
    ImageButton search;
    ListView ServiceStationsList;
    private ArrayList<Owner> ServiceStations = new ArrayList<Owner>();
    private ArrayList<String> ServiceStationUIDs = new ArrayList<String>();
    public Customer_ServicesFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_customer__services, container, false);
        city = (EditText) v.findViewById(R.id.City);
        search = (ImageButton) v.findViewById(R.id.search_btn);
        ServiceStationsList = (ListView) v.findViewById(R.id.cust_servicestations_list);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String City = city.getText().toString();
                FirebaseFirestore.getInstance().collection("Owners").whereEqualTo("location",City)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ServiceStations.add(document.toObject(Owner.class));
                                ServiceStationUIDs.add(document.getId());
                            }
                            ServiceStationsAdapter serviceStationsAdapter =  new ServiceStationsAdapter(getActivity(), ServiceStations, ServiceStationUIDs);
                            ServiceStationsList.setAdapter(serviceStationsAdapter);
                        } else {
                            Toast.makeText(getActivity(), "Error in getting docs", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return v;
    }
}