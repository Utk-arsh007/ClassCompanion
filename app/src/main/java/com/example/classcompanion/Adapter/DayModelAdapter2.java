package com.example.classcompanion.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classcompanion.AddSubject;
import com.example.classcompanion.Model.ClassModel;
import com.example.classcompanion.Model.DayModel;
import com.example.classcompanion.R;
import com.example.classcompanion.editSubject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DayModelAdapter2 extends RecyclerView.Adapter<DayModelAdapter2.ViewHolder> {

    private final Context context;
    private final ArrayList<DayModel> dayList;


    public DayModelAdapter2(Context context, ArrayList<DayModel> dayList) {
        this.context = context;
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_days, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayModel currentModel = dayList.get(position);

        holder.tvDayTitle.setText(currentModel.getDayName());
        holder.llClassList.setVisibility(currentModel.isExpanded() ? View.VISIBLE : View.GONE);

        // Toggle on click
        holder.llDay.setOnClickListener(v -> {
            currentModel.setExpanded(!currentModel.isExpanded());
            notifyDataSetChanged();
        });

        holder.llClassList.removeAllViews();
        for (ClassModel classModel : currentModel.getClassList()) {
            View classView = LayoutInflater.from(context).inflate(R.layout.item_class, holder.llClassList, false);
            TextView subject = classView.findViewById(R.id.tvSubject);
            TextView time = classView.findViewById(R.id.tvTime);
            subject.setText(classModel.getSubject());
            time.setText(classModel.getTime());

            CheckBox cbClass = classView.findViewById(R.id.cbClass);

            String todayDate = new java.text.SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault()).format(new java.util.Date());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String subjectName = classModel.getSubject();

            db.collection("users").document(uid).collection("attendance").document(subjectName).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String lastMarked = documentSnapshot.getString("lastMarked");
                                        if (lastMarked != null && lastMarked.equals(todayDate)) {
                                            cbClass.setChecked(true);
                                            cbClass.setEnabled(false);
                                            holder.llDay.setBackgroundResource(R.drawable.highlight);
                                        } else {
                                            cbClass.setChecked(false);
                                            cbClass.setEnabled(true);
                                            holder.llDay.setBackgroundResource(R.drawable.unhighlighted);
                                        }
                                    }
                                }
                            });

            cbClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = cbClass.isChecked();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String subjectName = classModel.getSubject();

                    db.collection("users")
                            .document(uid)
                            .collection("attendance")
                            .document(subjectName)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot snapshot = task.getResult();
                                        int present = 0;
                                        int total = 0;
                                        if (snapshot.exists()){
                                            if (snapshot.contains("presentCount")){
                                                present = snapshot.getLong("presentCount").intValue();
                                            }
                                            if (snapshot.contains("totalCount")){
                                                total = snapshot.getLong("totalCount").intValue();
                                            }
                                        }
                                        if(isChecked){
                                            present++;
                                            total++;
                                        }
                                        else{
                                            total++;
                                        }

                                        Map<String,Object> map = new HashMap<>();
                                        map.put("presentCount",present);
                                        map.put("totalCount",total);
                                        map.put("lastMarked",todayDate);
                                        db.collection("users")
                                                .document(uid)
                                                .collection("attendance")
                                                .document(subjectName)
                                                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(context, "Attendance Marked", Toast.LENGTH_SHORT).show();
                                                            cbClass.setEnabled(false);
                                                        }
                                                        else {
                                                            Toast.makeText(context, "Failed to Mark Attendance", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            });

            classView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                    deleteDialog.setTitle("Delete Class")
                            .setMessage("Are you sure want to delete this class?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore.getInstance().collection("users")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("timetable")
                                            .document(currentModel.getDayName())
                                            .collection("classes")
                                            .document(classModel.getDocId())
                                            .delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                                        currentModel.getClassList().remove(classModel);
                                                        notifyDataSetChanged();
                                                    }
                                                    else {
                                                        Toast.makeText(context, "Failed to delete!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    deleteDialog.show();

                    return true;
                }
            });

            classView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder editdialog = new AlertDialog.Builder(context)
                            .setTitle("Edit Class")
                            .setMessage("Do You want to edit this class?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentEdit = new Intent(context, editSubject.class);
                                    intentEdit.putExtra("subject",classModel.getSubject());
                                    intentEdit.putExtra("time",classModel.getTime());
                                    intentEdit.putExtra("docId",classModel.getDocId());
                                    intentEdit.putExtra("dayName",currentModel.getDayName());
                                    intentEdit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intentEdit);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    editdialog.show();
                }
            });

            holder.llClassList.addView(classView);
        }

        // Add button
        holder.btnAddSubject.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddSubject.class);
            intent.putExtra("dayName", currentModel.getDayName());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayTitle;
        Button btnAddSubject;
        LinearLayout llClassList, llDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayTitle = itemView.findViewById(R.id.tvDayTitle);
            btnAddSubject = itemView.findViewById(R.id.btnAddSubject);
            llClassList = itemView.findViewById(R.id.llClassList);
            llDay = itemView.findViewById(R.id.llDay);
        }
    }
}
