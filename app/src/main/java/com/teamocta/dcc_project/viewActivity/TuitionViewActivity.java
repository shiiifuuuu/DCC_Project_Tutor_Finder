package com.teamocta.dcc_project.viewActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityTuitionViewBinding;
import com.teamocta.dcc_project.tutorActivity.TutorSearchActivity;

public class TuitionViewActivity extends AppCompatActivity {

    private ActivityTuitionViewBinding binding;
    private String studentUid, studentPic, name, studentClass, department, institute, gender, location, mobile, email;
    private String streetAddress, areaAddress, zipCode, guardianName, guardianMobile,
            daysPerWeek, subjects, salaryRange, additionalInfo, tuitionRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tuition_view);

        getTuitionInfo();
        setData();
    }

    private String getIntentValue(String keyValue) {
        return getIntent().getStringExtra(keyValue);
    }
    private void getTuitionInfo() {

        studentUid = getIntentValue("studentUid");
        studentPic = getIntentValue("studentPic");
        name = getIntentValue("name");
        tuitionRating = getIntentValue("tuitionRating");
        studentClass = getIntentValue("studentClass");
        department = getIntentValue("department");
        institute = getIntentValue("institute");
        gender = getIntentValue("gender");
        location = getIntentValue("location");
        mobile = getIntentValue("mobile");
        email = getIntentValue("email");

        streetAddress = getIntentValue("streetAddress");
        areaAddress = getIntentValue("areaAddress");
        zipCode = getIntentValue("zipCode");

        guardianName = getIntentValue("guardianName");
        guardianMobile = getIntentValue("guardianMobile");

        daysPerWeek = getIntentValue("daysPerWeek");
        subjects = getIntentValue("subjects");
        salaryRange = getIntentValue("salaryRange");
        additionalInfo = getIntentValue("additionalInfo");
    }
    private void setData() {
        Glide.with(this).load(studentPic).into(binding.ivProfilePic);
        binding.tvUserName.setText(name);
        //binding.tuitionRating.setRating(Float.parseFloat(tuitionRating));
        binding.tvUserClass.setText(studentClass);
        binding.tvDepartment.setText(department);
        binding.tvUserInstitute.setText(institute);
        binding.tvUserGender.setText(gender);
        binding.tvUserLocation.setText(location);
        binding.tvUserMobile.setText(mobile);
        binding.tvUserEmail.setText(email);
        binding.tvUserAddress.setText(streetAddress + ", " + areaAddress + ", " + zipCode + ", Dhaka");
        binding.tvGuardianName.setText(guardianName);
        binding.tvGuardianMobile.setText(guardianMobile);
        binding.tvDaysPerWeek.setText(daysPerWeek);
        binding.tvSubjects.setText(subjects);
        binding.tvSalaryRange.setText(salaryRange + " tk/month");
        binding.tvAdditionalInfo.setText(additionalInfo);
    }

    public void btnBackClicked(View view) {
        startActivity(new Intent(this, TutorSearchActivity.class));
        finish();
    }

    public void btnSendMessageClicked(View view) {
        Intent intent = new Intent(this, MessageViewActivity.class);
        intent.putExtra("msgReceiverUid", studentUid);
        intent.putExtra("receiverName", name);
        startActivity(intent);
    }
}
