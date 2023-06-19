package com.example.autoware;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Authentication {
    public FirebaseAuth firebaseAuth;
    public Boolean success = null;

    public Authentication() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Boolean Register(Context c, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                success = task.isSuccessful();
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(c, "Already a user to this app", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(c, "User registered", Toast.LENGTH_SHORT).show();
            }
        });
        return success;
    }

    public Boolean Signin(Context c, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(c, "User you are logged in", Toast.LENGTH_SHORT).show();
                    success = true;
                }
                else
                    Toast.makeText(c, "Something went wrong, please verify your credentials", Toast.LENGTH_SHORT).show();
                }
            });
        return  success;
        }
    }


