//package com.example.classcompanion.Model;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.classcompanion.R;
//
//import java.util.ArrayList;
//
//public class ClassModelAdapter2 extends RecyclerView.Adapter<ClassModelAdapter2.ViewHolder> {
//
//    Context context;
//    ArrayList<ClassModel>classList;
//
//    public ClassModelAdapter2(Context context, ArrayList<ClassModel>classList){
//        this.context = context;
//        this.classList = classList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.item_class2,parent,false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.tvSubject.setText(classList.get(position).getSubject());
//        holder.tvTime.setText(classList.get(position).getTime());
//    }
//
//    @Override
//    public int getItemCount() {
//        if (classList == null) {
//            return 0;
//        } else {
//            return classList.size();
//        }
//
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView tvSubject,tvTime;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvSubject = itemView.findViewById(R.id.tvSubject);
//            tvTime = itemView.findViewById(R.id.tvTime);
//        }
//    }
//}
