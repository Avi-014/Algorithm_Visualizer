package com.example.algorithm_visualizer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ModelClass> AlgoList;
    private Context context;

    public void setFilteredList(List<ModelClass> filteredList){
        this.AlgoList = filteredList;
        notifyDataSetChanged();
    }
    public Adapter(List<ModelClass> algoList, Context context) {
        this.AlgoList = algoList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
     String Algorithm = AlgoList.get(position).getAlgoName();
     holder.setData(Algorithm);

        holder.AlgoName.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim4));
        if (position == 0){
            holder.RecyclerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatBot.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
        }
        if (position == 1) {
            holder.RecyclerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BinarySearch.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return AlgoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView AlgoName;
        private CardView RecyclerItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            RecyclerItem = itemView.findViewById(R.id.RecyclerItem);
            AlgoName = itemView.findViewById(R.id.AlgoName);
        }

        public void setData(String algorithm) {
            AlgoName.setText(algorithm);
        }
    }
}
