package com.example.classcompanion.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classcompanion.Home;
import com.example.classcompanion.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtLoginEmail,edtLoginPass;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPass = findViewById(R.id.edtLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtLoginEmail.getText().toString().trim();
                String pass = edtLoginPass.getText().toString().trim();
                if(!email.isEmpty()&&!pass.isEmpty()){
                    auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, Home.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    if (email.isEmpty()){
                        edtLoginEmail.requestFocus();
                    }
                    else {
                        edtLoginPass.requestFocus();
                    }
                }
            }
        });
        
        
    }
}