package com.teamocta.dcc_project.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.teamocta.dcc_project.pojo.UserProfile;
import com.teamocta.dcc_project.viewActivity.ActiveSessionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.teamocta.dcc_project.studentActivity.StudentProfileActivity.currentStudent;
import static com.teamocta.dcc_project.tutorActivity.TutorProfileActivity.currentTutor;
import static com.teamocta.dcc_project.viewActivity.ActiveSessionActivity.databaseReference;
import static com.teamocta.dcc_project.viewActivity.ActiveSessionActivity.userUid;

public class ActiveSessionAdapter extends RecyclerView.Adapter<ActiveSessionAdapter.ViewHolder> {

    private View view;
    private Dialog rankDialog;
    private float userRating;
    private ArrayList<HireService> hiredList;
    private ArrayList<Float> ratingList = new ArrayList<>();

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

        if (currentProfile.getSenderId().equals(userUid)){
            holder.tvName.setText(currentProfile.getReceiverName());
            holder.tvMobile.setText(currentProfile.getReceiverMobile());
            Glide.with(view).load(currentProfile.getReceiverImageUrl()).into(holder.ivUserPic);

            if(currentProfile.getReceiverRating()!=null){
                holder.tvRating.setText(currentProfile.getReceiverRating() + "/5.0");

            }else{
                holder.tvRating.setText("In Progress");

            }

        }else if (currentProfile.getReceiverId().equals(userUid)){
            holder.tvName.setText(currentProfile.getSenderName());
            holder.tvMobile.setText(currentProfile.getSenderMobile());
            Glide.with(view).load(currentProfile.getSenderImageUrl()).into(holder.ivUserPic);

            if(currentProfile.getSenderRating()!=null){
                holder.tvRating.setText(currentProfile.getSenderRating() + "/5.0");

            } else{
                holder.tvRating.setText("In Progress");

            }
        }

        holder.provideRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                rankDialog = new Dialog(view.getContext(), R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                RatingBar ratingBar = rankDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        ratingBar.setRating(rating);
                        userRating = ratingBar.getRating();
                    }
                });
                final Button updateButton = rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentProfile.getSenderId().equals(userUid)){
                            updateHireRequest("receiverRating", String.valueOf(userRating), currentProfile.getParentKey(), currentProfile.getReceiverId(), currentProfile.getSenderId());
                            rankDialog.dismiss();
                        }else if(currentProfile.getReceiverId().equals(userUid)){
                            updateHireRequest("senderRating", String.valueOf(userRating), currentProfile.getParentKey(), currentProfile.getSenderId(), currentProfile.getReceiverId());
                            rankDialog.dismiss();
                        }

                        //Support.toastMessageShort(String.valueOf(userRating), view.getContext());
                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
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

    private void updateHireRequest(String key, String value, String pushKey, String userId, String oppositeId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(key, value);
        databaseReference.child("hireRequest").child(pushKey).updateChildren(userMap);

        setRatingToId(userId, value, oppositeId);
    }
    private void setRatingToId(String userId, String value, String oppositeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("rating", value);
        databaseReference.child("Ratings").child(userId).child(oppositeId).updateChildren(map);

        gatherRatings(userId);
    }

    private void gatherRatings(final String userId) {
        DatabaseReference userRef = databaseReference.child("Ratings").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ratingList.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserProfile user = snapshot.getValue(UserProfile.class);
                        ratingList.add(Float.valueOf(user.getRating()));
                    }
                }
                calculateAverage(userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calculateAverage(String userId) {
        float sum = 0;
        for(int i = 0; i< ratingList.size(); i++){
            sum = sum + ratingList.get(i);
        }
        float avg = sum / ratingList.size();
        Support.toastMessageShort(String.valueOf(avg), view.getContext());

        updateUserProfileRating(userId, avg);
    }

    private void updateUserProfileRating(String userId, float avg) {

        if (currentStudent.getUid() != null){
            Map<String, Object> map = new HashMap<>();
            map.put("rating", String.valueOf(avg));
            databaseReference.child("Tutor").child(userId).updateChildren(map);
        }else if (currentTutor.getUid() != null){
            Map<String, Object> map = new HashMap<>();
            map.put("rating", String.valueOf(avg));
            databaseReference.child("Student").child(userId).updateChildren(map);
        }

        //Support.toastMessageShort(currentStudent.getUid(), view.getContext());
        //Support.toastMessageShort(currentTutor.getUid(), view.getContext());
    }
}
