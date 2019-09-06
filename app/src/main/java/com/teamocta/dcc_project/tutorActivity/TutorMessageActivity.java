package com.teamocta.dcc_project.tutorActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.teamocta.dcc_project.databinding.ActivityTutorMessageBinding;
import com.teamocta.dcc_project.mainActivity.LoginActivity;
import com.teamocta.dcc_project.pojo.Chat;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;
import com.teamocta.dcc_project.viewActivity.ActiveSessionActivity;
import com.teamocta.dcc_project.viewActivity.MessageViewActivity;
import com.teamocta.dcc_project.viewActivity.ShowRequestActivity;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static com.teamocta.dcc_project.tutorActivity.TutorProfileActivity.currentTutor;

public class TutorMessageActivity extends AppCompatActivity implements MessageAdapter.OnMessageClickListener {

    private ActivityTutorMessageBinding binding;
    private ActionBar actionBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ArrayList<String> msgSenderId;
    private ArrayList<UserProfile> tuitionList;

    private MessageAdapter tutorMessageAdapter;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_message);

        setActionBar();
        init();
        getMessageSenderList();
        configRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.navView.getMenu().getItem(2).setChecked(true);
    }

    private void setActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle("Messages");
    }

    private void init() {
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.navView.getMenu().getItem(2).setChecked(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userUid = firebaseAuth.getCurrentUser().getUid();

        msgSenderId = new ArrayList<>();
        tuitionList = new ArrayList<>();

        tutorMessageAdapter = new MessageAdapter(tuitionList, this);
    }

    private void getMessageSenderList() {
        DatabaseReference chatRef = databaseReference.child("chatMessage");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tuitionList.clear();
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
                    tuitionList.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserProfile studentProfile = snapshot.getValue(UserProfile.class);

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

    @Override
    public void onMessageClick(int position) {
        Intent intent = new Intent(this, MessageViewActivity.class);
        intent.putExtra("userProfile", tuitionList.get(position));
        startActivity(intent);
    }

    public void btnBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.showRequests:
                Intent intent = new Intent(context, ShowRequestActivity.class);
                intent.putExtra("userProfile", currentTutor);
                startActivity(intent);
                return true;
            case R.id.activeSessions:
                startActivity(new Intent(context, ActiveSessionActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Activity activity = TutorMessageActivity.this;
    private Context context = TutorMessageActivity.this;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    startActivity(new Intent(context, TutorProfileActivity.class));
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(context, TutorSearchActivity.class));
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
