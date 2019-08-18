package com.teamocta.dcc_project.tutorActivity;

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
import com.teamocta.dcc_project.databinding.ActivityUpdateTutorProfileBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class UpdateTutorProfileActivity extends AppCompatActivity {

    private ActivityUpdateTutorProfileBinding binding;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String uid;
    private AwesomeValidation mAwesomeValidation;

    private String firstName,lastName,mobile,location,gender,profession,institute;
    private String experience,tuitionType,areaCovered,daysPerWeek,minimumSalary,teachingSubjects;

    private String[] areaListItems, subjectListItems;
    private boolean[] areaCheckedItems, subjectCheckedItems;
    private ArrayList<Integer> areaUserItems, subjectUserItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_tutor_profile);
        init();
        getIntentExtras();
        setCurrentField();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        builder = new AlertDialog.Builder(this);
        mAwesomeValidation = new AwesomeValidation(BASIC);

        areaUserItems = new ArrayList<>();
        subjectUserItems = new ArrayList<>();
    }

    //getting values from previous activity
    private void getIntentExtras() {
        firstName = getIntent().getExtras().getString("firstName");
        lastName = getIntent().getExtras().getString("lastName");
        mobile = getIntent().getExtras().getString("mobile");
        location = getIntent().getExtras().getString("location", location);
        gender = getIntent().getExtras().getString("gender", gender);
        profession = getIntent().getExtras().getString("profession");
        institute = getIntent().getExtras().getString("institute");
        experience = getIntent().getExtras().getString("experience");
        tuitionType = getIntent().getExtras().getString("tuitionType", tuitionType);
        areaCovered = getIntent().getExtras().getString("areaCovered");
        daysPerWeek = getIntent().getExtras().getString("daysPerWeek", daysPerWeek);
        minimumSalary = getIntent().getExtras().getString("minimumSalary");
        teachingSubjects = getIntent().getExtras().getString("teachingSubjects");
    }
    private void setCurrentField() {
        binding.etFirstName.setText(firstName);
        binding.etLastName.setText(lastName);
        binding.etMobile.setText(mobile);
        binding.tvLocation.setText(location);
        binding.tvGender.setText(gender);
        binding.etProfessiopn.setText(profession);
        binding.etInstitute.setText(institute);
        binding.etExperience.setText(experience);
        binding.tvTuitionType.setText(tuitionType);
        binding.tvAreaCovered.setText(areaCovered);
        binding.tvDaysPerWeek.setText(daysPerWeek);
        binding.etMinimumSalary.setText(minimumSalary);
        binding.tvTeachingSubjects.setText(teachingSubjects);
    }

    //Database Update
    private void validationCheck() {
        mAwesomeValidation.addValidation(this, R.id.etFirstName, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.etLastName, "[a-zA-Z\\s]+", R.string.err_name);
        if(binding.etMobile.getText().toString().length() == 0){
            binding.etMobile.setError("Mobile number can't be empty");
        }
        if(binding.etProfessiopn.getText().toString().length() == 0){
            binding.etProfessiopn.setError("Field can't be empty!");
        }
        if(binding.etInstitute.getText().toString().length() == 0){
            binding.etInstitute.setError("Field can't be empty!");
        }
        if(binding.etExperience.getText().toString().length() == 0){
            binding.etExperience.setError("Field can't be empty!");
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
        userMap.put("profession", profession);
        userMap.put("institute", institute);
        userMap.put("experience", experience);
        userMap.put("tuitionType", tuitionType);
        userMap.put("areaCovered", areaCovered);
        userMap.put("daysPerWeek", daysPerWeek);
        userMap.put("minimumSalary", minimumSalary);
        userMap.put("teachingSubjects", teachingSubjects);


        DatabaseReference studentReference = databaseReference.child("Tutor");
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
        profession = binding.etProfessiopn.getText().toString();
        institute = binding.etInstitute.getText().toString();
        experience = binding.etExperience.getText().toString();
        tuitionType = binding.tvTuitionType.getText().toString();
        daysPerWeek = binding.tvDaysPerWeek.getText().toString();
        minimumSalary = binding.etMinimumSalary.getText().toString();
        areaCovered = binding.tvAreaCovered.getText().toString();
        teachingSubjects = binding.tvTeachingSubjects.getText().toString();
    }


    public void tvLocationClicked(View view) {
        final String[] locationList = getResources().getStringArray(R.array.location);
        builder.setTitle("Select Location").setCancelable(true).setSingleChoiceItems(locationList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvLocation.setText(locationList[i]);
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void tvGenderClicked(View view) {
        final String[] genderList = getResources().getStringArray(R.array.gender);
        builder.setTitle("Select Gender").setCancelable(true).setSingleChoiceItems(genderList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvGender.setText(genderList[i]);
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void tvDaysPerWeekClicked(View view) {
        final String[] daysPerWeekList = getResources().getStringArray(R.array.days_per_week);
        builder.setTitle("Select Days Per Week").setCancelable(true).setSingleChoiceItems(daysPerWeekList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvDaysPerWeek.setText(daysPerWeekList[i]);
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void tvTuitionTypeClicked(View view) {
        final String[] tuitionType = getResources().getStringArray(R.array.tuition_type);
        builder.setTitle("One Time Tuition ?").setCancelable(true).setSingleChoiceItems(tuitionType, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvTuitionType.setText(tuitionType[i]);
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    //getting areaCovered Lists
    public void tvAreaCoveredClicked(View view) {
        areaListItems = getResources().getStringArray(R.array.area_covered);
        areaCheckedItems = new boolean[areaListItems.length];

        builder.setTitle("Select Areas");
        builder.setMultiChoiceItems(areaListItems, areaCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    if(!areaUserItems.contains(position)){
                        areaUserItems.add(position);
                    }else{
                        areaUserItems.remove(position);
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for(int i=0;i<areaUserItems.size();i++){
                    item = item + areaListItems[areaUserItems.get(i)];
                    if(i!=areaUserItems.size()-1){
                        item = item + ", ";
                    }
                }
                binding.tvAreaCovered.setText(item);
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0;i<areaCheckedItems.length;i++){
                    areaCheckedItems[i] = false;
                    areaUserItems.clear();
                    binding.tvAreaCovered.setText("");
                }
            }
        });
        builder.create().show();
    }

    //getting teachingSubject Lists
    public void tvTeachingSubjectsClicked(View view) {
        subjectListItems = getResources().getStringArray(R.array.teaching_subjects);
        subjectCheckedItems = new boolean[subjectListItems.length];

        builder.setTitle("Select Subjects");
        builder.setMultiChoiceItems(subjectListItems, subjectCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    if(!subjectUserItems.contains(position)){
                        subjectUserItems.add(position);
                    }else{
                        subjectUserItems.remove(position);
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for(int i=0;i<subjectUserItems.size();i++){
                    item = item + subjectListItems[subjectUserItems.get(i)];
                    if(i!=subjectUserItems.size()-1){
                        item = item + ", ";
                    }
                }
                binding.tvTeachingSubjects.setText(item);
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0;i<subjectCheckedItems.length;i++){
                    subjectCheckedItems[i] = false;
                    subjectUserItems.clear();
                    binding.tvTeachingSubjects.setText("");
                }
            }
        });
        builder.create().show();
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
