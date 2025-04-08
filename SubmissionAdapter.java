package com.example.fbauth.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.ViewHolder> {

    private List<SubmissionModel> submissionList;

    public SubmissionAdapter(List<SubmissionModel> submissionList) {
        this.submissionList = submissionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_submission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubmissionModel model = submissionList.get(position);

        holder.txtAssignmentId.setText("Assignment ID: " + model.getAssignmentId());
        holder.txtAnswer.setText("Answer: " + model.getAnswer());

        if (model.getMark() != null && !model.getMark().isEmpty()) {
            holder.txtMark.setText("Mark: " + model.getMark());
        } else {
            holder.txtMark.setText("Mark: Not yet graded");
        }

        if (model.getFeedback() != null && !model.getFeedback().isEmpty()) {
            holder.txtFeedback.setText("Feedback: " + model.getFeedback());
        } else {
            holder.txtFeedback.setText("Feedback: Pending");
        }
    }

    @Override
    public int getItemCount() {
        return submissionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAssignmentId, txtAnswer, txtMark, txtFeedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAssignmentId = itemView.findViewById(R.id.txt_assignment_id);
            txtAnswer = itemView.findViewById(R.id.txt_answer);
            txtMark = itemView.findViewById(R.id.txt_mark);
            txtFeedback = itemView.findViewById(R.id.txt_feedback);
        }
    }
}
