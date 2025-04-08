package com.example.fbauth.ParentFiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParentHomeworkAdapter extends RecyclerView.Adapter<ParentHomeworkAdapter.ViewHolder> {

    private List<ParentHomeworkModel> homeworkList;

    public ParentHomeworkAdapter(List<ParentHomeworkModel> homeworkList) {
        this.homeworkList = homeworkList;
    }

    public void setHomeworkList(List<ParentHomeworkModel> list) {
        this.homeworkList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParentHomeworkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parent_homework, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentHomeworkAdapter.ViewHolder holder, int position) {
        ParentHomeworkModel model = homeworkList.get(position);
        holder.txtSubject.setText("Subject: " + model.getSubject());
        holder.txtHomework.setText("Homework: " + model.getHomework());

        String formattedDate = formatTimestamp(model.getTimestamp());
        holder.txtDate.setText("Date: " + formattedDate);
    }

    @Override
    public int getItemCount() {
        return homeworkList != null ? homeworkList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubject, txtHomework, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject = itemView.findViewById(R.id.txt_subject);
            txtHomework = itemView.findViewById(R.id.txt_homework);
            txtDate = itemView.findViewById(R.id.txt_date);
        }
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
