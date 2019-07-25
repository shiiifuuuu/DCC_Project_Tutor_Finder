package com.teamocta.dcc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class Signup extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private EditText etEmail, etPassword, etConfirmPass;
    private RadioGroup rgUserProfile;
    private RadioButton rbUserTutor, rbUserStudent, rbUserBoth;
    private Button btnSignup;

    private String userEmail, userPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        init();
        validationCheck();
    }

    private void validationCheck() {
        mAwesomeValidation.addValidation(this, R.id.etFirstName, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.etLastName, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.etMobile, RegexTemplate.TELEPHONE, R.string.err_mobile);
        mAwesomeValidation.addValidation(this, R.id.etEmail, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        mAwesomeValidation.addValidation(this, R.id.etConfirmPassword, R.id.etPassword, R.string.err_password_confirmation);
    }

    //I N I T I A L I Z I N G    A L L    I D S
    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etConfirmPass=findViewById(R.id.etConfirmPassword);
        rgUserProfile=findViewById(R.id.rgUserProfile);
        rbUserTutor=findViewById(R.id.rbUserTutor);
        rbUserStudent=findViewById(R.id.rbUserStudent);
        rbUserBoth=findViewById(R.id.rbUserBoth);
        btnSignup=findViewById(R.id.btnSignup);

        mAwesomeValidation = new AwesomeValidation(BASIC);
    }

    //O N    C L I C K
    public void btnSignupClicked(View view) {
        userEmail=etEmail.getText().toString();
        userPassword=etPassword.getText().toString();

        if(mAwesomeValidation.validate()){
            signupUser(userEmail, userPassword);
        }
    }

    //S I G N    U P    M E T H O D
    private void signupUser(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    toastMessageShort("Sign up Error !! Try Again");
                }else{
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                toastMessageShort("Regeistered Successfully!");
                                toastMessageLong("Please check your email for verification.");
                                nullifyAllFields();
                            }
                        }
                    });
                }
            }
        });
    }

    private void nullifyAllFields() {
        etFirstName.setText("");
        etLastName.setText("");
        etMobile.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPass.setText("");
        rgUserProfile.clearCheck();
    }


    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
