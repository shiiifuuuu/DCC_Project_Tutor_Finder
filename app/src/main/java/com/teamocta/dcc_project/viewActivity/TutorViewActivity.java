package com.teamocta.dcc_project.viewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityTutorViewBinding;
import com.teamocta.dcc_project.studentActivity.StudentSearchActivity;

public class TutorViewActivity extends AppCompatActivity {

    private ActivityTutorViewBinding binding;
    private String name, mobile, email, location, gender, experience, profession, institute;
    private String tuitionType, daysPerWeek, areaCovered, teachingSubjects, minimumSalary, tutorPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_view);

        getTutorInfo();
        setData();
    }

    private void getTutorInfo() {
        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("email");
        location = getIntent().getStringExtra("location");
        gender = getIntent().getStringExtra("gender");
        experience = getIntent().getStringExtra("experience");
        profession = getIntent().getStringExtra("profession");
        institute = getIntent().getStringExtra("institute");
        tuitionType = getIntent().getStringExtra("tuitionType");
        daysPerWeek = getIntent().getStringExtra("daysPerWeek");
        areaCovered = getIntent().getStringExtra("areaCovered");
        teachingSubjects = getIntent().getStringExtra("teachingSubjects");
        minimumSalary = getIntent().getStringExtra("minimumSalary");
        tutorPic = getIntent().getStringExtra("tutorPic");
    }
    private void setData() {
        binding.tvUserName.setText(name);
        binding.tvUserEmail.setText(email);
        binding.tvUserMobile.setText(mobile);
        binding.tvUserLocation.setText(location + " Dhaka.");
        binding.tvUserGender.setText(gender);
        binding.tvExperience.setText(experience);
        binding.tvProfession.setText(profession);
        binding.tvUserInstitute.setText(institute);

        binding.tvTuitionType.setText(tuitionType);
        if(binding.tvTuitionType.getText().toString().equals("Available")){
            binding.tvTuitionType.setTextColor(getResources().getColor(R.color.oneTimeTuitionTrue));
        }else{
            binding.tvTuitionType.setTextColor(getResources().getColor(R.color.oneTimeTuitionFalse));
        }

        binding.tvDaysPerWeek.setText(daysPerWeek);
        binding.tvAreaCovered.setText(areaCovered);
        binding.tvTeachingSubjects.setText(teachingSubjects);
        binding.tvMinimumSalary.setText(minimumSalary + "/=");
        Glide.with(this).load(tutorPic).into(binding.ivProfilePic);
    }

    public void btnBackClicked(View view) {
        startActivity(new Intent(this, StudentSearchActivity.class));
    }

    public void btnSendMessageClicked(View view) {

    }
}
