package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.HireService;
import com.teamocta.dcc_project.pojo.Support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveSessionAdapter extends RecyclerView.Adapter<ActiveSessionAdapter.ViewHolder> {

    private View view;
    private ArrayList<HireService> hiredList;

    private DatabaseReference databaseReference;
    private HireService hiredProfile;
    private String senderId, receiverId, key;

    public ActiveSessionAdapter(ArrayList<HireService> hiredList) {
        this.hiredList = hiredList;
    }

    @NonNull
    @Override
    public ActiveSessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_active_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveSessionAdapter.ViewHolder holder, int position) {
        final HireService currentProfile = hiredList.get(position);
        holder.tvName.setText(currentProfile.getName());
        holder.tvMobile.setText(currentProfile.getMobile());
        Glide.with(view).load(currentProfile.getImageUrl()).into(holder.ivUserPic);

        if(currentProfile.getRating()!=null){
            holder.tvRating.setText(currentProfile.getRating());

        }else{
            holder.tvRating.setText("In Progress");

        }

        holder.provideRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Support.toastMessageShort("Clicked", view.getContext());

                senderId = currentProfile.getSender();
                receiverId = currentProfile.getReceiver();

                getKey(senderId, receiverId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hiredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvMobile;
        private CircleImageView ivUserPic;
        private TextView tvRating;
        private LinearLayout provideRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvRating = itemView.findViewById(R.id.tvRating);
            provideRating = itemView.findViewById(R.id.actvSesnLayout);
        }
    }


    private String getKey(final String senderId, final String receiverId){
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("hireRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        hiredProfile = snapshot.getValue(HireService.class);
                        if(hiredProfile.getSender().equals(senderId) && hiredProfile.getReceiver().equals(receiverId)){
                            key = snapshot.getKey();
                        }
                    }
                }
                //updateDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return key;
    }
    private void updateDatabase(String value) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("rating", value);
        databaseReference.child("hireRequest").child(key).updateChildren(userMap);
    }
}
