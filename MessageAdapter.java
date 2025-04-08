package com.example.fbauth.TeacherFiles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbauth.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<TeacherMessageModel> messages = new ArrayList<>();

    public void setMessages(List<TeacherMessageModel> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        TeacherMessageModel model = messages.get(position);
        holder.tvFromId.setText("From: " + model.fromId);
        holder.tvMessage.setText("Message: " + model.message);
        holder.tvReply.setText("Reply: " + (model.reply == null || model.reply.isEmpty() ? "(No reply)" : model.reply));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvFromId, tvMessage, tvReply;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFromId = itemView.findViewById(R.id.tv_from_id);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvReply = itemView.findViewById(R.id.tv_reply);
        }
    }
}
