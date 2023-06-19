package com.example.autoware;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button resetpassword, changepassword;
    private ImageButton back;
    private EditText newpassword, currentpassword, emailforreset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        back = (ImageButton) findViewById(R.id.back_button);
        resetpassword = (Button) findViewById(R.id.reset_btn);
        changepassword = (Button) findViewById(R.id.changepassword_btn);
        newpassword = (EditText) findViewById(R.id.new_password);
        currentpassword = (EditText) findViewById(R.id.current_password);
        emailforreset = (EditText) findViewById(R.id.reset_password_email);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailforreset.getText()))
                {
                    emailforreset.setError("Enter your registered email");
                    return;
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailforreset.getText().toString()).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ChangePasswordActivity.this, "Reset Password is sent on provided email id", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                );
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailforreset.getText()))
                {
                    emailforreset.setError("Enter your registered email");
                    return;
                }
                if(TextUtils.isEmpty(currentpassword.getText()))
                {
                    currentpassword.setError("Enter your current password");
                    return;
                }
                if(TextUtils.isEmpty(newpassword.getText()))
                {
                    newpassword.setError("Enter your new password");
                    return;
                }
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailforreset.getText().toString(),currentpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ChangePasswordActivity.this, "Your Password is updated successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                });
            }
        });

    }
}