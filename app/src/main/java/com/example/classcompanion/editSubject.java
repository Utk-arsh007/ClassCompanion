package com.example.classcompanion;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class editSubject extends AppCompatActivity {

    private EditText edtSubject_edit, edtTime_edit;
    Button btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_subject);
        
        edtSubject_edit = findViewById(R.id.edtSubject_edit);
        edtTime_edit = findViewById(R.id.edtTime_edit);
        btnEdit = findViewById(R.id.btnEdit);

        String subject = getIntent().getStringExtra("subject");
        String time = getIntent().getStringExtra("time");
        String docId = getIntent().getStringExtra("docId");
        String dayName = getIntent().getStringExtra("dayName");

        if (subject==null || time==null || docId==null || dayName==null){
            Toast.makeText(this, "Missing fields!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtSubject_edit.setText(subject);
        edtTime_edit.setText(time);



        edtTime_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog startPicker = new TimePickerDialog(editSubject.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int startHour, int startMinute) {
                        String startTime = String.format("%02d:%02d",startHour,startMinute);

                        TimePickerDialog endPicker = new TimePickerDialog(editSubject.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int endHour, int endMinute) {
                                String endTime = String.format("%02d:%02d",endHour,endMinute);

                                edtTime_edit.setText(startTime+"-"+endTime);
                            }
                        }, hour, minute, true);
                        endPicker.show();
                    }
                }, hour, minute, true);
                startPicker.show();

            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(edtSubject_edit.getText().toString().trim()==null||edtTime_edit.getText().toString().trim()==null){
                    Toast.makeText(editSubject.this, "Please fill both fields!", Toast.LENGTH_SHORT).show();
                    return;
                }



                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("timetable")
                        .document(dayName)
                        .collection("classes")
                        .document(docId)
                        .update("subject",edtSubject_edit.getText().toString(),"time",edtTime_edit.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(editSubject.this, "Edited!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(editSubject.this, "Failed to edit!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });





    }
}