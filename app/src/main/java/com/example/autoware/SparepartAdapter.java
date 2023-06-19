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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SparepartAdapter extends BaseAdapter {
    private Activity c;
    private ArrayList<Sparepart> spareparts;
    private static LayoutInflater inflater = null;

    public SparepartAdapter() {
    }

    public SparepartAdapter(Activity c, ArrayList<Sparepart> spareparts) {
        this.c = c;
        this.spareparts = spareparts;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return spareparts.size();
    }

    @Override
    public Sparepart getItem(int position) {
        return spareparts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = inflater.inflate(R.layout.item_recycler,null);
        TextView Sparepartname = (TextView) itemView.findViewById(R.id.text_big);
        TextView Sparepartprice = (TextView) itemView.findViewById(R.id.text_small);
        ImageButton deletesparepart = (ImageButton) itemView.findViewById(R.id.cust_car_delete);
        ImageView Spareparticon = (ImageView) itemView.findViewById(R.id.item_image);
        Spareparticon.setImageDrawable(ContextCompat.getDrawable(c,R.drawable.sparepart_icon));
        deletesparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore.getInstance().collection("Spareparts").document(UID).collection("Spareparts").document(getItem(position).getName()).delete().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FragmentTransaction ft = ((FragmentActivity)c).getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.owner_framelayout,new Owner_AdminPanelFragment());
                                ft.commit();
                            }
                        });
            }
        });
        Sparepart selectedsparepart = getItem(position);
        Sparepartname.setText(selectedsparepart.getName());
        Sparepartprice.setText(String.valueOf(selectedsparepart.getPrice()));
        return itemView;
    }
}