package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.Chat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private ArrayList<Chat> chatList;
    private  View view;

    private String userUid;

    public ChatListAdapter(ArrayList<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_message_right, parent, false);
            return new ViewHolder(view);

        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_message_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {
        Chat currentChat = chatList.get(position);
        holder.tvShowMessage.setText(currentChat.getMsg());
        Glide.with(view).load(currentChat.getImageUrl()).into(holder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShowMessage;
        private CircleImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShowMessage = itemView.findViewById(R.id.tvShowMessage);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }
    }

    @Override
    public int getItemViewType(int position) {
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (chatList.get(position).getSender().equals(userUid)){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
