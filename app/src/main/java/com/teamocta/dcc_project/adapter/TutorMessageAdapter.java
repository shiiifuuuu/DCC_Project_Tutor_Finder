package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorMessageAdapter extends RecyclerView.Adapter<TutorMessageAdapter.ViewHolder> {

    private View view;
    private ArrayList<UserProfile> tuitionList;
    private OnMessageClickListener onMessageClickListener;


    public TutorMessageAdapter(ArrayList<UserProfile> tuitionList, OnMessageClickListener onMessageClickListener) {
        this.tuitionList = tuitionList;
        this.onMessageClickListener = onMessageClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_message_profile, parent, false);
        return new ViewHolder(view, onMessageClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorMessageAdapter.ViewHolder holder, int position) {
        UserProfile users = tuitionList.get(position);
        holder.tvName.setText(users.getFirstName() + " " + users.getLastName());
        Glide.with(view).load(users.getImageUrl()).into(holder.ivUserPic);
    }

    @Override
    public int getItemCount() {
        return tuitionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnMessageClickListener onMessageClickListener;

        private CircleImageView ivUserPic;
        private TextView tvName;

        public ViewHolder(@NonNull View itemView, OnMessageClickListener onMessageClickListener) {
            super(itemView);
            this.onMessageClickListener = onMessageClickListener;


            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            onMessageClickListener.onMessageClick(getAdapterPosition());
        }
    }

    public interface OnMessageClickListener{
        void onMessageClick(int position);
    }
}
