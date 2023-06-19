package com.example.autoware;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Customer_Service_ListFragment extends Fragment {

    private ListView ServicesListView;
    private CustomerServiceListAdapter customerServiceListAdapter;
    private ArrayList<Services> services;
    private FloatingActionButton add_service;
    public Customer_Service_ListFragment() {
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
        View v = inflater.inflate(R.layout.fragment_customer__service__list, container, false);
        ServicesListView = (ListView) v.findViewById(R.id.cust_services_list);
        add_service =(FloatingActionButton) v.findViewById(R.id.add_service);
        services = new ArrayList<Services>();
        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cust_framelayout, new Customer_ServicesFragment());
                ft.commit();
            }
        });
        FirebaseFirestore.getInstance().collection("Services").whereEqualTo("customerID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        //services.clear();
                        for (DocumentSnapshot documentSnapshot : snapshots.getDocuments())
                        {
                            services.add(documentSnapshot.toObject(Services.class));
                        }
                        try {
                            customerServiceListAdapter =  new CustomerServiceListAdapter(getActivity(), services);
                            ServicesListView.setAdapter(customerServiceListAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        return v;
    }
}