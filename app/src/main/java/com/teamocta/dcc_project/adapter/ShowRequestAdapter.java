package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.HireService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowRequestAdapter extends RecyclerView.Adapter<ShowRequestAdapter.ViewHolder> {

    public static String STATUS_ACCEPTED = "Accepted";
    public static String STATUS_REJECTED = "Rejected";

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
    public void onBindViewHolder(@NonNull final ShowRequestAdapter.ViewHolder holder, int position) {
        final HireService currentRequest = requSenderList.get(position);

        holder.tvName.setText(currentRequest.getSenderName());
        holder.tvMobile.setText(currentRequest.getSenderMobile());
        Glide.with(view).load(currentRequest.getSenderImageUrl()).into(holder.ivUserPic);

        if(currentRequest.getStatus()!=null){
            holder.tvResult.setText("Request " + currentRequest.getStatus());

            holder.btnAccept.setEnabled(false);
            holder.btnReject.setEnabled(false);
            holder.btnAccept.setBackgroundColor(holder.btnAccept.getResources().getColor(R.color.gray));
            holder.btnReject.setBackgroundColor(holder.btnReject.getResources().getColor(R.color.gray));
        }

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.tvResult.setText("Request accepted");
                holder.btnAccept.setEnabled(false);
                holder.btnAccept.setBackgroundColor(holder.btnAccept.getResources().getColor(R.color.gray));

                updateDatabase(STATUS_ACCEPTED, currentRequest.getParentKey());
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.tvResult.setText("Request rejected");
                holder.btnReject.setEnabled(false);
                holder.btnReject.setBackgroundColor(holder.btnReject.getResources().getColor(R.color.gray));

                updateDatabase(STATUS_REJECTED, currentRequest.getParentKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return requSenderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivUserPic;
        private TextView tvName;
        private TextView tvMobile;
        private TextView tvResult;
        private Button btnAccept;
        private Button btnReject;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvResult = itemView.findViewById(R.id.tvResult);

            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    private void updateDatabase(String value, String key) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("status", value);
        databaseReference.child("hireRequest").child(key).updateChildren(userMap);
    }
}
