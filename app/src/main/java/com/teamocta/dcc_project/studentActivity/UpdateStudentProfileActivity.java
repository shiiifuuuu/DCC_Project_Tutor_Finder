package com.teamocta.dcc_project.studentActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityUpdateStudentProfileBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class UpdateStudentProfileActivity extends AppCompatActivity {

    private ActivityUpdateStudentProfileBinding binding;
    //private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String uid;
    private AwesomeValidation mAwesomeValidation;

    private String firstName,lastName,mobile,location,gender,studentClass, department,institute;
    private String streetAddress,areaAddress,zipCode,guardianName,guardianMobile;
    private String daysPerWeek, subjects, salaryRange, additionalInfo;

    private String[] subjectListItems;
    private boolean[] subjectCheckedItems;
    private ArrayList<Integer> subjectUserItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_student_profile);

        init();
        getIntentExtras();
        setCurrentField();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        mAwesomeValidation = new AwesomeValidation(BASIC);

        subjectUserItems = new ArrayList<>();
    }

    //getting values from previous activity
    private void getIntentExtras() {
        firstName = getIntent().getExtras().getString("firstName");
        lastName = getIntent().getExtras().getString("lastName");
        mobile = getIntent().getExtras().getString("mobile");
        studentClass = getIntent().getExtras().getString("studentClass");
        institute = getIntent().getExtras().getString("institute");
        department = getIntent().getExtras().getString("department");
        gender = getIntent().getStringExtra("gender");
        location = getIntent().getStringExtra("location");
        streetAddress = getIntent().getStringExtra("streetAddress");
        areaAddress = getIntent().getStringExtra("areaAddress");
        zipCode = getIntent().getStringExtra("zipCode");
        guardianName = getIntent().getExtras().getString("guardianName");
        guardianMobile = getIntent().getExtras().getString("guardianMobile");

        daysPerWeek = getIntent().getStringExtra("daysPerWeek");
        subjects = getIntent().getStringExtra("subjects");
        salaryRange = getIntent().getStringExtra("salaryRange");
        additionalInfo = getIntent().getStringExtra("additionalInfo");
    }
    private void setCurrentField() {
        binding.etFirstName.setText(firstName);
        binding.etLastName.setText(lastName);
        binding.etMobile.setText(mobile);
        binding.etClass.setText(studentClass);
        binding.etInstitute.setText(institute);
        binding.etDept.setText(department);
        binding.tvGender.setText(gender);
        binding.tvLocation.setText(location);
        binding.etStreet.setText(streetAddress);
        binding.etArea.setText(areaAddress);
        binding.etZipCode.setText(zipCode);
        binding.etGuardianName.setText(guardianName);
        binding.etGuardinaMobile.setText(guardianMobile);

        binding.tvDaysPerWeek.setText(daysPerWeek);
        binding.tvSubjects.setText(subjects);
        binding.etSalaryRange.setText(salaryRange);
        binding.etAdditionalInfo.setText(additionalInfo);
    }

    //Database Update
    private void validationCheck() {
        mAwesomeValidation.addValidation(this, R.id.etFirstName, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.etLastName, "[a-zA-Z\\s]+", R.string.err_name);
        if(binding.etMobile.getText().toString().length() == 0){
            binding.etMobile.setError("Mobile number can't be empty");
        }
        if(binding.etClass.getText().toString().length() == 0){
            binding.etClass.setError("Field can't be empty!");
        }
        if(binding.etInstitute.getText().toString().length() == 0){
            binding.etInstitute.setError("Field can't be empty!");
        }
        if(binding.etStreet.getText().toString().length() == 0){
            binding.etStreet.setError("Field can't be empty!");
        }
        if(binding.etArea.getText().toString().length() == 0){
            binding.etArea.setError("Field can't be empty!");
        }
        if(binding.etZipCode.getText().toString().length() == 0){
            binding.etZipCode.setError("Field can't be empty!");
        }
    }
    public void btnSaveProfileClicked(View view) {
        validationCheck();
        if(mAwesomeValidation.validate()){
            showAlertDialog("Updating user profile...");
            updateDatabase();
        }
    }
    private void updateDatabase() {
        initializeInputData();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("mobile", mobile);
        userMap.put("location", location);
        userMap.put("gender", gender);
        userMap.put("studentClass", studentClass);
        userMap.put("department", department);
        userMap.put("institute", institute);
        userMap.put("streetAddress", streetAddress);
        userMap.put("areaAddress", areaAddress);
        userMap.put("zipCode", zipCode);
        userMap.put("guardianName", guardianName);
        userMap.put("guardianMobile", guardianMobile);

        userMap.put("daysPerWeek", daysPerWeek);
        userMap.put("salaryRange", salaryRange);
        userMap.put("subjects", subjects);
        userMap.put("additionalInfo", additionalInfo);

        DatabaseReference studentReference = databaseReference.child("Student");
        studentReference.child(uid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    alertDialog.cancel();
                    toastMessageLong("Profile Updated Successfully.");
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.cancel();
                toastMessageLong(e.getMessage());
            }
        });
    }
    private void initializeInputData() {
        firstName = binding.etFirstName.getText().toString();
        lastName = binding.etLastName.getText().toString();
        mobile = binding.etMobile.getText().toString();
        location = binding.tvLocation.getText().toString();
        gender = binding.tvGender.getText().toString();
        studentClass = binding.etClass.getText().toString();
        department = binding.etDept.getText().toString();
        institute = binding.etInstitute.getText().toString();
        streetAddress = binding.etStreet.getText().toString();
        areaAddress = binding.etArea.getText().toString();
        zipCode = binding.etZipCode.getText().toString();
        guardianName = binding.etGuardianName.getText().toString();
        guardianMobile = binding.etGuardinaMobile.getText().toString();

        daysPerWeek = binding.tvDaysPerWeek.getText().toString();
        subjects = binding.tvSubjects.getText().toString();
        salaryRange = binding.etSalaryRange.getText().toString();
        additionalInfo = binding.etAdditionalInfo.getText().toString();
    }

    private int genderCheckedItem=0;
    public void tvGenderClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] genderList = getResources().getStringArray(R.array.gender);
        builder.setTitle("Select Gender").setCancelable(true).setSingleChoiceItems(genderList, genderCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvGender.setText(genderList[i]);
                genderCheckedItem=i;
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private int locationCheckedItem=0;
    public void tvLocationClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] locationList = getResources().getStringArray(R.array.location);
        builder.setTitle("Select Location").setCancelable(true).setSingleChoiceItems(locationList, locationCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvLocation.setText(locationList[i]);
                locationCheckedItem=i;
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private int daysCheckedItem=0;
    public void tvDaysPerWeekClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] daysPerWeekList = getResources().getStringArray(R.array.days_per_week);
        builder.setTitle("Select Days Per Week").setCancelable(true).setSingleChoiceItems(daysPerWeekList, daysCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvDaysPerWeek.setText(daysPerWeekList[i]);
                daysCheckedItem=i;
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void tvSubjectsClicked(View view) {
        subjectListItems = getResources().getStringArray(R.array.teaching_subjects);
        subjectCheckedItems = new boolean[subjectListItems.length];

        for (int i=0; i<subjectUserItems.size();i++){
            subjectCheckedItems[subjectUserItems.get(i)] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Subjects").setMultiChoiceItems(subjectListItems, subjectCheckedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    subjectUserItems.add(position);
                }else{
                    subjectUserItems.remove((Integer.valueOf(position)));
                }
            }
        }).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for(int i=0;i<subjectUserItems.size();i++){
                    item = item + subjectListItems[subjectUserItems.get(i)];
                    if(i!=subjectUserItems.size()-1){
                        item = item + ", ";
                    }
                }
                binding.tvSubjects.setText(item);
            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0;i<subjectCheckedItems.length;i++){
                    subjectCheckedItems[i] = false;
                    subjectUserItems.clear();
                    binding.tvSubjects.setText("");
                }
            }
        }).create().show();
    }


    //B A C K   B U T T O N
    public void btnBackClicked(View view) {
        onBackPressed();
    }
    //A L E R T   D I A L O G   B O X
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        //Closing Alert Dialog use this (alertDialog.cancel();)
    }
    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
