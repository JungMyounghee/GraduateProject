package com.example.myhomefood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhomefood.UserDirectory.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH>  {
    private Context context;

    ArrayList<User> list = new ArrayList<>();

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.UserVH onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);
        return new UserAdapter.UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserVH holder, int position) {
        User user = list.get(holder.getBindingAdapterPosition());

        //이름
        //holder.nameText.setText(user.getSetting_list());
        ArrayList<String> newList = new ArrayList<>();

        newList = user.getSetting_list();

        System.out.println("못 먹는 재료 : " + newList.toString());

        for(int i=0; i<newList.size(); i++) {
            holder.nameText.setText(newList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserVH extends RecyclerView.ViewHolder {
        TextView nameText;
        CardView cardView;

        public UserVH(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_text);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
