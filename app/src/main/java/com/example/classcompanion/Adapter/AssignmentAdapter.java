package com.example.classcompanion.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classcompanion.Model.AssignmentModel;
import com.example.classcompanion.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    Context context;
    ArrayList<AssignmentModel> assignmentList;

    public AssignmentAdapter(Context context,ArrayList<AssignmentModel>assignmentList){
        this.context = context;
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_assignment,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        AssignmentModel assignmentModel = assignmentList.get(position);
        holder.txt_assignSub.setText(assignmentModel.getAssignSub());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date dueDate = new Date(assignmentModel.getDueDate());
        String dueDateStr = sdf.format(dueDate);
        holder.txt_dueDate.setText("Due: " + dueDateStr);

        holder.cbAssign.setOnCheckedChangeListener(null);
        holder.cbAssign.setChecked(assignmentModel.isCompleted());

        holder.cbAssign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                assignmentModel.setCompleted(isChecked);
                notifyItemChanged(pos);

                if(assignmentModel.getId()!=null&& !assignmentModel.getId().isEmpty()){
                    db.collection("Assignment")
                            .document(assignmentModel.getId())
                            .update("isCompleted",isChecked);
                }

            }
        });

        View statusView = holder.view_status;
        if(assignmentModel.isCompleted()){
            statusView.setBackgroundColor(Color.parseColor("#4CAF50"));
        }else{
            long currentTime = System.currentTimeMillis();
            long dueTime = assignmentModel.getDueDate();

            if(dueTime<currentTime){
                statusView.setBackgroundColor(Color.parseColor("#F44336"));
            } else if (dueTime-currentTime<2*24*60*60*1000) {
                statusView.setBackgroundColor(Color.parseColor("#FFC107"));
            }
            else {
                statusView.setBackgroundColor(Color.parseColor("#2196F3"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_assignSub,txt_dueDate;
        CheckBox cbAssign;
        View view_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_assignSub = itemView.findViewById(R.id.txt_assignSub);
            txt_dueDate = itemView.findViewById(R.id.txt_dueDate);
            view_status = itemView.findViewById(R.id.view_status);
            cbAssign = itemView.findViewById(R.id.cbAssign);
        }
    }

}
