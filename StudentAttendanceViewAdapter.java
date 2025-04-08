package com.example.fbauth.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.util.List;

public class StudentAttendanceViewAdapter extends RecyclerView.Adapter<StudentAttendanceViewAdapter.ViewHolder> {

    private final List<StudentAttendanceModel> attendanceList;

    public StudentAttendanceViewAdapter(List<StudentAttendanceModel> list) {
        this.attendanceList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvSubject, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentAttendanceModel attendance = attendanceList.get(position);
        holder.tvDate.setText("Date: " + attendance.getDate());
        holder.tvSubject.setText("Subject: " + attendance.getSubject());
        holder.tvStatus.setText("Status: " + attendance.getStatus());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
}
