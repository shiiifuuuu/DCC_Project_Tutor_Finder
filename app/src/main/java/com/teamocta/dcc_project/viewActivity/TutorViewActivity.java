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
import com.teamocta.dcc_project.databinding.ActivityTutorViewBinding;
import com.teamocta.dcc_project.pojo.HireService;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.HashMap;
import java.util.Map;

import static com.teamocta.dcc_project.studentActivity.StudentProfileActivity.currentStudent;

public class TutorViewActivity extends AppCompatActivity{

    private ActivityTutorViewBinding binding;
    private UserProfile tutorProfile;
    private DatabaseReference databaseReference;

    private Boolean requestExist = false;
    private String senderUid, receiverUid, senderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_view);

        getIntentExtras();
        init();
        setData();
    }

    private void getIntentExtras() {
        tutorProfile = (UserProfile) getIntent().getSerializableExtra("tutorProfile");
    }
    private void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUid = tutorProfile.getUid();
        senderImage = currentStudent.getImageUrl();
    }
    private void setData() {
        Glide.with(this).load(tutorProfile.getImageUrl()).into(binding.ivProfilePic);
        binding.tvUserName.setText(tutorProfile.getFirstName() + " " + tutorProfile.getLastName());
        binding.tvUserEmail.setText(tutorProfile.getEmail());
        binding.tvUserMobile.setText(tutorProfile.getMobile());
        binding.tvUserLocation.setText(tutorProfile.getLocation() + " Dhaka.");
        binding.tvUserGender.setText(tutorProfile.getGender());
        binding.tvExperience.setText(tutorProfile.getExperience());
        binding.tvProfession.setText(tutorProfile.getProfession());
        binding.tvUserInstitute.setText(tutorProfile.getInstitute());

        binding.tvTuitionType.setText(tutorProfile.getTuitionType());
        if(binding.tvTuitionType.getText().toString().equals("Available")){
            binding.tvTuitionType.setTextColor(getResources().getColor(R.color.oneTimeTuitionTrue));
        }else{
            binding.tvTuitionType.setTextColor(getResources().getColor(R.color.oneTimeTuitionFalse));
        }

        binding.tvDaysPerWeek.setText(tutorProfile.getDaysPerWeek());
        binding.tvAreaCovered.setText(tutorProfile.getAreaCovered());
        binding.tvTeachingSubjects.setText(tutorProfile.getTeachingSubjects());
        binding.tvMinimumSalary.setText(tutorProfile.getMinimumSalary() + "/=");
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
        intent.putExtra("userProfile", tutorProfile);
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
                        if((request.getSender().equals(senderUid) && request.getReceiver().equals(receiverUid))){
                            requestExist = true;
                            Support.toastMessageShort("Sorry Request Exist", TutorViewActivity.this);
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
                hashMap.put("sender", senderUid);
                hashMap.put("receiver", receiverUid);
                hashMap.put("imageUrl", senderImage);
                hashMap.put("name", currentStudent.getFirstName());
                hashMap.put("mobile", currentStudent.getMobile());
                hashMap.put("parentKey", chatRef.getKey());

                chatRef.setValue(hashMap);

                Support.toastMessageLong("Thank you for sending request.", TutorViewActivity.this);
            }catch (Exception e){
                Support.toastMessageLong(e.getMessage(), TutorViewActivity.this);
            }
        }
    }
}
