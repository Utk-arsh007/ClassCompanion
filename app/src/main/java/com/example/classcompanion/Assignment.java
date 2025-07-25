package com.example.classcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classcompanion.Adapter.AssignmentAdapter;
import com.example.classcompanion.Model.AssignmentModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Assignment extends AppCompatActivity {

    private RecyclerView rv_assignment;
    private FloatingActionButton fab_assignAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assignment);

        rv_assignment = findViewById(R.id.rv_assignment);
        fab_assignAdd = findViewById(R.id.fab_assignAdd);

        fab_assignAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Assignment.this, AddAssignment.class));
            }
        });

        ArrayList<AssignmentModel>assignmentList = new ArrayList<>();
        Collections.sort(assignmentList, new Comparator<AssignmentModel>() {
            @Override
            public int compare(AssignmentModel a1, AssignmentModel a2) {
                return Long.compare(a1.getDueDate(), a2.getDueDate()); // ascending
            }
        });

        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(this,assignmentList);
        rv_assignment.setLayoutManager(new LinearLayoutManager(this));
        rv_assignment.setAdapter(assignmentAdapter);

    }
}