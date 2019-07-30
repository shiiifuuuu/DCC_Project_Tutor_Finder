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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamocta.dcc_project.databinding.ActivitySignupBinding;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SignUp extends AppCompatActivity {

    private ActivitySignupBinding binding;

    private String firstName, lastName, email, mobile, gender, location, Uid;
    private String userEmail, userPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseTutorRef;
    private DatabaseReference databaseStudentRef;

    private AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        
        init();
        validationCheck();
    }

    //I N I T I A L I Z I N G    A L L    I D S
    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
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
        userEmail=binding.etEmail.getText().toString();
        userPassword=binding.etPassword.getText().toString();

        if(mAwesomeValidation.validate()){
            if(!binding.rbUserTutor.isChecked()){
                toastMessageLong("Error!! Choose What Suits You");
            }else if(binding.rbUserTutor.isChecked()){
                signUpTutor(userEmail,userPassword);
            }else if(!binding.rbUserStudent.isChecked()){
                toastMessageLong("Error!! Choose What Suits You");
            }else if(binding.rbUserStudent.isChecked()){
                signUpStudent(userEmail,userPassword);
            }
        }
    }

    //S I G N    U P    M E T H O D
    private void signUpTutor(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    toastMessageShort("Sign up Error !! Try Again");
                }else{
                    firebaseUser = firebaseAuth.getCurrentUser();
                    Uid = firebaseUser.getUid();
                    writeTutorData(Uid);
                    nullifyAllFields();
                    Intent intent = new Intent(SignUp.this,Login.class);
                    startActivity(intent);
                    finish();
                    toastMessageShort("Registered Successfully!");
                }
            }
        });
    }
    private void signUpStudent(String userEmail, String userPassword) {
    }

    //W R I T I N G   D A T A    O N    D A T A B A S E
    private void writeTutorData(String Uid) {
        initInputData();
        TutorInformation newTutor = new TutorInformation(Uid, firstName,lastName, email, mobile, location, gender);
        databaseTutorRef = database.getReference("Tutor");
        databaseTutorRef.child(Uid).setValue(newTutor);
    }

    ////getting inputs from the user and putting it to a variable to simply create a new tutor
    private void initInputData() {
        firstName = binding.etFirstName.getText().toString();
        lastName = binding.etLastName.getText().toString();
        email = binding.etEmail.getText().toString();
        mobile = binding.etMobile.getText().toString();
        location = binding.spnrLocation.getSelectedItem().toString();
        gender = binding.spnrGender.getSelectedItem().toString();
    }

    /*private void signupUser(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    toastMessageShort("Sign up Error !! Try Again");
                }else{
                    toastMessageShort("Registered Successfully!");
                    nullifyAllFields();
                    Intent intent = new Intent(SignUp.this,Login.class);
                    startActivity(intent);
                    finish();
                    //email Verification Method
                    *//*firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                toastMessageShort("Registered Successfully!");
                                nullifyAllFields();
                                Intent intent = new Intent(SignUp.this,Login.class);
                                startActivity(intent);
                                finish();
                                toastMessageLong("Please check your email for verification.");
                            }
                        }
                    });*//*
                }
            }
        });
    }*/

    //N U L L I F Y I N G    A L L    F I E L D S   AFTER REGISTRATION SUCCESSFUL
    private void nullifyAllFields() {
        binding.etFirstName.setText("");
        binding.etLastName.setText("");
        binding.etMobile.setText("");
        binding.etEmail.setText("");
        binding.etPassword.setText("");
        binding.etConfirmPassword.setText("");
        binding.spnrGender.setAdapter(null);
        binding.spnrLocation.setAdapter(null);
        binding.rgUserProfile.clearCheck();
    }

    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
