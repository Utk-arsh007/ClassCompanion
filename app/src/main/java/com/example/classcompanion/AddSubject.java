package com.example.classcompanion;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classcompanion.Model.ClassModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddSubject extends AppCompatActivity {
    String dayName = "" ;
    Button btnAdd;
    EditText edtSubject,edtTime;
    FirebaseFirestore db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        btnAdd = findViewById(R.id.btnAdd);
        edtSubject = findViewById(R.id.edtSubject);
        edtTime = findViewById(R.id.edtTime);
        db = FirebaseFirestore.getInstance();
        dayName = getIntent().getStringExtra("dayName");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                int hour = calender.get(Calendar.HOUR_OF_DAY);
                int minute = calender.get(Calendar.MINUTE);

                TimePickerDialog startPicker = new TimePickerDialog(AddSubject.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int startHour, int startMinute) {
                        String startTime = String.format("%02d:%02d", startHour, startMinute);

                        TimePickerDialog endPicker = new TimePickerDialog(AddSubject.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int endHour, int endMinute) {
                                String endTime = String.format("%02d:%02d", endHour, endMinute);
                                edtTime.setText(startTime + " - " + endTime);
                            }
                        }, hour, minute, true);
                        endPicker.show();
                    }
                }, hour, minute, true);
                startPicker.show();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = edtSubject.getText().toString();
                String time = edtTime.getText().toString();

                String docId = db.collection("users").document(uid)
                        .collection("timetable").document(dayName)
                        .collection("classes").document().getId();

                if (!subject.isEmpty() && !time.isEmpty()) {
                    db.collection("users").document(uid)
                            .collection("timetable").document(dayName)
                            .collection("classes").document(docId)
                            .set(new ClassModel(subject, time,docId))
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(AddSubject.this, "Subject Added!", Toast.LENGTH_SHORT).show();
                                edtSubject.setText("");
                                edtTime.setText("");
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AddSubject.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(AddSubject.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}