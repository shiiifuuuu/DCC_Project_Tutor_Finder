package com.teamocta.dcc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamocta.dcc_project.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private String userEmail, userPassword;

    private String studentUid, tutorUid;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;

    private DatabaseReference studentRef, tutorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

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
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();

        studentRef = database.getReference().child("Student");
        tutorRef = database.getReference().child("Tutor");
    }

    //O N     C L I C K
    public void btnLoginClicked(View view) {
        userEmail=binding.etEmail.getText().toString();
        userPassword=binding.etPassword.getText().toString();

        if(userEmail.equals("") || userPassword.equals("")){
            toastMessageShort("Fields Empty!");
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
                if(!task.isSuccessful()){
                    toastMessageShort("Sign In ERROR!!");
                    toastMessageLong("Check your email or password again.");
                }else{
                    /*if(firebaseUser.isEmailVerified()){*/
                    /*Intent intent = new Intent(this, TutorHome.class);
                    startActivity(intent);
                    finish();*/
                    //new MyAsyncTask().execute();
                    /*}else{
                        toastMessageLong("Please verify your email first");
                    }*/
                }
            }
        });
    }

    /*private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        public MyAsyncTask() {
        }

        @Override
        protected synchronized Void doInBackground(Void... args) {
            getUserEmailUid();
            return null;
        }
        @Override
        protected synchronized void onPostExecute(Void result) {
            gotoSpecificIntent();
        }
    }*/

    /*private void getUserEmailUid() {
        studentRef.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot email: dataSnapshot.getChildren()){
                    studentUid = email.getKey();
                    toastMessageShort("Student Profile Found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*
        tutorRef.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot email: dataSnapshot.getChildren()){
                    tutorUid = email.getKey();
                    toastMessageShort("Tutor Profile Found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
    /*private void gotoSpecificIntent() {
        if(studentUid != null){
            Intent intent = new Intent(this, StudentHome.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, TutorHome.class);
            startActivity(intent);
            finish();
        }
    }*/


    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
