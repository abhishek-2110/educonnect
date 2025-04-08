package com.example.fbauth.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.util.List;

public class StudentAnnouncementViewAdapter extends RecyclerView.Adapter<StudentAnnouncementViewAdapter.ViewHolder> {

    private final List<StudentAnnouncementModel> announcementList;

    public StudentAnnouncementViewAdapter(List<StudentAnnouncementModel> announcementList) {
        this.announcementList = announcementList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAnnouncementText, tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnnouncementText = itemView.findViewById(R.id.tvAnnouncementText);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }

    @NonNull
    @Override
    public StudentAnnouncementViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAnnouncementViewAdapter.ViewHolder holder, int position) {
        StudentAnnouncementModel model = announcementList.get(position);
        holder.tvAnnouncementText.setText(model.getText());
        holder.tvTimestamp.setText(model.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }
}
