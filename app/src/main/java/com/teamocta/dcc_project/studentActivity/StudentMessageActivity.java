package com.teamocta.dcc_project.studentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
import com.teamocta.dcc_project.adapter.MessageAdapter;
import com.teamocta.dcc_project.databinding.ActivityStudentMessageBinding;
import com.teamocta.dcc_project.mainActivity.LoginActivity;
import com.teamocta.dcc_project.pojo.Chat;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;
import com.teamocta.dcc_project.viewActivity.MessageViewActivity;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class StudentMessageActivity extends AppCompatActivity implements MessageAdapter.OnMessageClickListener {

    private ActivityStudentMessageBinding binding;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ArrayList<String> msgSenderId;
    private ArrayList<UserProfile> tutorList;

    private MessageAdapter studentMessageAdapter;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_student_message);

        init();
        getMessageSenderList();
        configRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.navView.getMenu().getItem(2).setChecked(true);
    }

    private void init() {
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.navView.getMenu().getItem(2).setChecked(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userUid = firebaseAuth.getCurrentUser().getUid();

        msgSenderId = new ArrayList<>();
        tutorList = new ArrayList<>();

        studentMessageAdapter = new MessageAdapter(tutorList, this);
    }

    private void getMessageSenderList() {
        DatabaseReference chatRef = databaseReference.child("chatMessage");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tutorList.clear();
                    for(DataSnapshot chats: dataSnapshot.getChildren()){
                        Chat chat = chats.getValue(Chat.class);
                        if(chat.getSender().equals(userUid)){
                            msgSenderId.add(chat.getReceiver());
                        }if (chat.getReceiver().equals(userUid)){
                            msgSenderId.add(chat.getSender());
                        }
                    }

                    removeDuplicates();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void removeDuplicates() {
        // delete duplicates (if any) from 'ArrayList'
        msgSenderId = new ArrayList<>(new LinkedHashSet<>(msgSenderId));
        DatabaseReference chatRef = databaseReference.child("Tutor");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserProfile tutorProfile = snapshot.getValue(UserProfile.class);

                        for(String id: msgSenderId){
                            if(tutorProfile.getUid().equals(id)){
                                tutorList.add(tutorProfile);
                            }
                        }
                        studentMessageAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void configRecyclerView() {
        binding.rvMessageList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessageList.setAdapter(studentMessageAdapter);
    }

    @Override
    public void onMessageClick(int position) {
        Intent intent = new Intent(this, MessageViewActivity.class);
        intent.putExtra("userProfile", tutorList.get(position));
        startActivity(intent);

    }


    public void fabHiredClicked(View view) {
        
    }


    private Activity activity = StudentMessageActivity.this;
    private Context context = StudentMessageActivity.this;
    public void btnBackClicked(View view) {
        onBackPressed();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    startActivity(new Intent(context, StudentProfileActivity.class));
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(context, StudentSearchActivity.class));
                    return true;
                case R.id.navigation_message:
                    return true;
                case R.id.navigation_logout:
                    Support.logout(firebaseAuth, LoginActivity.class, activity, context);
                    return true;
            }
            return false;
        }
    };
}
