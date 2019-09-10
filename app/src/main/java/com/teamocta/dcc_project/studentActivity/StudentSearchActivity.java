package com.teamocta.dcc_project.studentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private ActionBar actionBar;

    private UserProfile tutorProfile;
    private ArrayList<UserProfile> tutorList, filteredList;
    private TutorListAdapter tutorListAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_search);

        setActionBar();
        init();
        getTutors();
        configRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.navView.getMenu().getItem(1).setChecked(true);
    }

    private void setActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle("Tutor Search");
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
                tutorList.clear();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity_menu, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        return true;
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
                || tutor.getProfession().toLowerCase().contains(text.toLowerCase())
                || tutor.getTeachingSubjects().toLowerCase().contains(text.toLowerCase())){
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


    private Activity activity = StudentSearchActivity.this;
    private Context context = StudentSearchActivity.this;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    startActivity(new Intent(context, StudentProfileActivity.class));
                    return true;
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_message:
                    startActivity(new Intent(context, StudentMessageActivity.class));
                    return true;
                case R.id.navigation_logout:
                    Support.logout(firebaseAuth, LoginActivity.class, activity, context);
                    return true;
            }
            return false;
        }
    };
}
