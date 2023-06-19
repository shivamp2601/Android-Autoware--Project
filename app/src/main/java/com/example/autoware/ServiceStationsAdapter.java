package com.example.autoware;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ServiceStationsAdapter extends BaseAdapter {

    private Activity c;
    private ArrayList<Owner> ServiceStations;
    private ArrayList<String> ServiceStationUIDs;
    private static LayoutInflater inflater = null;

    public ServiceStationsAdapter() {
    }

    public ServiceStationsAdapter(Activity c, ArrayList<Owner> serviceStations, ArrayList<String> ServiceStationUIDs) {
        this.c = c;
        ServiceStations = serviceStations;
        this.ServiceStationUIDs = ServiceStationUIDs;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ServiceStations.size();
    }

    @Override
    public Owner getItem(int position) {
        return ServiceStations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = inflater.inflate(R.layout.item_recycler,null);
        TextView GarageName = (TextView) itemView.findViewById(R.id.text_big);
        TextView GarageDetails = (TextView) itemView.findViewById(R.id.text_small);
        ImageView Listicon = (ImageView) itemView.findViewById(R.id.item_image);
        ImageButton BookService = (ImageButton) itemView.findViewById(R.id.cust_car_delete); //button to book service
        Listicon.setImageDrawable(ContextCompat.getDrawable(c,R.drawable.garage_icon));
        BookService.setBackgroundColor(Color.TRANSPARENT);
        GarageName.setText(getItem(position).getGaragename());
        GarageDetails.setText(getItem(position).getName() +", "+ getItem(position).getPhone());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((FragmentActivity)c).getSupportFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("OwnerUID",ServiceStationUIDs.get(position));
                Fragment fragment  = new Book_ServiceFragment();
                fragment.setArguments(b);
                ft.replace(R.id.cust_framelayout, fragment);
                ft.commit();
            }
        });
        return itemView;
    }
}
