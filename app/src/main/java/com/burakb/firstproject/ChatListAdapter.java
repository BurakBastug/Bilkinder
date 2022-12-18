package com.burakb.firstproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> implements ChildrenStatusItemListener{
    Context context;
    ArrayList<Child> chatList = new ArrayList<>();
    ChildrenStatusItemListener itemListener;

    public ChatListAdapter(Context context, ArrayList<Child> list,ChildrenStatusItemListener itemListener){
        this.context = context;
        this.chatList = list;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //holder.image.setImageResource(list.get(position).getImg());
        holder.name.setText(chatList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onItemClick(chatList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public void onItemClick(Child child) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView image;
        TextView name;
        LinearLayout parent_layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //image = itemView.findViewById(R.id.event_image);
            name = itemView.findViewById(R.id.parent_name);
            parent_layout = itemView.findViewById(R.id.chat_parent_layout);
        }
    }
}
