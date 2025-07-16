package com.example.classcompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.classcompanion.login.Login;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    LinearLayout llTimetable,llAttendance;
    Toolbar toolbar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        llTimetable = findViewById(R.id.llTimetable);
        llAttendance = findViewById(R.id.llAttendance);
        toolbar = findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class Companion");

        llTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Timetable.class));
            }
        });
        llAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Attendance.class));
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Do You Want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            auth.signOut();
                            Toast.makeText(Home.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Home.this, Login.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show(); // ✅ Only here

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}