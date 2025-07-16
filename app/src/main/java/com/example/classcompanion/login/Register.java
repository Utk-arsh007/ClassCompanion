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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private Button btnSignup;
    private EditText edtUserName, edtUserEmail, edtUserPassword, edtUserRePassword;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        btnSignup = findViewById(R.id.btnSignup);
        edtUserName = findViewById(R.id.edtUserName);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserPassword = findViewById(R.id.edtUserPassword);
        edtUserRePassword = findViewById(R.id.edtUserRePassword);

        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtUserName.getText().toString();
                String email = edtUserEmail.getText().toString();
                String password = edtUserPassword.getText().toString();
                String rePassword = edtUserRePassword.getText().toString();

                if(password.equals(rePassword)){
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Home.class));
                            }
                            else{
                                Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this, "Password not matched", Toast.LENGTH_SHORT).show();
                    edtUserRePassword.requestFocus();
                }
            }
        });






    }
}