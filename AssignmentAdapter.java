package com.example.fbauth.TeacherFiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {

    private List<AssignmentModel> assignmentList;

    public AssignmentAdapter(List<AssignmentModel> assignmentList) {
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssignmentModel assignment = assignmentList.get(position);
        if (assignment != null) {
            holder.txtTitle.setText("Title: " + assignment.getTitle());
            holder.txtDesc.setText("Description: " + assignment.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return assignmentList != null ? assignmentList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_assignment_title);
            txtDesc = itemView.findViewById(R.id.txt_assignment_desc);
        }
    }
}
