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

public class ParentAttendanceAdapter extends RecyclerView.Adapter<ParentAttendanceAdapter.ViewHolder> {

    private List<ParentAttendanceModel> attendanceList;

    public ParentAttendanceAdapter(List<ParentAttendanceModel> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parent_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParentAttendanceModel model = attendanceList.get(position);
        holder.txtSubject.setText("Subject: " + model.getSubject());
        holder.txtStatus.setText("Status: " + model.getAttendanceStatus());

        String formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(new Date(model.getTimestamp()));
        holder.txtDate.setText("Date: " + formattedDate);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubject, txtStatus, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject = itemView.findViewById(R.id.txt_subject);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtDate = itemView.findViewById(R.id.txt_date);
        }
    }
}
