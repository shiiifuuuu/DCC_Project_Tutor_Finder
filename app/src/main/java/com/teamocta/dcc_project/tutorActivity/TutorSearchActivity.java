package com.teamocta.dcc_project.tutorActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
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
import com.teamocta.dcc_project.adapter.TuitionListAdapter;
import com.teamocta.dcc_project.databinding.ActivityTutorSearchBinding;
import com.teamocta.dcc_project.mainActivity.LoginActivity;
import com.teamocta.dcc_project.pojo.StudentProfile;
import com.teamocta.dcc_project.viewActivity.TuitionViewActivity;

import java.util.ArrayList;

public class TutorSearchActivity extends AppCompatActivity implements TuitionListAdapter.OnTuitionClickListener{

    private ActivityTutorSearchBinding binding;

    private ArrayList<StudentProfile> tuitionList;
    private TuitionListAdapter tuitionListAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private StudentProfile studentProfile;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_search);

        init();
        getTuitions();
        configRecyclerView();
        etTextChangeListner();
    }


    private void init() {
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.navView.getMenu().getItem(1).setChecked(true);

        studentProfile = new StudentProfile();
        tuitionList = new ArrayList<>();
        tuitionListAdapter= new TuitionListAdapter(tuitionList, this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void getTuitions() {
        DatabaseReference tutorRef = databaseReference.child("Student");
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot tuitions: dataSnapshot.getChildren()){
                        studentProfile = tuitions.getValue(StudentProfile.class);
                        tuitionList.add(studentProfile);
                        tuitionListAdapter.notifyDataSetChanged();
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
        binding.rvTutorList.setAdapter(tuitionListAdapter);
    }

    private void etTextChangeListner() {
        binding.etSearchTuition.addTextChangedListener(new TextWatcher() {
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
        ArrayList<StudentProfile> filteredList = new ArrayList<>();
        for(StudentProfile tuition: tuitionList){
            if(tuition.getDaysPerWeek().toLowerCase().contains(text.toLowerCase())
                    || tuition.getSubjects().toLowerCase().contains(text.toLowerCase())
                    || tuition.getSalaryRange().toLowerCase().contains(text.toLowerCase())
                    || tuition.getLocation().toLowerCase().contains(text.toLowerCase())
                    || tuition.getAdditionalInfo().toLowerCase().contains(text.toLowerCase())
                    || tuition.getInstitute().toLowerCase().contains(text.toLowerCase())
                    || tuition.getStudentClass().toLowerCase().contains(text.toLowerCase())
                    || tuition.getAreaAddress().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(tuition);
            }
        }
        tuitionListAdapter.filterList(filteredList);
    }
    @Override
    public void onTuitionClick(int position) {
        tuitionList.get(position);
        Intent intent = new Intent(this, TuitionViewActivity.class);
        sentTuitionInfo(intent, position);
        startActivity(intent);
    }
    private void sentTuitionInfo(Intent intent, int position) {
        intent.putExtra("name",tuitionList.get(position).getFirstName()
                + " " + tuitionList.get(position).getLastName());
        intent.putExtra("mobile", tuitionList.get(position).getMobile());
        intent.putExtra("email", tuitionList.get(position).getEmail());
        intent.putExtra("studentClass", tuitionList.get(position).getStudentClass());
        intent.putExtra("institute", tuitionList.get(position).getInstitute());
        intent.putExtra("gender", tuitionList.get(position).getGender());
        intent.putExtra("location", tuitionList.get(position).getLocation());
        intent.putExtra("department", tuitionList.get(position).getDepartment());
        intent.putExtra("guardianName", tuitionList.get(position).getGuardianName());
        intent.putExtra("guardianMobile", tuitionList.get(position).getGuardianMobile());
        intent.putExtra("streetAddress", tuitionList.get(position).getStreetAddress());
        intent.putExtra("areaAddress", tuitionList.get(position).getAreaAddress());
        intent.putExtra("zipCode", tuitionList.get(position).getZipCode());
        intent.putExtra("daysPerWeek", tuitionList.get(position).getDaysPerWeek());
        intent.putExtra("additionalInfo", tuitionList.get(position).getAdditionalInfo());
        intent.putExtra("subjects", tuitionList.get(position).getSubjects());
        intent.putExtra("salaryRange", tuitionList.get(position).getSalaryRange());
        intent.putExtra("studentPic", tuitionList.get(position).getImageUrl());
    }


    public void btnBackClicked(View view) {
        onBackPressed();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    startActivity(new Intent(TutorSearchActivity.this, TutorProfileActivity.class));
                    return true;
                case R.id.navigation_search:
                    /*startActivity(new Intent(StudentSearchActivity.this, StudentSearchActivity.class));
                    finish();*/
                    return true;
                case R.id.navigation_message:
                    startActivity(new Intent(TutorSearchActivity.this, TutorMessageActivity.class));
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
                        toastMessageShort("Signing out user...");
                        firebaseAuth.signOut();
                        startActivity(new Intent(TutorSearchActivity.this, LoginActivity.class));
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
