package com.example.autoware;

import android.app.Service;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Owner_ServicesFragment extends Fragment {

    private ListView ServicesListView;
    private ArrayList<Services> services;
    public Owner_ServicesFragment() {
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
        View v = inflater.inflate(R.layout.fragment_owner__services, container, false);
        ServicesListView = (ListView) v.findViewById(R.id.Owner_Services_List);
        services = new ArrayList<Services>();
        FirebaseFirestore.getInstance().collection("Services").whereEqualTo("ownerID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        services.clear();
                        for (DocumentSnapshot documentSnapshot : snapshots.getDocuments())
                        {
                            services.add(documentSnapshot.toObject(Services.class));
                        }
                        OwnerServiceListAdapter ownerServiceListAdapter = null;
                        try {
                            ownerServiceListAdapter = new OwnerServiceListAdapter(getActivity(), services);
                            ServicesListView.setAdapter(ownerServiceListAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        return v;
    }
}