package com.example.fbauth.TeacherFiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private final List<String> announcements;

    public AnnouncementAdapter(List<String> announcements) {
        this.announcements = announcements;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAnnouncement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnnouncement = itemView.findViewById(R.id.tv_announcement);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAnnouncement.setText(announcements.get(position));
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }
}
