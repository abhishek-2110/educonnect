package com.example.fbauth.TeacherFiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;
import com.example.fbauth.Student.StudentModel;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<StudentModel> students;

    public AttendanceAdapter(List<StudentModel> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel student = students.get(position);

        // Set student details
        holder.txtStudentName.setText(student.getStudentName());
        holder.txtStudentId.setText("ID: " + student.getStudentId());
        holder.txtEmail.setText("Email: " + student.getEmail());
        holder.txtPhone.setText("Phone: " + student.getPhone());

        // Handle attendance selection

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public List<StudentModel> getMarkedStudents() {
        return students;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentName, txtStudentId, txtEmail, txtPhone;
        RadioGroup radioGroup;
        RadioButton radioPresent, radioAbsent, radioLate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentName = itemView.findViewById(R.id.txt_name);
            txtStudentId = itemView.findViewById(R.id.txt_id);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            radioGroup = itemView.findViewById(R.id.radio_group);
            radioPresent = itemView.findViewById(R.id.radio_present);
            radioAbsent = itemView.findViewById(R.id.radio_absent);
            radioLate = itemView.findViewById(R.id.radio_late);
        }
    }
}
