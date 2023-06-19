package com.example.autoware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login, signup, changepassword;
    private CheckBox check_if_owner;
    private Boolean isowner = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) this.findViewById(R.id.email);
        password = (EditText) findViewById(R.id.passsword);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        check_if_owner =  (CheckBox) findViewById(R.id.check_if_owner);
        check_if_owner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isowner = isChecked;
            }
        });

        changepassword = (Button) findViewById(R.id.changepassword);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Password is empty");
                    return;
                }
                if (TextUtils.isEmpty(email.getText())) {
                    email.setError("email field is empty");
                    return;
                }


                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(c, "User you are logged in", Toast.LENGTH_SHORT).show();
                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference dref;
                            String ID = firebaseAuth.getCurrentUser().getUid();
                            if(isowner)
                            {
                                dref = firebaseFirestore.collection("Owners").document(ID);
                                dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                            Toast.makeText(MainActivity.this, "No such Owner exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                dref = firebaseFirestore.collection("Customers").document(ID);
                                dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            Intent i = new Intent(MainActivity.this, CustomerHomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                            Toast.makeText(MainActivity.this, "No such Customer exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please verify your credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseFirestore.getInstance().collection("Customers").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(
                    new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                Intent i = new Intent(MainActivity.this, CustomerHomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
            );
            FirebaseFirestore.getInstance().collection("Owners").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(
                    new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
            );
            //Toast.makeText(this, "Username: " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Please login (username is NULL)", Toast.LENGTH_SHORT).show();

    }
}

