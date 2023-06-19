package com.example.autoware;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.FirestoreChannel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Owner_ManageServiceFragment extends Fragment {

    private TextView ServiceDetails, jobcard;
    private Button Save, Back, Done;
    private EditText Tax, ServiceCharges;
    private LinearLayout SparepartsList;
    private List<CheckBox> SparepartsCheckBoxes;
    private List<Sparepart> Spareparts;
    private FirebaseFirestore db;
    private Services service;
    private int total = 0;
    private String ser;
    private String ToCustomerEmail;
    public Owner_ManageServiceFragment() {
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
        View v = inflater.inflate(R.layout.fragment_owner__manage_service, container, false);
        Save = (Button) v.findViewById(R.id.btn_save);
        Done = (Button) v.findViewById(R.id.btn_done);
        Back = (Button) v.findViewById(R.id.btn_back);
        ServiceDetails = (TextView) v.findViewById(R.id.ServiceDetails);
        jobcard = (TextView) v.findViewById(R.id.jobcard);
        ServiceCharges = (EditText) v.findViewById(R.id.service_charges);
        Tax = (EditText) v.findViewById(R.id.Charges_tax);
        SparepartsList = v.findViewById(R.id.linear_layout_sparepartlist);
        db = FirebaseFirestore.getInstance();
        Spareparts = new ArrayList<Sparepart>();
        SparepartsCheckBoxes = new ArrayList<CheckBox>();
        Tax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s))
                    PopulateJobCard();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Bundle b = this.getArguments();
        db.collection("Services").document(b.getString("ServiceID")).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        service = documentSnapshot.toObject(Services.class);
                        PopulateServiceDetails();

                        db.collection("Spareparts").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Spareparts").get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                                            Sparepart s = document.toObject(Sparepart.class);
                                            Spareparts.add(s);
                                            CheckBox cb = new CheckBox((getActivity().getApplicationContext()));
                                            cb.setText(s.getName() + " - ₹" + s.getPrice());
                                            cb.setTextSize(20);
                                            cb.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    PopulateJobCard();
                                                }
                                            });
                                            SparepartsCheckBoxes.add(cb);
                                            SparepartsList.addView(cb);
                                        }
                                    }
                                });
                        Back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.owner_framelayout, new Owner_ServicesFragment());
                                ft.commit();
                            }
                        });
                        Save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> Spares = new ArrayList<String>();
                                ArrayList<Integer> Sparesprices = new ArrayList<Integer>();
                                for (int i = 0; i < SparepartsCheckBoxes.size(); i++) {
                                    if (SparepartsCheckBoxes.get(i).isChecked()) {
                                        Spares.add(Spareparts.get(i).getName());
                                        Sparesprices.add(Spareparts.get(i).getPrice());
                                    }
                                }
                                service.setAmount(total);
                                service.setSpareparts(Spares);
                                service.setSparepartsprice(Sparesprices);
                                FirebaseFirestore.getInstance().collection("Services").document(service.getServiceID()).set(service)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity().getApplicationContext(), "Service Details updated successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        Done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> Spares = new ArrayList<String>();
                                ArrayList<Integer> Sparesprices = new ArrayList<Integer>();
                                for (int i = 0; i < SparepartsCheckBoxes.size(); i++) {
                                    if (SparepartsCheckBoxes.get(i).isChecked()) {
                                        Spares.add(Spareparts.get(i).getName());
                                        Sparesprices.add(Spareparts.get(i).getPrice());
                                    }
                                }
                                service.setAmount(total);
                                service.setSpareparts(Spares);
                                service.setSparepartsprice(Sparesprices);
                                service.setStatus(true);
                                FirebaseFirestore.getInstance().collection("Services").document(service.getServiceID()).set(service)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity().getApplicationContext(), "Service Details updated successfully", Toast.LENGTH_SHORT).show();
                                                String[] TO = { ToCustomerEmail};
                                                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                                                emailIntent.setData(Uri.parse("mailto:"));
                                                emailIntent.setType("text/plain");
                                                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "No reply: Your Service is completed");
                                                emailIntent.putExtra(Intent.EXTRA_TEXT, ServiceDetails.getText().toString() +"\n" + jobcard.getText().toString());

                                                try {
                                                    startActivity(Intent.createChooser(emailIntent, "Send Email about the completion"));
                                                } catch (android.content.ActivityNotFoundException ex) {
                                                    Toast.makeText(getActivity(), "Error in sending email", Toast.LENGTH_SHORT).show();
                                                }
                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.replace(R.id.owner_framelayout, new Owner_ServicesFragment());
                                                ft.commit();
                                            }
                                        });

                            }
                        });
                    }
                }
        );
        return v;
    }

    public void PopulateServiceDetails() {
        ser = "Service Details:\n";
        for (String s : service.getCars()) {
            ser += "Car Number: " + s + "\n";
        }
        if (service.getTowingFlag())
            ser += "Towing Service: YES\n";
        else
            ser += "Towing Service: NO\n";
        db.collection("Customers").document(service.getCustomerID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Customer c = documentSnapshot.toObject(Customer.class);
                ser += c.getName() + "\nEmail: " + c.getEmail() + "\nAddress" + c.getLocation();
                ToCustomerEmail = c.getEmail();
            }
        });
        ser += "\nDate of Booking: " + service.getDate();
        ser += "\nDescription: " + service.getDescription();
        if (service.getStatus())
            ser += "\nStatus: Pending";
        else
            ser += "\nStatus: Completed";
        ServiceDetails.setText(ser);
    }

    public void PopulateJobCard() {
        String jobcardstring = "";
        total = 0;
        for (int i = 0; i < SparepartsCheckBoxes.size(); i++) {
            if (SparepartsCheckBoxes.get(i).isChecked()) {
                jobcardstring += "Spare part " + i + ": " + Spareparts.get(i).getName() + " - ₹" + Spareparts.get(i).getPrice() + "\n";
                total += Spareparts.get(i).getPrice();
            }
        }
        if (!TextUtils.isEmpty(ServiceCharges.getText())) {
            total += Integer.parseInt(ServiceCharges.getText().toString());
            jobcardstring += "\nService Charges applied:\t " + ServiceCharges.getText().toString();
            jobcardstring += "\nTotal Amount:\t ₹" + total;

        }
        if (!TextUtils.isEmpty(Tax.getText())) {
            total += (Integer.parseInt(Tax.getText().toString()) / total);

            jobcardstring += "\nTotal Taxable Amount:\t  ₹" + (total + ((total / 100) * Float.parseFloat(Tax.getText().toString()))) + " @ " + Tax.getText().toString();
        }
        jobcard.setText(jobcardstring);
    }
}