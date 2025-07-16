package com.example.classcompanion;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classcompanion.Adapter.AttendanceAdapter;
import com.example.classcompanion.Model.AttendanceModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {

    private RecyclerView rv_attendance;
    ArrayList<AttendanceModel> attendanceList;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance);

        rv_attendance = findViewById(R.id.rv_attendance);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        attendanceList = new ArrayList<>();
        rv_attendance.setLayoutManager(new LinearLayoutManager(this));
        AttendanceAdapter adapter = new AttendanceAdapter(this,attendanceList);
        rv_attendance.setAdapter(adapter);

        db.collection("users")
                .document(uid)
                .collection("attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(DocumentSnapshot docSnap : task.getResult()){
                                String sub = docSnap.getId();
                                int present  = docSnap.contains("presentCount")?docSnap.getLong("presentCount").intValue():0;
                                int total = docSnap.contains("totalCount")?docSnap.getLong("totalCount").intValue():0;

                                AttendanceModel attendanceModel = new AttendanceModel(sub,present,total);
                                attendanceList.add(attendanceModel);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(Attendance.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}