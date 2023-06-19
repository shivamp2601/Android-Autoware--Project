package com.example.autoware;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CarListAdapter extends BaseAdapter {
    private Activity c;
    private ArrayList<Car> Cars;
    private static LayoutInflater inflater = null;

    public CarListAdapter() {
    }

    public CarListAdapter(Activity c, ArrayList<Car> cars) {
        this.c = c;
        Cars = cars;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Cars.size();
    }

    @Override
    public Car getItem(int position) {
        return Cars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = inflater.inflate(R.layout.item_recycler,null);
        TextView RegistrationNumber = (TextView) itemView.findViewById(R.id.text_big);
        TextView CarDetails = (TextView) itemView.findViewById(R.id.text_small);
        ImageView Listicon = (ImageView) itemView.findViewById(R.id.item_image);
        ImageButton deletecar = (ImageButton) itemView.findViewById(R.id.cust_car_delete);

        deletecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore.getInstance().collection("Cars").document(UID).collection("Cars").document(getItem(position).getRegistrationNumber()).delete().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FragmentTransaction ft = ((FragmentActivity)c).getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.cust_framelayout,new CarsFragment());
                                ft.commit();
                            }
                        });
            }
        });
        Car selectedCar = getItem(position);
        RegistrationNumber.setText(selectedCar.getRegistrationNumber());
        CarDetails.setText(selectedCar.getBrand()+" " +selectedCar.getModel() +", " +selectedCar.getFueltype());
        return itemView;
    }
}
