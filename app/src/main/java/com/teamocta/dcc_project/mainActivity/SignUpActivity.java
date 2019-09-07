package com.teamocta.dcc_project.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivitySignupBinding;
import com.teamocta.dcc_project.pojo.Support;

import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    private String firstName, lastName, email, mobile, gender, location, uid;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        init();
    }

    //I N I T I A L I Z I N G    A L L    I D S
    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(); //database root reference

        mAwesomeValidation = new AwesomeValidation(BASIC);
    }

    //C H E C K I N G    I N P U T    V A L I D A T I O N
    private void validationCheck() {
        mAwesomeValidation.addValidation(this, R.id.etFirstName, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.etLastName, "[a-zA-Z\\s]+", R.string.err_name);
        if(binding.etMobile.getText().toString().length() == 0){
            binding.etMobile.setError("Mobile number can't be empty");
        }
        mAwesomeValidation.addValidation(this, R.id.etEmail, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        if(binding.etPassword.getText().toString().length() == 0){
            binding.etPassword.setError("Password can't be empty");
        }
        mAwesomeValidation.addValidation(this, R.id.etConfirmPassword, R.id.etPassword, R.string.err_password_confirmation);
    }

    //O N    C L I C K
    public void btnSignUpClicked(View view) {
        validationCheck();
        String userEmail=binding.etEmail.getText().toString();
        String userPassword=binding.etPassword.getText().toString();
        if(mAwesomeValidation.validate()){
            if(binding.rbUserTutor.isChecked()){
                signUpTutor(userEmail,userPassword);
            }
            else if(binding.rbUserStudent.isChecked()){
                signUpStudent(userEmail,userPassword);
            }else if(!binding.rbUserTutor.isChecked() || !binding.rbUserStudent.isChecked()){
                Support.toastMessageLong("Check box empty!!", getApplicationContext());
            }
        }
    }

    //S I G N    U P    M E T H O D -> TUTOR
    private void signUpTutor(String userEmail, String userPassword) {
        Support.showAlertDialog("Signing Up...", getApplicationContext());
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    uid = firebaseAuth.getCurrentUser().getUid();
                    setDataTutor();
                    firebaseAuth.signOut();
                    //sendVerificationEmail();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                    Support.cancelAlertDialog();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Support.cancelAlertDialog();
                Support.toastMessageLong(e.getMessage(), getApplicationContext());
            }
        });
    }
    //W R I T I N G   D A T A    O N    D A T A B A S E -> TUTOR
    private void setDataTutor() {

        initializeInputData();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("email", email);
        userMap.put("mobile", mobile);
        userMap.put("location", location);
        userMap.put("gender", gender);

        //databaseReference.setValue("My Message");
        DatabaseReference tutorReference = databaseReference.child("Tutor");
        tutorReference.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Support.toastMessageShort("Registered Successfully as a Tutor!", getApplicationContext());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });
    }

    //S I G N    U P    M E T H O D -> STUDENT
    private void signUpStudent(String userEmail, String userPassword) {
        Support.showAlertDialog("Signing Up...", getApplicationContext());
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    uid = firebaseAuth.getCurrentUser().getUid();
                    setDataStudent();
                    firebaseAuth.signOut();
                    //sendVerificationEmail();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                    Support.cancelAlertDialog();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Support.cancelAlertDialog();
                Support.toastMessageLong(e.getMessage(), getApplicationContext());
            }
        });
    }
    //W R I T I N G   D A T A    O N    D A T A B A S E -> STUDENT
    private void setDataStudent() {

        initializeInputData();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("email", email);
        userMap.put("mobile", mobile);
        userMap.put("location", location);
        userMap.put("gender", gender);

        //databaseReference.setValue("My Message");
        DatabaseReference studentReference = databaseReference.child("Student");
        studentReference.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Support.toastMessageShort("Registered Successfully as a Student!", getApplicationContext());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });
    }

/*    private void sendVerificationEmail() {
        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                    Support.cancelAlertDialog();
                    Support.toastMessageLong("Please check your email for verification", getApplicationContext());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Support.cancelAlertDialog();
                Support.toastMessageLong(e.getMessage(), getApplicationContext());
            }
        });
    }*/

    //
    private void initializeInputData() {
        firstName = binding.etFirstName.getText().toString();
        lastName = binding.etLastName.getText().toString();
        email = binding.etEmail.getText().toString();
        mobile = binding.etMobile.getText().toString();
        location = binding.spnrLocation.getSelectedItem().toString();
        gender = binding.spnrGender.getSelectedItem().toString();
    }

    public void btnBack(View view) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}