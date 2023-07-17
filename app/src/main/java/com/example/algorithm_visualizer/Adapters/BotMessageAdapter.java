package com.example.algorithm_visualizer.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.algorithm_visualizer.Models.BotMessageModelClass;
import com.example.algorithm_visualizer.R;

import java.util.List;

public class BotMessageAdapter extends RecyclerView.Adapter<BotMessageAdapter.MessageViewholder> {
    private final List<BotMessageModelClass> messageList;
    public BotMessageAdapter(List<BotMessageModelClass> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_item,null);
        return new MessageViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewholder holder, int position) {
        BotMessageModelClass msg = messageList.get(position);
        if (msg.getSentBy().equals(BotMessageModelClass.SENT_BY_ME)){
            holder.botMessageLayout.setVisibility(View.GONE);
            holder.userMessageLayout.setVisibility(View.VISIBLE);
            holder.userMessage.setText(msg.getMessage());
        } else {
            holder.userMessageLayout.setVisibility(View.GONE);
            holder.botMessageLayout.setVisibility(View.VISIBLE);
            holder.botMessage.setText(msg.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewholder extends RecyclerView.ViewHolder{
        LinearLayout botMessageLayout , userMessageLayout;
        TextView botMessage , userMessage;
        public MessageViewholder(@NonNull View itemView) {
            super(itemView);
            botMessageLayout = itemView.findViewById(R.id.botmessageLayout);
            userMessageLayout = itemView.findViewById(R.id.usermessageLayout);
            botMessage = itemView.findViewById(R.id.botmessage);
            userMessage = itemView.findViewById(R.id.usermessage);
        }
    }
}
