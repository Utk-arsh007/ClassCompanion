package com.example.classcompanion;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.classcompanion.Model.ClassModel;
import com.example.classcompanion.Model.DayModel;
import com.example.classcompanion.Adapter.DayModelAdapter2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Timetable extends AppCompatActivity {

    private RecyclerView rvDays;
    private ArrayList<DayModel> dayList;
    private FirebaseFirestore db;
    private DayModelAdapter2 adapter;
    private SwipeRefreshLayout ref_list;

    AtomicInteger completedDays = new AtomicInteger(0);
    Map<String,Integer>dayOrder = new HashMap<>();
    int totalDays = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        rvDays = findViewById(R.id.rvDays);
        ref_list = findViewById(R.id.ref_list);

        rvDays.setLayoutManager(new LinearLayoutManager(this));

        dayList = new ArrayList<>();
        adapter = new DayModelAdapter2(this, dayList);
        rvDays.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dayOrder.put("Monday",1);
        dayOrder.put("Tuesday",2);
        dayOrder.put("Wednesday",3);
        dayOrder.put("Thursday",4);
        dayOrder.put("Friday",5);
        dayOrder.put("Saturday",6);
        dayOrder.put("Sunday",7);

        dayList.clear();
        getData(uid);

        ref_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                completedDays.set(0);
                dayList.clear();
                adapter.notifyDataSetChanged();
                getData(uid);
            }
        });
    }

    private void getData(String uid) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};

        for (String day : days) {

            final String currentDay = day;

            db.collection("users").document(uid).collection("timetable").
                    document(currentDay).collection("classes").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<ClassModel> classList = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    ClassModel model = doc.toObject(ClassModel.class);
                                    classList.add(model);
                                }

                                Collections.sort(classList,(c1,c2)->{
                                    String startTime1 = c1.getTime().split("-")[0].trim();
                                    String startTime2 = c2.getTime().split("-")[0].trim();
                                    return startTime1.compareTo(startTime2);
                                });

                                DayModel dayModel = new DayModel(day, classList, false);
                                dayList.add(dayModel);
                            } else {
                                Toast.makeText(Timetable.this, "Failed to load data for " + day, Toast.LENGTH_SHORT).show();
                                Log.e("FIRESTORE_ERROR", task.getException().getMessage());
                            }
                            if (completedDays.incrementAndGet() == totalDays) {
                                Collections.sort(dayList, (d1, d2) -> {
                                    return dayOrder.get(d1.getDayName()) - dayOrder.get(d2.getDayName());
                                });

                                        String today = new java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault()).format(new java.util.Date());
                                        for (DayModel dayModel : dayList) {
                                            if (dayModel.getDayName().equalsIgnoreCase(today)) {
                                                dayModel.setExpanded(true);

                                                int index = dayList.indexOf(dayModel);
                                                rvDays.scrollToPosition(index);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                        ref_list.setRefreshing(false);
                            }
                        }
                    });
        }
    }
}
