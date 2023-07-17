package com.example.algorithm_visualizer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.algorithm_visualizer.Code_Snippet;
import com.example.algorithm_visualizer.Models.AlgorithmsModelClass;
import com.example.algorithm_visualizer.R;

import java.util.List;

public class AlgoAdapter extends RecyclerView.Adapter<AlgoAdapter.ViewHolder> {
    private List<AlgorithmsModelClass> AlgoList;
    private Context context;

    public void setFilteredList(List<AlgorithmsModelClass> filteredList){
        this.AlgoList = filteredList;
        notifyDataSetChanged();
    }
    public AlgoAdapter(List<AlgorithmsModelClass> algoList, Context context) {
        this.AlgoList = algoList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlgoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AlgoAdapter.ViewHolder holder, int position) {

        String Algorithm = AlgoList.get(position).getAlgoName();
        holder.setData(Algorithm);

        holder.AlgoName.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim4));

        holder.RecyclerItem.setOnClickListener(v -> {
        Intent intent = new Intent(context, Code_Snippet.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("itemPosition", position);
        context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return AlgoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView AlgoName;
        private ConstraintLayout RecyclerItem;

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
