package com.example.classcompanion.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classcompanion.Model.AttendanceModel;
import com.example.classcompanion.R;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    Context context;
    ArrayList<AttendanceModel>attendanceList;


    public AttendanceAdapter(Context context, ArrayList<AttendanceModel> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_attendance,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceModel attendanceModel = attendanceList.get(position);
        String subject = attendanceModel.getSubject();
        int present = attendanceModel.getPresentCount();
        int total = attendanceModel.getTotalCount();

        int percentage = (total==0)?0:(present*100)/total;

        holder.txt_subName_attendance.setText(subject);
        holder.txt_percent_attendance.setText("Percentage: "+percentage+"%");
        holder.txt_attendedClasses_attendance.setText("Classes Attended: "+present);
        holder.txt_totalClasses_attendance.setText("Total Classes: "+total);
        holder.attendanceProgress.setProgress(percentage);

    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_subName_attendance,txt_percent_attendance,txt_totalClasses_attendance,txt_attendedClasses_attendance;
        ProgressBar attendanceProgress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_subName_attendance = itemView.findViewById(R.id.txt_subName_attendance);
            txt_percent_attendance = itemView.findViewById(R.id.txt_percent_attendance);
            txt_totalClasses_attendance = itemView.findViewById(R.id.txt_totalClasses_attendance);
            txt_attendedClasses_attendance = itemView.findViewById(R.id.txt_attendedClasses_attendance);
            attendanceProgress = itemView.findViewById(R.id.attendanceProgress);
        }
    }

}
