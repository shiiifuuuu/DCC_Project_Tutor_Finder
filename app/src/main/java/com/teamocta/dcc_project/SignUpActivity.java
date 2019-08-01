package com.teamocta.dcc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamocta.dcc_project.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    private String firstName, lastName, email, mobile, gender, location, Uid;

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
        mAwesomeValidation.addValidation(this, R.id.etMobile, RegexTemplate.TELEPHONE, R.string.err_mobile);
        mAwesomeValidation.addValidation(this, R.id.etEmail, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
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
            }else if(binding.rbUserStudent.isChecked()){
                signUpStudent(userEmail,userPassword);
            }
        }else if(!(binding.rbUserStudent.isChecked() || binding.rbUserTutor.isChecked())){
            toastMessageLong("Must Check any REGISTER button");
        }
    }

    //S I G N    U P    M E T H O D -> TUTOR
    private void signUpTutor(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Uid = firebaseAuth.getCurrentUser().getUid();
                    setDataTutor();
                    startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessageLong(e.getMessage());
            }
        });
    }
    //W R I T I N G   D A T A    O N    D A T A B A S E -> TUTOR
    private void setDataTutor() {

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("ID", Uid);
        userMap.put("First Name", firstName);
        userMap.put("Last Name", lastName);
        userMap.put("Email", email);
        userMap.put("Mobile", mobile);
        userMap.put("Location", location);
        userMap.put("Gender", gender);

        //databaseReference.setValue("My Message");
        DatabaseReference tutorReference = databaseReference.child("Tutor");
        tutorReference.child(Uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                toastMessageShort("Registered Successfully as a Tutor!");
            }
        });
    }

    //S I G N    U P    M E T H O D -> STUDENT
    private void signUpStudent(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Uid = firebaseAuth.getCurrentUser().getUid();
                    initializeInputData();
                    setDataStudent();
                    startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessageLong(e.getMessage());
            }
        });
    }
    //W R I T I N G   D A T A    O N    D A T A B A S E -> STUDENT
    private void setDataStudent() {

        initializeInputData();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("ID", Uid);
        userMap.put("First Name", firstName);
        userMap.put("Last Name", lastName);
        userMap.put("Email", email);
        userMap.put("Mobile", mobile);
        userMap.put("Location", location);
        userMap.put("Gender", gender);

        //databaseReference.setValue("My Message");
        DatabaseReference studentReference = databaseReference.child("Student");
        studentReference.child(Uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                toastMessageShort("Registered Successfully as a Student!");
            }
        });
    }

    //
    private void initializeInputData() {
        firstName = binding.etFirstName.getText().toString();
        lastName = binding.etLastName.getText().toString();
        email = binding.etEmail.getText().toString();
        mobile = binding.etMobile.getText().toString();
        location = binding.spnrLocation.getSelectedItem().toString();
        gender = binding.spnrGender.getSelectedItem().toString();
    }

    public void btnBackClicked(View view) {
        onBackPressed();
    }

    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
//email Verification Method
/*firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
@Override
public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
        toastMessageShort("Registered Successfully!");
        nullifyAllFields();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
        toastMessageLong("Please check your email for verification.");
        }
        }
        });*/