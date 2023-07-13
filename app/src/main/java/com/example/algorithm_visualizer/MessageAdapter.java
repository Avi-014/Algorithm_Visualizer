package com.example.algorithm_visualizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewholder> {

    private final List<MessageModelClass> messageList;
    public MessageAdapter(List<MessageModelClass> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.messages_item, parent,false);
        return new MessageViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewholder holder, int position) {
        MessageModelClass msg = messageList.get(position);
        if (msg.getSentBy().equals(MessageModelClass.SENT_BY_ME)){
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
