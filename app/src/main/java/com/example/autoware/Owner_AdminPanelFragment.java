package com.example.autoware;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Owner_AdminPanelFragment extends Fragment {

    private FloatingActionButton btn_sparepartadd;
    private EditText sparepartname,sparepartprice;
    private ListView sparepartlist;
    private ArrayList<Sparepart> spareparts = new ArrayList<Sparepart>();
    private String UID;
    public Owner_AdminPanelFragment() {
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
        View v  = inflater.inflate(R.layout.fragment_owner__admin_panel, container, false);
        btn_sparepartadd = (FloatingActionButton) v.findViewById(R.id.btn_sparepartadd);
        sparepartname = (EditText) v.findViewById(R.id.edittext_sparepartname);
        sparepartprice = (EditText) v.findViewById(R.id.edittext_sparepartprice);
        sparepartlist = (ListView) v.findViewById(R.id.owner_price_list);

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Spareparts").document(UID).collection("Spareparts").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots)
                        {
                            spareparts.add(document.toObject(Sparepart.class));
                            //Toast.makeText(getActivity(), cars.get(0).getRegistrationNumber(), Toast.LENGTH_SHORT).show();
                        }
                        SparepartAdapter sparepartAdapter =  new SparepartAdapter(getActivity(),spareparts );
                        sparepartlist.setAdapter(sparepartAdapter);
                    }
                });

        btn_sparepartadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = sparepartname.getText().toString();
                int price = Integer.parseInt(sparepartprice.getText().toString());
                Sparepart newsparepart = new Sparepart(name,price);
                FirebaseFirestore.getInstance().collection("Spareparts").document(UID).collection("Spareparts").
                        document(name).set(newsparepart).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Sparepart added", Toast.LENGTH_SHORT).show();
                        spareparts.add(newsparepart);
                        SparepartAdapter sparepartAdapter =  new SparepartAdapter(getActivity(),spareparts );
                        sparepartlist.setAdapter(sparepartAdapter);
                        sparepartname.setText("");
                        sparepartprice.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Operation Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;
    }
}