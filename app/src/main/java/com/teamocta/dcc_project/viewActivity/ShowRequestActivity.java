package com.teamocta.dcc_project.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.adapter.ShowRequestAdapter;
import com.teamocta.dcc_project.databinding.ActivityShowRequestBinding;
import com.teamocta.dcc_project.pojo.Chat;
import com.teamocta.dcc_project.pojo.HireService;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.ArrayList;

public class ShowRequestActivity extends AppCompatActivity {

    public static String snapshotKey;

    private ActivityShowRequestBinding binding;
    private ActionBar actionBar;

    private DatabaseReference databaseReference;
    private UserProfile userProfile;
    private ShowRequestAdapter showRequestAdapter;
    private ArrayList<HireService> requSenderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_request);

        getIntentExtras();
        init();
        getRequestSenderList();
        configRecyclerView();
    }

    //User is on receiver id, user will look for who send him request i.e SenderId and
    //then show those ids to recycler view.

    private void getIntentExtras() {
        //student or tutor profile
        userProfile = (UserProfile) getIntent().getSerializableExtra("userProfile");
    }

    private void init() {
        actionBar = getSupportActionBar();
        actionBar.setTitle("Request List");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        requSenderList = new ArrayList<>();
        showRequestAdapter = new ShowRequestAdapter(requSenderList);
    }

    private void getRequestSenderList() {
        DatabaseReference requestRef = databaseReference.child("hireRequest");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    requSenderList.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        HireService requSender = snapshot.getValue(HireService.class);
                        if(requSender.getReceiver().equals(userProfile.getUid())){
                            requSenderList.add(requSender);
                            snapshotKey = snapshot.getKey();
                        }
                        showRequestAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configRecyclerView() {
        binding.rvRequestList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRequestList.setAdapter(showRequestAdapter);
    }

    public void btnBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
