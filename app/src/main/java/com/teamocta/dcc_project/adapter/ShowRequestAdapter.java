package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.Chat;
import com.teamocta.dcc_project.pojo.HireService;
import com.teamocta.dcc_project.pojo.Support;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowRequestAdapter extends RecyclerView.Adapter<ShowRequestAdapter.ViewHolder> {

    private View view;
    private ArrayList<HireService> requSenderList;

    public ShowRequestAdapter(ArrayList<HireService> requSenderList) {
        this.requSenderList = requSenderList;
    }

    @NonNull
    @Override
    public ShowRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hire_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowRequestAdapter.ViewHolder holder, int position) {
        HireService currentRequest = requSenderList.get(position);
        holder.tvName.setText(currentRequest.getName());
        holder.tvMobile.setText(currentRequest.getMobile());
        Glide.with(view).load(currentRequest.getImageUrl()).into(holder.ivUserPic);


    }

    @Override
    public int getItemCount() {
        return requSenderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivUserPic;
        private TextView tvName;
        private TextView tvMobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);

            ivUserPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requSenderProfile();
                }
            });

            itemView.findViewById(R.id.btnAccept).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Support.toastMessageShort("btn Clicked", view.getContext());
                }
            });

            itemView.findViewById(R.id.btnReject).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Support.toastMessageShort("btn Clicked", view.getContext());

                }
            });
        }
    }

    private void requSenderProfile() {

    }
}
