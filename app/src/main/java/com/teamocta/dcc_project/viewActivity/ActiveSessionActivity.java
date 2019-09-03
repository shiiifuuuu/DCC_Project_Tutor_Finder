package com.teamocta.dcc_project.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.adapter.ActiveSessionAdapter;
import com.teamocta.dcc_project.databinding.ActivityActiveSessionBinding;
import com.teamocta.dcc_project.pojo.HireService;
import com.teamocta.dcc_project.pojo.Support;

import java.util.ArrayList;

import static com.teamocta.dcc_project.adapter.ShowRequestAdapter.STATUS_ACCEPTED;

public class ActiveSessionActivity extends AppCompatActivity {

    public static String snapshotKey;

    private ActivityActiveSessionBinding binding;
    private ActionBar actionBar;

    private DatabaseReference databaseReference;
    private String userUid;
    private ArrayList<HireService> hiredList;
    private ActiveSessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_session);

        init();
        getData();
        configRecyclerView();
    }

    private void init() {
        actionBar = getSupportActionBar();
        actionBar.setTitle("History and Feedback");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        hiredList = new ArrayList<>();
    }

    private void getData() {
        DatabaseReference reference = databaseReference.child("hireRequest");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        HireService hiredProfile = snapshot.getValue(HireService.class);
                        if(hiredProfile.getStatus()!=null){
                            if(hiredProfile.getReceiver().equals(userUid) && hiredProfile.getStatus().equals(STATUS_ACCEPTED)){
                                hiredList.add(hiredProfile);
                                snapshotKey = snapshot.getKey();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void configRecyclerView() {
        adapter = new ActiveSessionAdapter(hiredList);
        binding.rvActiveSession.setLayoutManager(new LinearLayoutManager(this));
        binding.rvActiveSession.setAdapter(adapter);
    }



}
