package com.example.autoware;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class CarsFragment extends Fragment {

    private FloatingActionButton add_car;
    private ListView car_listView;
    private ArrayList<Car> cars = new ArrayList<Car>();
    private CarListAdapter carListAdapter;
    public CarsFragment() {/*Required empty public constructor*/}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cars, container, false);
        car_listView = (ListView) view.findViewById(R.id.cust_cars_list);
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cref = db.collection("Cars").document(UID).collection("Cars");
        cref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               for (DocumentSnapshot document : queryDocumentSnapshots)
               {
                   cars.add(document.toObject(Car.class));
                   //Toast.makeText(getActivity(), cars.get(0).getRegistrationNumber(), Toast.LENGTH_SHORT).show();
               }
                carListAdapter =  new CarListAdapter(getActivity(), cars);
                car_listView.setAdapter(carListAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

        add_car = (FloatingActionButton) view.findViewById(R.id.add_car);
        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cust_framelayout,new AddCarFragment());
                ft.commit();
            }
        });
        return view;
    }
}