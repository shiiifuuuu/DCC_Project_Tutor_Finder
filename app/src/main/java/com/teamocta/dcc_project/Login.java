package com.teamocta.dcc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    //V A R I A B L E S
    private EditText etEmail;
    private EditText etPassword;
    private CheckBox chkbxKeepLoggedIn;

    private String userEmail, userPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    //When initializing the Activity, check to see if the user is currently signed in.
/*    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser) {
        if(!currentUser.equals(null)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }*/

    //I N I T I A L I Z I N G    A L L    I D S
    private void init() {
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        chkbxKeepLoggedIn=findViewById(R.id.chkbxKeepLoggedIn);

        firebaseAuth=FirebaseAuth.getInstance();
    }

    //O N     C L I C K
    public void btnLoginClicked(View view) {
        userEmail=etEmail.getText().toString();
        userPassword=etPassword.getText().toString();

        if(userEmail.equals("") || userPassword.equals("")){
            toastMessageShort("Fields Empty!");
        }else{
            signInUser(userEmail, userPassword);
        }
    }
    public void tvForgetPassClicked(View view) {
        Intent intent = new Intent(Login.this, ForgetPass.class);
        startActivity(intent);
    }
    public void tvSignupClicked(View view) {
        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
    }

    //S I G N    I N    M E T H O D
    private void signInUser(String userEmail, String userPassword) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    toastMessageShort("Sign In ERROR!!");
                    toastMessageLong("Check your email or password again.");
                }else{
                    /*if(firebaseAuth.getCurrentUser().isEmailVerified()){*/
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    /*}else{
                        toastMessageLong("Please verify your email first");
                    }*/
                }
            }
        });
    }

    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
