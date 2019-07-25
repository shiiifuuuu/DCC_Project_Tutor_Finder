package com.teamocta.dcc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private EditText etEmail, etPassword, etConfirmPassword;
    private RadioGroup rgUserProfile;
    private RadioButton rbUserTutor, rbUserStudent, rbUserBoth;
    private Button btnSignup;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        init();
    }
    //I N I T I A L I Z I N G    A L L    I D S
    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);
        rgUserProfile=findViewById(R.id.rgUserProfile);
        rbUserTutor=findViewById(R.id.rbUserTutor);
        rbUserStudent=findViewById(R.id.rbUserStudent);
        rbUserBoth=findViewById(R.id.rbUserBoth);
        btnSignup=findViewById(R.id.btnSignup);
    }

    //O N    C L I C K
    public void btnSignupClicked(View view) {
    }
}
