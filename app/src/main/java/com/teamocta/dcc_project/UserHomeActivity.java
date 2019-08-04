package com.teamocta.dcc_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    private Fragment profileFragment, searchFragment, messageFragment;
    private BottomNavigationView navView;
    private Boolean userIsStudent, userIsTutor;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        init();
        checkingUserAndInitializing();

        replaceFragment(profileFragment);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void init() {
        navView = findViewById(R.id.nav_view);
        firebaseAuth = FirebaseAuth.getInstance();

        userIsStudent = getIntent().getExtras().getBoolean("userIsStudent");
        userIsTutor = getIntent().getExtras().getBoolean("userIsTutor");
    }

    private void checkingUserAndInitializing() {
        if(userIsStudent){
            profileFragment = new StudentProfileFragment();
            searchFragment = new StudentSearchFragment();
            messageFragment = new StudentMessageFragment();
        }else if(userIsTutor){
            profileFragment = new TutorProfileFragment();
            searchFragment = new TutorSearchFragment();
            messageFragment = new TutorMessageFragment();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    replaceFragment(profileFragment);
                    return true;
                case R.id.navigation_search:
                    replaceFragment(searchFragment);
                    return true;
                case R.id.navigation_message:
                    replaceFragment(messageFragment);
                    return true;
                case R.id.navigation_logout:
                    logoutCurrentUser();
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

    //L O G O U T    D I A L O G
    private void logoutCurrentUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toastMessageLong("Signing out user...");
                        firebaseAuth.signOut();
                        startActivity(new Intent(UserHomeActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //A L E R T   D I A L O G
    private void showAlertDialog (String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
