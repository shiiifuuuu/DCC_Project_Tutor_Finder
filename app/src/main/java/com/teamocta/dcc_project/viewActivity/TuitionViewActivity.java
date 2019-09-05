package com.teamocta.dcc_project.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityTuitionViewBinding;
import com.teamocta.dcc_project.pojo.HireService;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.teamocta.dcc_project.tutorActivity.TutorProfileActivity.currentTutor;

public class TuitionViewActivity extends AppCompatActivity {

    private ActivityTuitionViewBinding binding;
    private UserProfile tuitionProfile;
    private DatabaseReference databaseReference;
    private ArrayList<Float> ratingList;

    private Boolean requestExist = false;
    private String senderUid, receiverUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tuition_view);

        getTuitionInfo();
        init();
        gatherRating();
        setData();
    }

    private void getTuitionInfo() {
        tuitionProfile = (UserProfile) getIntent().getSerializableExtra("tuitionProfile");
    }

    private void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUid = tuitionProfile.getUid();
        ratingList = new ArrayList<>();
    }

    private void gatherRating() {
        DatabaseReference reference = databaseReference.child("hireRequest");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ratingList.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        HireService hireService = snapshot.getValue(HireService.class);

                        if(hireService.getSenderId().equals(tuitionProfile.getUid())){
                            if(hireService.getSenderRating()!=null){
                                float rating = Float.valueOf(hireService.getSenderRating());
                                ratingList.add(rating);
                            }
                        }
                        else if (hireService.getReceiverId().equals(tuitionProfile.getUid())){
                            if(hireService.getReceiverRating()!=null){
                                float rating = Float.valueOf(hireService.getReceiverRating());
                                ratingList.add(rating);
                            }
                        }
                    }
                }
                calculateAverage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void calculateAverage() {
        float sum = 0;
        for(int i = 0; i< ratingList.size(); i++){
            sum = sum + ratingList.get(i);
        }
        float avg = sum / ratingList.size();
        Support.toastMessageShort(String.valueOf(avg), TuitionViewActivity.this);
        updateDatabase(avg);
        binding.tuitionRating.setRating(avg);
    }

    private void updateDatabase(float avg) {
        DatabaseReference tutorRef = databaseReference.child("Student");
        Map<String, Object> rating = new HashMap<>();
        rating.put("rating", String.valueOf(avg));
        tutorRef.child(receiverUid).updateChildren(rating);
    }


    private void setData() {
        Glide.with(this).load(tuitionProfile.getImageUrl()).into(binding.ivProfilePic);
        binding.tvUserName.setText(tuitionProfile.getFirstName() + " " + tuitionProfile.getLastName());
        binding.tvUserClass.setText(tuitionProfile.getStudentClass());
        binding.tvDepartment.setText(tuitionProfile.getDepartment());
        binding.tvUserInstitute.setText(tuitionProfile.getInstitute());
        binding.tvUserGender.setText(tuitionProfile.getGender());
        binding.tvUserLocation.setText(tuitionProfile.getLocation());
        binding.tvUserMobile.setText(tuitionProfile.getMobile());
        binding.tvUserEmail.setText(tuitionProfile.getEmail());
        binding.tvUserAddress.setText(tuitionProfile.getStreetAddress() + ", "
                + tuitionProfile.getAreaAddress() + ", " + tuitionProfile.getZipCode() + ", Dhaka");
        binding.tvGuardianName.setText(tuitionProfile.getGuardianName());
        binding.tvGuardianMobile.setText(tuitionProfile.getGuardianMobile());
        binding.tvDaysPerWeek.setText(tuitionProfile.getDaysPerWeek());
        binding.tvSubjects.setText(tuitionProfile.getSubjects());
        binding.tvSalaryRange.setText(tuitionProfile.getSalaryRange() + " tk/month");
        binding.tvAdditionalInfo.setText(tuitionProfile.getAdditionalInfo());
    }

    public void btnBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void btnSendMessageClicked(View view) {
        Intent intent = new Intent(this, MessageViewActivity.class);
        intent.putExtra("userProfile", tuitionProfile);
        startActivity(intent);
    }

    public void btnHireClicked(View view) {
        checkingForMatchingRequest();
    }

    private void checkingForMatchingRequest() {

        DatabaseReference requestRef = databaseReference.child("hireRequest");
        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        HireService request = snapshot.getValue(HireService.class);
                        if((request.getSenderId().equals(senderUid) && request.getReceiverId().equals(receiverUid))){
                            requestExist = true;
                            Support.toastMessageShort("Sorry Request Exist", TuitionViewActivity.this);
                        }
                    }
                }
                sendRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendRequest(){

        if(!requestExist){
            try{
                DatabaseReference chatRef = databaseReference.child("hireRequest").push();

                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("senderId", senderUid);
                hashMap.put("receiverId", receiverUid);
                hashMap.put("senderImageUrl", currentTutor.getImageUrl());
                hashMap.put("senderName", currentTutor.getFirstName());
                hashMap.put("senderMobile", currentTutor.getMobile());
                hashMap.put("receiverImageUrl", tuitionProfile.getImageUrl());
                hashMap.put("receiverName", tuitionProfile.getFirstName());
                hashMap.put("receiverMobile", tuitionProfile.getMobile());
                hashMap.put("parentKey", chatRef.getKey());

                chatRef.setValue(hashMap);

                Support.toastMessageLong("Thank you for sending request.", TuitionViewActivity.this);
            }catch (Exception e){
                Support.toastMessageLong(e.getMessage(), TuitionViewActivity.this);
            }
        }
    }
}
