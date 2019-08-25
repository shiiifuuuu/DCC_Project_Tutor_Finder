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
    private String tutorUid, name, mobile, email, location, gender, experience, profession, institute;
    private String tuitionType, daysPerWeek, areaCovered, teachingSubjects, minimumSalary, tutorPic,
            tutorRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_view);

        getTutorInfo();
        setData();
    }

    private String getIntentValue(String keyValue) {
        return getIntent().getStringExtra(keyValue);
    }
    private void getTutorInfo() {
        tutorUid = getIntentValue("tutorUid");
        name = getIntentValue("name");
        mobile = getIntentValue("mobile");
        email = getIntentValue("email");
        location = getIntentValue("location");
        gender = getIntentValue("gender");
        experience = getIntentValue("experience");
        profession = getIntentValue("profession");
        institute = getIntentValue("institute");
        tuitionType = getIntentValue("tuitionType");
        daysPerWeek = getIntentValue("daysPerWeek");
        areaCovered = getIntentValue("areaCovered");
        teachingSubjects = getIntentValue("teachingSubjects");
        minimumSalary = getIntentValue("minimumSalary");
        tutorPic = getIntentValue("tutorPic");
        tutorRating = getIntentValue("tutorRating");
    }
    private void setData() {
        Glide.with(this).load(tutorPic).into(binding.ivProfilePic);
        binding.tvUserName.setText(name);
        //binding.tutorRating.setRating(Float.parseFloat(tutorRating));
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
    }

    public void btnBackClicked(View view) {
        startActivity(new Intent(this, StudentSearchActivity.class));
        finish();
    }

    public void btnSendMessageClicked(View view) {
        Intent intent = new Intent(this, MessageViewActivity.class);
        intent.putExtra("msgReceiverUid", tutorUid);
        intent.putExtra("receiverName", name);
        startActivity(intent);
    }
}
