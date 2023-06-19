package com.example.autoware;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CustomerViewServiceFragment extends Fragment {

    private TextView ServiceDetails;
    String detail;
    public CustomerViewServiceFragment() {
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
        View v = inflater.inflate(R.layout.fragment_customer_view_service, container, false);
        Bundle b= this.getArguments();
        FirebaseFirestore.getInstance().collection("Services").document(b.getString("ServiceID")).get().
                addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Services service = documentSnapshot.toObject(Services.class);
                        ServiceDetails = (TextView) v.findViewById(R.id.service_details_textview);
                        detail ="Date: \t"+service.getDate();
                        detail ="\nDescription: \t"+service.getDescription();
                        for (String s: service.getCars()) {
                            detail += "\nCar: \t"+ s;
                        }
                        FirebaseFirestore.getInstance().collection("Owners").document(service.getOwnerID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Owner o = documentSnapshot.toObject(Owner.class);
                                    detail += "\nGarage: \t" + o.getGaragename();
                                    detail +="\nGarage Owner: \t" + o.getName();
                                    detail +="\nAddress: \t" + o.getAddress();
                                    detail +="\nPhone Number: \t" + o.getPhone();
                                    detail +="\nSpareparts\t";
                                    try {
                                        for (int i = 0; i < service.getSpareparts().size(); i++)
                                            detail += "\n     -" + service.getSpareparts().get(i) + " - ₹" + service.getSparepartsprice().get(i);
                                        detail += "\nTotal Billing Amount: \t" + service.getAmount();
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                if(service.getStatus())
                                    detail +="\nStatus: \t Your Service is completed, Please collect your car from the venue";
                                else
                                    detail +="\nStatus: \t Your Service is pending";
                                ServiceDetails.setText(detail);
                            }
                        });
                    }
                });
        return  v;
    }
}