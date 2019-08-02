package com.teamocta.dcc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.teamocta.dcc_project.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences myPrefs;
    private static final String PREFS_NAME = "myPrefsFile";
    private String currentUser;

    private ActivityLoginBinding binding;
    private AlertDialog alertDialog;
    private Boolean userIsTutor, userIsStudent;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        init();
        rememberUser();
    }

    private void rememberUser() {
        if(firebaseAuth.getCurrentUser()!=null){
            toastMessageLong("Firebase user OK");
            loadPrefsFile();
            if(currentUser.equals("tutor")){
                showAlertDialog("Loading your account..");
                Boolean userIsTutor = true;
                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                intent.putExtra("userIsTutor", userIsTutor);
                startActivity(intent);
                finish();
                alertDialog.cancel();
                toastMessageShort("Tutor Login Successful");
            }else if(currentUser=="student"){
                showAlertDialog("Loading your account..");
                Boolean userIsStudent = true;
                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                intent.putExtra("userIsStudent", userIsStudent);
                startActivity(intent);
                finish();
                alertDialog.cancel();
                toastMessageShort("Student Login Successful");
            }
            /*showAlertDialog("Loading your account..");
            String uid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference tutorReference = databaseReference.child("Tutor");
            tutorReference.orderByChild("ID").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()){
                        Boolean userIsTutor = true;
                        Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                        intent.putExtra("userIsTutor", userIsTutor);
                        startActivity(intent);
                        finish();
                        alertDialog.cancel();
                        toastMessageShort("Tutor Login Successful");
                    }
                    else{
                        Boolean userIsStudent = true;
                        Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                        intent.putExtra("userIsStudent", userIsStudent);
                        startActivity(intent);
                        finish();
                        alertDialog.cancel();
                        toastMessageShort("Student Login Successful");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }
    }

    //I N I T I A L I Z I N G
    private void init() {
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference =FirebaseDatabase.getInstance().getReference(); //database root reference
        myPrefs = getSharedPreferences(PREFS_NAME, 0);

    }

    //O N     C L I C K
    public void btnLoginClicked(View view) {
        String userEmail=binding.etEmail.getText().toString();
        String userPassword=binding.etPassword.getText().toString();

        if(userEmail.equals("") || userPassword.equals("")){
            toastMessageShort("Doesn't match any account!");
        }else{
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
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        showAlertDialog("Logging In..");
                        checkUser();
                    }else{
                        toastMessageLong("Verify your email first.");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessageLong(e.getMessage());
            }
        });
    }

    //checking if the currentUser UID matches with Tutor or Student and then
    //going according to that matching intent
    private void checkUser() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference tutorReference = databaseReference.child("Tutor");
        tutorReference.orderByChild("ID").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    currentUser = "tutor";
                    savePrefsFile(currentUser);
                    userIsTutor = true;
                    Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                    intent.putExtra("userIsTutor", userIsTutor);
                    startActivity(intent);
                    finish();
                    alertDialog.cancel();
                    toastMessageShort("Tutor Login Successful");
                }
                else{
                    currentUser="student";
                    savePrefsFile(currentUser);
                    userIsStudent = true;
                    Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                    intent.putExtra("userIsStudent", userIsStudent);
                    startActivity(intent);
                    finish();
                    alertDialog.cancel();
                    toastMessageShort("Student Login Successful");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void savePrefsFile(String currentUser) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("USER", currentUser);
        editor.commit();
        toastMessageLong(currentUser + " data saved");
    }
    private void loadPrefsFile(){
        if(myPrefs.contains("USER")){
            currentUser = myPrefs.getString("USER", "");
            toastMessageLong(currentUser + " data loaded");
        }
    }

    //P R E V E N T I N G    U S E R    F R O M    A C C E S S I N G    A C T I V I T Y
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
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
