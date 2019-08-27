package com.teamocta.dcc_project.viewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityTutorViewBinding;
import com.teamocta.dcc_project.pojo.UserProfile;
import com.teamocta.dcc_project.studentActivity.StudentSearchActivity;

public class TutorViewActivity extends AppCompatActivity{

    private ActivityTutorViewBinding binding;
    private UserProfile tutorProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_view);

        getTutorInfo();
        setData();
    }

    private void getTutorInfo() {
        tutorProfile = (UserProfile) getIntent().getSerializableExtra("tutorProfile");
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
        startActivity(new Intent(this, StudentSearchActivity.class));
        finish();
    }

    public void btnSendMessageClicked(View view) {
        Intent intent = new Intent(this, MessageViewActivity.class);
        intent.putExtra("userProfile", tutorProfile);
        startActivity(intent);
    }
}
