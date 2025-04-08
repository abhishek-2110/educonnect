package com.example.fbauth.TeacherFiles;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;
import com.example.fbauth.Student.StudentModel;

import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder> {

    private final List<StudentModel> studentList;

    public HomeworkAdapter(List<StudentModel> studentList) {
        this.studentList = studentList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvEmail;
        EditText etHomework;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_student_name);
            tvId = itemView.findViewById(R.id.tv_student_id);
            tvEmail = itemView.findViewById(R.id.tv_student_email);
            etHomework = itemView.findViewById(R.id.et_homework);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assign_homework, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel student = studentList.get(position);

        holder.tvName.setText(student.getStudentName());
        holder.tvId.setText("ID: " + student.getStudentId());
        holder.tvEmail.setText("Email: " + student.getEmail());

        holder.etHomework.setText(student.getHomework());
        holder.etHomework.setTag(position);
        holder.etHomework.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int adapterPosition = (int) holder.etHomework.getTag();
                if (adapterPosition < studentList.size()) {
                    studentList.get(adapterPosition).setHomework(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
