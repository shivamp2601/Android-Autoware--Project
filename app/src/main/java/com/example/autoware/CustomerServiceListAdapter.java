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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class CustomerServiceListAdapter extends BaseAdapter {

    private Activity c;
    private ArrayList<Services> services;
    private static LayoutInflater inflater = null;

    public CustomerServiceListAdapter() {
    }

    public CustomerServiceListAdapter(Activity c, ArrayList<Services> servicess) {
        this.c = c;
        this.services = servicess;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Services getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = inflater.inflate(R.layout.item_recycler,null);
        TextView ServiceHeader = (TextView) itemView.findViewById(R.id.text_big);
        TextView ServiceDetails = (TextView) itemView.findViewById(R.id.text_small);
        ImageView Listicon = (ImageView) itemView.findViewById(R.id.item_image);
        Listicon.setImageDrawable(ContextCompat.getDrawable(c,R.drawable.service_icon));
        ImageButton Status = (ImageButton) itemView.findViewById(R.id.cust_car_delete);
        Status.setBackgroundColor(Color.TRANSPARENT);
        Services service = getItem(position);
        if(service.getStatus())
            Listicon.setBackgroundColor(Color.rgb(66, 245, 90));
        else
            Listicon.setBackgroundColor(Color.LTGRAY);
        ServiceHeader.setText("");
        for (String s : service.getCars()) {
            ServiceHeader.setText(ServiceHeader.getText() + s +", ");
        }
        if(service.getTowingFlag())
            ServiceDetails.setText(service.getDate() + " Towing service: YES");
        else
            ServiceDetails.setText(service.getDate() + " Towing service: NO");
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((FragmentActivity)c).getSupportFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("ServiceID",getItem(position).getServiceID());
                Fragment fragment  = new CustomerViewServiceFragment();
                fragment.setArguments(b);
                ft.replace(R.id.cust_framelayout, fragment);
                ft.commit();
            }
        });
        return  itemView;
    }
}
