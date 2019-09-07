package com.teamocta.dcc_project.mainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityLoginBinding;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.studentActivity.StudentProfileActivity;
import com.teamocta.dcc_project.tutorActivity.TutorProfileActivity;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences myPrefs;
    private static final String PREFS_NAME = "myPrefsFile";
    private String saveUserProfileType = "";

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isUserExist();
    }

    //C H E C K I N   F O R   A N Y   C U R R E N T   U S E R
    private void isUserExist() {
        if(firebaseAuth.getCurrentUser()==null){
            myPrefs.edit().clear().commit();
        }else{
            loadPrefsFile();
            Support.showAlertDialog("Loading your account..", LoginActivity.this);
            if(saveUserProfileType.equals("tutor")){
                Intent intent = new Intent(LoginActivity.this, TutorProfileActivity.class);
                startActivity(intent);
                finish();
                Support.cancelAlertDialog();
                Support.toastMessageShort("Tutor Login Successful", LoginActivity.this);
            }else if(saveUserProfileType.equals("student")){
                Intent intent = new Intent(LoginActivity.this, StudentProfileActivity.class);
                startActivity(intent);
                finish();
                Support.cancelAlertDialog();
                Support.toastMessageShort("Student Login Successful", LoginActivity.this);
            }else if(saveUserProfileType.equals("")){
                firebaseAuth.signOut();
                Support.cancelAlertDialog();
            }
        }
    }

    //I N I T I A L I Z I N G
    private void init() {
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference =FirebaseDatabase.getInstance().getReference(); //database root reference
        myPrefs = getSharedPreferences(PREFS_NAME, 0); // mode 0 private

        databaseReference.keepSynced(true);
    }

    private void validationCheck(){
        if(binding.etEmail.getText().toString().length()==0){
            binding.etEmail.setError("Email required");
        }
        if(binding.etPassword.getText().toString().length()==0){
            binding.etPassword.setError("Password required");
        }
    }
    //O N     C L I C K
    public void btnLoginClicked(View view) {
        validationCheck();

        String userEmail=binding.etEmail.getText().toString();
        String userPassword=binding.etPassword.getText().toString();
        if(!userEmail.equals("") && !userPassword.equals("")){
            signInUser(userEmail, userPassword);
        }
    }
    public void tvForgetPassClicked(View view) {
        Intent intent = new Intent(this, ForgetPassActivity.class);
        startActivity(intent);
    }
    public void btnSignUpClicked(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //S I G N    I N    M E T H O D
    private void signInUser(String userEmail, String userPassword) {
        Support.showAlertDialog("Logging In..", LoginActivity.this);
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkUserType();
                    /*if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        Support.showAlertDialog("Logging In..", getApplicationContext());
                        checkUserType();
                    }else{
                        Support.toastMessageLong("Verify your email first.", getApplicationContext());
                    }*/
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

    //checking if the logged in user Uid matches with Tutor or Student and then
    //going according to that matching intent
    private void checkUserType() {
        final String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference tutorReference = databaseReference.child("Tutor");
        tutorReference.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    if(binding.cbRememberUser.isChecked()){
                        saveUserProfileType = "tutor";
                        savePrefsFile(saveUserProfileType);
                    }else{
                        myPrefs.edit().clear().commit();
                    }
                    Intent intent = new Intent(getApplicationContext(), TutorProfileActivity.class);
                    startActivity(intent);
                    finish();
                    Support.cancelAlertDialog();
                    Support.toastMessageShort("Tutor Login Successful", getApplicationContext());
                }
                else{
                    if(binding.cbRememberUser.isChecked()){
                        saveUserProfileType ="student";
                        savePrefsFile(saveUserProfileType);
                    }
                    else{
                        myPrefs.edit().clear().commit();
                    }
                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                    startActivity(intent);
                    finish();
                    Support.cancelAlertDialog();
                    Support.toastMessageShort("Student Login Successful", getApplicationContext());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Support.cancelAlertDialog();
                databaseError.getMessage();
            }
        });
    }

    //S A V I N G   &   L O A D I N G   internal data
    private void savePrefsFile(String currentUser) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("USER", currentUser);
        editor.commit();
    }
    private void loadPrefsFile(){
        if(myPrefs.contains("USER")){
            saveUserProfileType = myPrefs.getString("USER", "");
        }
    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed(){
//        moveTaskToBack(true);
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Support.toastMessageShort("Please click BACK again to exit", getApplicationContext());

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}