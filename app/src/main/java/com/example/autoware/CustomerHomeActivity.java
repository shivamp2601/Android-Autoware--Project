package com.example.autoware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Slide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class CustomerHomeActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drl;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        drl = (DrawerLayout) findViewById(R.id.cust_drawer);
        nv = (NavigationView) findViewById(R.id.cust_navigationdrawer);

        FirebaseFirestore.getInstance().collection("Customers").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Customer c = documentSnapshot.toObject(Customer.class);
                        TextView tv = nv.findViewById(R.id.drawer_Textview);
                        tv.setText(c.getName());
                        ImageView ProfileImage = nv.findViewById(R.id.drawer_ImageView);
                        File imgFile = new File(Environment.getExternalStorageDirectory(), "/AutowarePictures/ProfilePicture.jpg");
                        try {
                            Glide.with(getApplicationContext()).load(imgFile).into(ProfileImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        toggle = new ActionBarDrawerToggle(this, drl, R.string.open, R.string.close);
        drl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.replace(R.id.cust_framelayout, new Customer_HomeFragment());
        f.commit();
        //Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent it;
                FragmentTransaction ft;
                switch (item.getItemId()) {
                    case R.id.dr_cust_logout:
                        FirebaseAuth.getInstance().signOut();
                        it = new Intent(CustomerHomeActivity.this, MainActivity.class);
                        startActivity(it);
                        finish();
                        break;

                    case R.id.dr_cust_cars:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.cust_framelayout, new CarsFragment());
                        ft.commit();
                        break;

                    case R.id.dr_cust_services:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.cust_framelayout, new Customer_Service_ListFragment());
                        ft.commit();
                        break;

                    case R.id.dr_cust_account:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.cust_framelayout, new Cust_AccountFragment());
                        ft.commit();
                        break;

                    case R.id.dr_cust_home:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.cust_framelayout, new Customer_HomeFragment());
                        ft.commit();
                        break;
                }
                drl.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Toast.makeText(this, "onOptionsItemSelected "+id, Toast.LENGTH_SHORT).show();
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}


