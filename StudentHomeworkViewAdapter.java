package com.example.fbauth.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.util.List;

public class StudentHomeworkViewAdapter extends RecyclerView.Adapter<StudentHomeworkViewAdapter.ViewHolder> {

    private final List<StudentHomeworkModel> homeworkList;

    public StudentHomeworkViewAdapter(List<StudentHomeworkModel> homeworkList) {
        this.homeworkList = homeworkList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHomeworkSubject, tvHomeworkText, tvHomeworkTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHomeworkSubject = itemView.findViewById(R.id.tv_homework_subject);
            tvHomeworkText = itemView.findViewById(R.id.tv_homework_text);
            tvHomeworkTimestamp = itemView.findViewById(R.id.tv_homework_timestamp);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_homework, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentHomeworkModel model = homeworkList.get(position);
        holder.tvHomeworkSubject.setText("Subject: " + model.getSubject());
        holder.tvHomeworkText.setText("Homework: " + model.getHomework());
        holder.tvHomeworkTimestamp.setText("Assigned on: " + model.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return homeworkList.size();
    }
}
