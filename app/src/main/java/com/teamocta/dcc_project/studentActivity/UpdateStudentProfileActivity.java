package com.teamocta.dcc_project.studentActivity;

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

import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class UpdateStudentProfileActivity extends AppCompatActivity {

    private ActivityUpdateStudentProfileBinding binding;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String uid;
    private AwesomeValidation mAwesomeValidation;

    private String firstName,lastName,mobile,location,gender,studentClass, department,institute;
    private String streetAddress,areaAddress,zipCode,guardianName,guardianMobile;

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
        builder = new AlertDialog.Builder(this);
        mAwesomeValidation = new AwesomeValidation(BASIC);
    }

    //getting values from previous activity
    private void getIntentExtras() {
        firstName = getIntent().getExtras().getString("firstName");
        lastName = getIntent().getExtras().getString("lastName");
        mobile = getIntent().getExtras().getString("mobile");
        studentClass = getIntent().getExtras().getString("studentClass");
        institute = getIntent().getExtras().getString("institute");
        department = getIntent().getExtras().getString("department");
        streetAddress = getIntent().getStringExtra("streetAddress");
        areaAddress = getIntent().getStringExtra("areaAddress");
        zipCode = getIntent().getStringExtra("zipCode");
        guardianName = getIntent().getExtras().getString("guardianName");
        guardianMobile = getIntent().getExtras().getString("guardianMobile");
    }
    private void setCurrentField() {
        binding.etFirstName.setText(firstName);
        binding.etLastName.setText(lastName);
        binding.etMobile.setText(mobile);
        binding.etClass.setText(studentClass);
        binding.etInstitute.setText(institute);
        binding.etDept.setText(department);
        binding.etStreet.setText(streetAddress);
        binding.etArea.setText(areaAddress);
        binding.etZipCode.setText(zipCode);
        binding.etGuardianName.setText(guardianName);
        binding.etGuardinaMobile.setText(guardianMobile);
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
        location = binding.spnrLocation.getSelectedItem().toString();
        gender = binding.spnrGender.getSelectedItem().toString();
        studentClass = binding.etClass.getText().toString();
        department = binding.etDept.getText().toString();
        institute = binding.etInstitute.getText().toString();
        streetAddress = binding.etStreet.getText().toString();
        areaAddress = binding.etArea.getText().toString();
        zipCode = binding.etZipCode.getText().toString();
        guardianName = binding.etGuardianName.getText().toString();
        guardianMobile = binding.etGuardinaMobile.getText().toString();
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
