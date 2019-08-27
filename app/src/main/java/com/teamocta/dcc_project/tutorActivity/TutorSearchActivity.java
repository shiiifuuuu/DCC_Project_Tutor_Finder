package com.teamocta.dcc_project.tutorActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

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
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;
import com.teamocta.dcc_project.studentActivity.StudentProfileActivity;
import com.teamocta.dcc_project.viewActivity.TuitionViewActivity;

import java.util.ArrayList;

public class TutorSearchActivity extends AppCompatActivity implements TuitionListAdapter.OnTuitionClickListener{

    private ActivityTutorSearchBinding binding;

    private ArrayList<UserProfile> tuitionList, filteredList;
    private TuitionListAdapter tuitionListAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private UserProfile studentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_search);

        init();
        getTuitions();
        configRecyclerView();
        etTextChangeListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.navView.getMenu().getItem(1).setChecked(true);
    }

    private void init() {
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.navView.getMenu().getItem(1).setChecked(true);

        studentProfile = new UserProfile();
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
                    tuitionList.clear();
                    for(DataSnapshot tuitions: dataSnapshot.getChildren()){
                        studentProfile = tuitions.getValue(UserProfile.class);
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

    private void etTextChangeListener() {
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
        filteredList = new ArrayList<>();
        for(UserProfile tuition: tuitionList){
            if(tuition.getFirstName().toLowerCase().contains(text.toLowerCase())
                || tuition.getStudentClass().toLowerCase().contains(text.toLowerCase())
                || tuition.getDepartment().toLowerCase().contains(text.toLowerCase())
                || tuition.getDaysPerWeek().toLowerCase().contains(text.toLowerCase())
                || tuition.getSubjects().toLowerCase().contains(text.toLowerCase())
                || tuition.getSalaryRange().contains(text)
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
        if(filteredList!=null){
            filteredList.get(position);
            Intent intent = new Intent(this, TuitionViewActivity.class);
            intent.putExtra("tuitionProfile", filteredList.get(position));
            startActivity(intent);
        }else{
            tuitionList.get(position);
            Intent intent = new Intent(this, TuitionViewActivity.class);
            intent.putExtra("tuitionProfile", tuitionList.get(position));
            startActivity(intent);
        }
    }


    private Activity activity = TutorSearchActivity.this;
    private Context context = TutorSearchActivity.this;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    startActivity(new Intent(context, TutorProfileActivity.class));
                    return true;
                case R.id.navigation_search:
                    /*startActivity(new Intent(StudentSearchActivity.this, StudentSearchActivity.class));
                    finish();*/
                    return true;
                case R.id.navigation_message:
                    startActivity(new Intent(context, TutorMessageActivity.class));
                    return true;
                case R.id.navigation_logout:
                    Support.logout(firebaseAuth, LoginActivity.class, activity, context);
                    return true;
            }
            return false;
        }
    };
}
