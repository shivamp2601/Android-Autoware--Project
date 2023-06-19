package com.example.autoware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private EditText email, password, confirmpassword;
    private Button login, register;
    private CheckBox check_if_owner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference dref;
    private Boolean isowner = false;
    private Boolean write_success = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = (EditText) this.findViewById(R.id.sign_up_email);
        password = (EditText) findViewById(R.id.sign_up_passsword);
        confirmpassword = (EditText) findViewById(R.id.sign_up_confirmpassword);
        login = (Button) findViewById(R.id.sign_up_login);
        register = (Button) findViewById(R.id.register);
        check_if_owner = (CheckBox) findViewById(R.id.Check_owner);


        check_if_owner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isowner = isChecked;

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText())) {
                    email.setError("email field is empty");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    email.setError("Please enter a valid email address");
                    return;
                }
                if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Password is empty");
                    return;
                }
                if (password.getText().length() < 8) {
                    password.setError("Password is less than 8 characters");
                    return;
                }
                if (TextUtils.isEmpty(confirmpassword.getText())) {
                    confirmpassword.setError("Confirm Password is empty");
                    return;
                }

                if (!TextUtils.equals(password.getText(), confirmpassword.getText())) {
                    confirmpassword.setError("Passwords do not match");
                    return;
                }
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignupActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                            String ID = firebaseAuth.getCurrentUser().getUid();
                            if (isowner) {
                                db.collection("Owners").document(ID).set(new Owner("","","","","",firebaseAuth.getCurrentUser().getEmail())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent i = new Intent(SignupActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupActivity.this, "Failed to create a collection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                db.collection("Customers").document(ID).set(new Customer("", "", firebaseAuth.getCurrentUser().getEmail())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent i = new Intent(SignupActivity.this, CustomerHomeActivity.class);
                                        startActivity(i);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupActivity.this, "Failed to create a collection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "Already a user to this app", Toast.LENGTH_SHORT).show();
                            //firestore owner/customer document creation.
                            return;
                        }
                    }
                });
            }
        });
    }
}