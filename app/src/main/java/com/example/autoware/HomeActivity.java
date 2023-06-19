package com.example.autoware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
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

public class HomeActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drl;
    private NavigationView nv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drl = (DrawerLayout) findViewById(R.id.drawer);
        nv = (NavigationView) findViewById(R.id.navigationdrawer);
        FirebaseFirestore.getInstance().collection("Owners").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Owner o = documentSnapshot.toObject(Owner.class);
                        TextView tv = nv.findViewById(R.id.drawer_Textview);
                        tv.setText(o.getName());
                        ImageView ProfileImage = nv.findViewById(R.id.drawer_ImageView);
                        File imgFile = new File(Environment.getExternalStorageDirectory(), "/AutowarePictures/ProfilePicture.jpg");
                        try {
                            Glide.with(getApplicationContext()).load(imgFile).into(ProfileImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        toggle = new ActionBarDrawerToggle(this, drl,R.string.open,R.string.close);
        drl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.replace(R.id.owner_framelayout, new Owner_HomeFragment());
        f.commit();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent it;
                FragmentTransaction ft;
                switch (item.getItemId()) {
                    case R.id.owner_logout:
                        FirebaseAuth.getInstance().signOut();
                        it = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(it);
                        finish();
                        break;

                    case R.id.owner_admin_panel:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.owner_framelayout, new Owner_AdminPanelFragment());
                        ft.commit();
                        break;

                    case R.id.owner_dashboard:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.owner_framelayout, new Owner_HomeFragment());
                        ft.commit();
                        break;

                    case R.id.owner_services:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.owner_framelayout, new Owner_ServicesFragment());
                        ft.commit();
                        break;

                    case R.id.owner_analytics:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.owner_framelayout, new Owner_AnalyticsFragment());
                        ft.commit();
                        break;
                    case R.id.owner_Account:
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.owner_framelayout, new Owner_AccountFragment());
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
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}