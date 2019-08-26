package com.teamocta.dcc_project.studentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.adapter.TutorListAdapter;
import com.teamocta.dcc_project.databinding.ActivityStudentSearchBinding;
import com.teamocta.dcc_project.mainActivity.LoginActivity;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;
import com.teamocta.dcc_project.viewActivity.TutorViewActivity;
import java.util.ArrayList;

public class StudentSearchActivity extends AppCompatActivity implements TutorListAdapter.OnTutorClickListener {

    private ActivityStudentSearchBinding binding;

    private UserProfile tutorProfile;
    private ArrayList<UserProfile> tutorList, filteredList;
    private TutorListAdapter tutorListAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_search);

        init();
        getTutors();
        configRecyclerView();
        etTextChangeListener();
    }

    private void init() {
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.navView.getMenu().getItem(1).setChecked(true);

        tutorProfile = new UserProfile();
        tutorList = new ArrayList<>();
        tutorListAdapter = new TutorListAdapter(tutorList, this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void getTutors() {
        DatabaseReference tutorRef = databaseReference.child("Tutor");
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot tutors: dataSnapshot.getChildren()){
                        tutorProfile = tutors.getValue(UserProfile.class);
                        tutorList.add(tutorProfile);
                        tutorListAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void configRecyclerView() {
        binding.rvTutorList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTutorList.setAdapter(tutorListAdapter);
    }

    private void etTextChangeListener() {
        binding.etSearchTutor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }
    private void filter(String text) {
        filteredList = new ArrayList<>();
        for(UserProfile tutor: tutorList){
            if(tutor.getFirstName().toLowerCase().contains(text.toLowerCase())
                || tutor.getLastName().toLowerCase().contains(text.toLowerCase())
                || tutor.getAreaCovered().toLowerCase().contains(text.toLowerCase())
                || tutor.getMinimumSalary().toLowerCase().contains(text.toLowerCase())
                || tutor.getLocation().toLowerCase().contains(text.toLowerCase())
                || tutor.getInstitute().toLowerCase().contains(text.toLowerCase())
                || tutor.getProfession().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(tutor);
            }
            tutorListAdapter.filterList(filteredList);
        }
    }
    @Override
    public void onTutorClick(int position) {

        if(filteredList!=null){
            filteredList.get(position);
            Intent intent = new Intent(this, TutorViewActivity.class);
            intent.putExtra("tutorProfile", filteredList.get(position));
            startActivity(intent);
        }else{
            tutorList.get(position);
            Intent intent = new Intent(this, TutorViewActivity.class);
            intent.putExtra("tutorProfile", tutorList.get(position));
            startActivity(intent);
        }
    }





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    startActivity(new Intent(StudentSearchActivity.this, StudentProfileActivity.class));
                    return true;
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_message:
                    startActivity(new Intent(StudentSearchActivity.this, StudentMessageActivity.class));
                    return true;
                case R.id.navigation_logout:
                    logoutCurrentUser();
                    return true;
            }
            return false;
        }
    };
    //L O G O U T    D I A L O G
    private void logoutCurrentUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Support.toastMessageShort("Signing out user...", StudentSearchActivity.this);
                        firebaseAuth.signOut();
                        startActivity(new Intent(StudentSearchActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                binding.navView.getMenu().getItem(3).setChecked(false);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
