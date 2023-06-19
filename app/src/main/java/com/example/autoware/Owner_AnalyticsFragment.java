package com.example.autoware;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Owner_AnalyticsFragment extends Fragment {


    private TextView totalRevenue, totalEquipments, totalCustomers, totalServices;
    private int totaltowing = 0, pending = 0, completed = 0, totalsparesused = 0,revenue =0,cost =0 ;
    ArrayList<String> customers;
    private Services s;

    public Owner_AnalyticsFragment() {
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

        customers = new ArrayList<String>();
        View v = inflater.inflate(R.layout.fragment_owner__analytics, container, false);
        totalRevenue = (TextView) v.findViewById(R.id.TotalRevenue);
        totalEquipments = (TextView) v.findViewById(R.id.TotalEquipments);
        totalCustomers = (TextView) v.findViewById(R.id.TotalCustomers);
        totalServices = (TextView) v.findViewById(R.id.TotalServices);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("Services").whereEqualTo("ownerID", user.getUid()).get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            s = documentSnapshot.toObject(Services.class);
                            if (!customers.contains(s.getCustomerID()))
                                customers.add(s.getCustomerID());
                            if (s.getTowingFlag())
                                totaltowing++;
                            if (s.getStatus())
                                completed++;
                            else
                                pending++;
                            if (s.getSpareparts() != null)
                            {    totalsparesused += s.getSpareparts().size();
                                for (Integer i:s.getSparepartsprice()) {
                                    cost+=i;
                                }
                            }
                            revenue += s.getAmount();


                        }
                        totalServices.setText("Total Services: "+queryDocumentSnapshots.size() +"\nPending Serivces:"+pending+"\nCompleted Services:"+completed);
                        totalCustomers.setText("Total Customers till date:"+customers.size());
                        totalRevenue.setText("Total Revenue:"+revenue+"\nTotal Costs:"+cost);
                        FirebaseFirestore.getInstance().collection("Spareparts").document(user.getUid()).collection("Spareparts").get().addOnSuccessListener(
                                new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        float avgequipmentprice = 0;
                                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                        {
                                            Sparepart sp = documentSnapshot.toObject(Sparepart.class);
                                            avgequipmentprice+=sp.getPrice();
                                        }
                                        totalEquipments.setText("Total Spareparts: "+queryDocumentSnapshots.size()+"\n"+"Spares used till today: "+totalsparesused+"\n"+"Average Equipment Cost: "+ avgequipmentprice/queryDocumentSnapshots.size());
                                    }
                                }
                        );
                    }
                }
        );
        return v;
    }
}