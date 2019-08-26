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
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class UpdateTutorProfileActivity extends AppCompatActivity {

    private ActivityUpdateTutorProfileBinding binding;
    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String uid;
    private AwesomeValidation mAwesomeValidation;

    private UserProfile tutorProfile;

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
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();
        mAwesomeValidation = new AwesomeValidation(BASIC);

        areaUserItems = new ArrayList<>();
        subjectUserItems = new ArrayList<>();
    }

    //getting values from previous activity
    private void getIntentExtras() {
        tutorProfile = (UserProfile) getIntent().getSerializableExtra("tutorProfile");
        binding.etFirstName.setText(tutorProfile.getFirstName());
        binding.etLastName.setText(tutorProfile.getLastName());
        binding.etMobile.setText(tutorProfile.getMobile());
        binding.tvLocation.setText(tutorProfile.getLocation());
        binding.tvGender.setText(tutorProfile.getGender());
        binding.etProfessiopn.setText(tutorProfile.getProfession());
        binding.etInstitute.setText(tutorProfile.getInstitute());
        binding.etExperience.setText(tutorProfile.getExperience());
        binding.tvTuitionType.setText(tutorProfile.getTuitionType());
        binding.tvAreaCovered.setText(tutorProfile.getAreaCovered());
        binding.tvDaysPerWeek.setText(tutorProfile.getDaysPerWeek());
        binding.etMinimumSalary.setText(tutorProfile.getMinimumSalary());
        binding.tvTeachingSubjects.setText(tutorProfile.getTeachingSubjects());
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
            Support.showAlertDialog("Updating user profile...", this);
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
                    Support.cancelAlertDialog();
                    Support.toastMessageLong("Profile Updated Successfully.", UpdateTutorProfileActivity.this);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Support.cancelAlertDialog();
                Support.toastMessageLong(e.getMessage(), UpdateTutorProfileActivity.this);
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

    private int locationCheckedItem=0;
    public void tvLocationClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] locationList = getResources().getStringArray(R.array.location);
        builder.setTitle("Select Location").setCancelable(true).setSingleChoiceItems(locationList, locationCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvLocation.setText(locationList[i]);
                locationCheckedItem = i;
                dialogInterface.dismiss();
            }
        }).create().show();
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

    private int tuitionTypeCheckedItem=0;
    public void tvTuitionTypeClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] tuitionType = getResources().getStringArray(R.array.tuition_type);
        builder.setTitle("One Time Tuition ?").setCancelable(true).setSingleChoiceItems(tuitionType, tuitionTypeCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                binding.tvTuitionType.setText(tuitionType[i]);
                tuitionTypeCheckedItem=i;
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    //getting areaCovered Lists
    public void tvAreaCoveredClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        areaListItems = getResources().getStringArray(R.array.area_covered);
        areaCheckedItems = new boolean[areaListItems.length];
        for (int i=0; i<areaUserItems.size();i++){
            areaCheckedItems[areaUserItems.get(i)] = true;
        }
        builder.setTitle("Select Areas");
        builder.setMultiChoiceItems(areaListItems, areaCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    areaUserItems.add(position);
                }else{
                    areaUserItems.remove((Integer.valueOf(position)));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        subjectListItems = getResources().getStringArray(R.array.teaching_subjects);
        subjectCheckedItems = new boolean[subjectListItems.length];

        for (int i=0; i<subjectUserItems.size();i++){
            subjectCheckedItems[subjectUserItems.get(i)] = true;
        }

        builder.setTitle("Select Subjects");
        builder.setMultiChoiceItems(subjectListItems, subjectCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    subjectUserItems.add(position);
                }else{
                    subjectUserItems.remove((Integer.valueOf(position)));
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
}
