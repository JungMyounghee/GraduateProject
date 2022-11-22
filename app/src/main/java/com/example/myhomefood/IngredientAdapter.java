package com.example.myhomefood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.Holder> {
    private ArrayList<IngredientInfo> items;

    public IngredientAdapter(ArrayList<IngredientInfo> items) {
        this.items = items;
    }

    public interface OnItemClickEventListener {
        void onItemClick(View a_view, int a_position);
    }

    private OnItemClickEventListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        mItemClickListener = a_listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new Holder(v, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        IngredientInfo item = items.get(position);
        holder.item.setText(item.getProcess());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView item;

        public Holder(@NonNull View itemView, final OnItemClickEventListener a_itemClickListener) {
            super(itemView);

            item = itemView.findViewById(R.id.item);

            //click listener 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View a_view) {
                    final int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        a_itemClickListener.onItemClick(a_view, position);
                    }
                }
            });

        }
    }
}

class IngredientInfo {
    private String process;

    public IngredientInfo() {}
    public IngredientInfo(String process) {
        this.process = process;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
