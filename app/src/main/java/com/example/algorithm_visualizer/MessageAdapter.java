package com.example.algorithm_visualizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewholder> {

    private List<MessageModelClass> message;
    private static final int usermsg = 1 , botmsg = 0;

    public MessageAdapter(List<MessageModelClass> message) {
        this.message = message;
    }

    @NonNull
    @Override
    public MessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == usermsg){
            view = layoutInflater.inflate(R.layout.item_message_user,parent,false);
        } else {
            view = layoutInflater.inflate(R.layout.item_message_bot,parent,false);
        }
        return new MessageViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewholder holder, int position) {
        MessageModelClass msg = message.get(position);
        holder.bind(msg);
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public int getItemViewType(int position){
        MessageModelClass msg = message.get(position);
        return msg.isSentByUser()?usermsg:botmsg;
    }
    public class MessageViewholder extends RecyclerView.ViewHolder{
        TextView mText;
        public MessageViewholder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.usermessage);
        }
        public void bind(MessageModelClass message){
            if (message.isSentByUser()){
                mText = itemView.findViewById(R.id.usermessage);
                mText.setText(message.getmText());
            } else {
                mText = itemView.findViewById(R.id.botmessage);
                mText.setText(message.getmText());
            }
        }
    }
}
