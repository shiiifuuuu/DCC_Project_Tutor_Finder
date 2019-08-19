package com.teamocta.dcc_project.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.adapter.MessageListAdapter;
import com.teamocta.dcc_project.databinding.ActivityMessageViewBinding;
import com.teamocta.dcc_project.pojo.StudentProfile;
import com.teamocta.dcc_project.pojo.TutorProfile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageViewActivity extends AppCompatActivity {

    private ActivityMessageViewBinding binding;
    private AlertDialog alertDialog;

    private String key;

    private String displayName;
    private String userUid, oppositeUid, parent, child;

    private ArrayList<TutorProfile> tutorChatList;
    private ArrayList<StudentProfile> studentChatList;

    private MessageListAdapter messageListAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_view);

        init();
        checkingDatabase();
    }


    private void init() {

        tutorChatList = new ArrayList<>();
        studentChatList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userRef = databaseReference.child("chatMessage");
        userUid = firebaseAuth.getCurrentUser().getUid();
        oppositeUid = getIntent().getStringExtra("msgReceiverUid");
    }

    private void checkingDatabase() {
        toastMessageLong("entered Checking Database");
        userRef.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild(oppositeUid)){
                        setUids(userUid, oppositeUid);
                    }
                }else if(!dataSnapshot.exists()){
                    userRef.child(oppositeUid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                if(dataSnapshot.hasChild(userUid)){
                                    setUids(oppositeUid, userUid);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setUids(String str1, String str2) {
        parent = str1;
        child = str2;
        toastMessageLong("user Uid " + parent);
        toastMessageLong("opposite Uid " + child);
    }

    public void btnSendMessageClicked(View view) {
        Date currentTime = Calendar.getInstance().getTime();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("chats", binding.etTypeMessage.getText().toString());
        if(parent!=null && child!=null){
            DatabaseReference chatRef = databaseReference.child("chatMessage").child(parent)
                .child(child);
            chatRef.child(currentTime.toString()).setValue(userMap);
        }else{
            DatabaseReference chatRef = databaseReference.child("chatMessage").child(userUid)
                    .child(oppositeUid);
            chatRef.child(currentTime.toString()).setValue(userMap);
        }
    }
    private void getChatMessage() {

    }

    //A L E R T   D I A L O G   B O X
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageViewActivity.this);
        builder.setMessage(message).setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        //Closing Alert Dialog use this (alertDialog.cancel();)
    }

    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
