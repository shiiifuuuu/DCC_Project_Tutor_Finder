package com.teamocta.dcc_project.tutorActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.teamocta.dcc_project.adapter.TutorMessageAdapter;
import com.teamocta.dcc_project.databinding.ActivityTutorMessageBinding;
import com.teamocta.dcc_project.mainActivity.LoginActivity;
import com.teamocta.dcc_project.pojo.Chat;
import com.teamocta.dcc_project.pojo.StudentProfile;
import com.teamocta.dcc_project.pojo.TutorProfile;
import com.teamocta.dcc_project.studentActivity.StudentMessageActivity;
import com.teamocta.dcc_project.studentActivity.StudentProfileActivity;
import com.teamocta.dcc_project.studentActivity.StudentSearchActivity;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class TutorMessageActivity extends AppCompatActivity {

    private ActivityTutorMessageBinding binding;
    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ArrayList<String> msgSenderId;
    private ArrayList<StudentProfile> tuitionList;

    private TutorMessageAdapter tutorMessageAdapter;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_message);

        init();
        getMessageSenderList();
        configRecyclerView();
    }

    private void init() {
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.navView.getMenu().getItem(2).setChecked(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userUid = firebaseAuth.getCurrentUser().getUid();

        msgSenderId = new ArrayList<>();
        tuitionList = new ArrayList<>();

        tutorMessageAdapter = new TutorMessageAdapter(tuitionList);
    }

    private void getMessageSenderList() {
        DatabaseReference chatRef = databaseReference.child("chatMessage");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    userList.clear();
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
        DatabaseReference chatRef = databaseReference.child("Student");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        StudentProfile studentProfile = snapshot.getValue(StudentProfile.class);

                        for(String id: msgSenderId){
                            if(studentProfile.getUid().equals(id)){
                                tuitionList.add(studentProfile);
                            }
                        }
                        tutorMessageAdapter.notifyDataSetChanged();
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
        binding.rvMessageList.setAdapter(tutorMessageAdapter);
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
                    startActivity(new Intent(TutorMessageActivity.this, TutorProfileActivity.class));
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(TutorMessageActivity.this, TutorSearchActivity.class));
                    return true;
                case R.id.navigation_message:
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
                        startActivity(new Intent(TutorMessageActivity.this, LoginActivity.class));
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
    //Refresh Current Activity
    public void refreshActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
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
