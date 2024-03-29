package com.teamocta.dcc_project.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.teamocta.dcc_project.googleMapActivity.TuitionMapViewActivity;
import com.teamocta.dcc_project.pojo.HireService;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.HashMap;
import java.util.Map;

import static com.teamocta.dcc_project.tutorActivity.TutorProfileActivity.currentTutor;

public class TuitionViewActivity extends AppCompatActivity {

    private ActivityTuitionViewBinding binding;
    private ActionBar actionBar;

    private UserProfile tuitionProfile;
    private DatabaseReference databaseReference;

    private Boolean requestExist = false;
    private String senderUid, receiverUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tuition_view);

        getTuitionInfo();
        setActionBar();
        init();
        setData();
    }

    private void getTuitionInfo() {
        tuitionProfile = (UserProfile) getIntent().getSerializableExtra("tuitionProfile");
    }

    private void setActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Tuition Profile");
    }
    private void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUid = tuitionProfile.getUid();
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

        if(tuitionProfile.getRating()!=null){
            binding.tuitionRating.setRating(Float.valueOf(tuitionProfile.getRating()));

        }
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
                            Support.toastMessageShort("You already sent offer to this person!", TuitionViewActivity.this);
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

    public void mobileNmbrClicked(View view) {
        try {
            String uri = "tel:" + tuitionProfile.getMobile() ;
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(uri));
            startActivity(callIntent);
        }catch (Exception e){
            Support.toastMessageShort(e.getMessage(), TuitionViewActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void ShowMapLocation(View view) {
        if(tuitionProfile.getLatitude()!=null && tuitionProfile.getLongitude() != null){
            Intent intent = new Intent(getApplicationContext(), TuitionMapViewActivity.class);
            intent.putExtra("studentProfile", tuitionProfile);
            startActivity(intent);
        }else{
            Support.toastMessageLong("Map location not found !", getApplicationContext());
        }
    }
}
