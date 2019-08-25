package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.TutorProfile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class studentMessageAdapter extends RecyclerView.Adapter<studentMessageAdapter.ViewHolder> {
    private View view;
    private ArrayList<TutorProfile> tutorList;
    public studentMessageAdapter(ArrayList<TutorProfile> tutorList) {
        this.tutorList = tutorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_message_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull studentMessageAdapter.ViewHolder holder, int position) {
        TutorProfile users = tutorList.get(position);
        holder.tvName.setText(users.getFirstName() + " " + users.getLastName());
        Glide.with(view).load(users.getImageUrl()).into(holder.ivTutorPic);
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView ivTutorPic;
        private TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTutorPic = itemView.findViewById(R.id.ivTutorPic);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
