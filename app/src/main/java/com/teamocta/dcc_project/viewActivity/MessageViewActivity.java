package com.teamocta.dcc_project.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.adapter.MessageListAdapter;
import com.teamocta.dcc_project.databinding.ActivityMessageViewBinding;
import com.teamocta.dcc_project.pojo.Chat;
import com.teamocta.dcc_project.pojo.StudentProfile;
import com.teamocta.dcc_project.pojo.TutorProfile;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class MessageViewActivity extends AppCompatActivity {

    private ActivityMessageViewBinding binding;
    private AlertDialog alertDialog;

    private Boolean isUserTutor, isUserStudent;
    private String userUid, oppositeUid;
    private String receiverUid, senderUid;
    private String imageUrl;
    private StudentProfile studentProfile;
    private TutorProfile tutorProfile;
    private ArrayList<Chat> chatList;

    private MessageListAdapter messageListAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_view);

        init();
        checkUserType();
        //setMessage();
        getMessage();
        configRecyclerView();
    }

    private void init() {

        chatList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        oppositeUid = getIntent().getStringExtra("msgReceiverUid");
        messageListAdapter = new MessageListAdapter(chatList);
    }


    private void checkUserType() {
        DatabaseReference tutorReference = databaseReference.child("Tutor");
        tutorReference.orderByChild("uid").equalTo(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    isUserTutor = true;
                    senderUid = userUid;
                    receiverUid = oppositeUid;

                    toastMessageShort("user is tutor");
                    toastMessageShort("opposite is student");
                    isUserStudent = false;

                    getTutorInfo();
                }
                else{
                    isUserStudent = true;
                    senderUid = userUid;
                    receiverUid = oppositeUid;

                    toastMessageShort("user is student");
                    toastMessageShort("opposite is tutor");
                    isUserTutor = false;

                    getStudentInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }
    private void getStudentInfo() {
        databaseReference.child("Student").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    studentProfile = dataSnapshot.getValue(StudentProfile.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getTutorInfo() {
        databaseReference.child("Tutor").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tutorProfile = dataSnapshot.getValue(TutorProfile.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMessage() {
        databaseReference.child("chatMessage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    chatList.clear();
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        Chat chat = data.getValue(Chat.class);
                        if(chat.getSender().equals(senderUid) && chat.getReceiver().equals(receiverUid) ||
                                chat.getSender().equals(receiverUid) && chat.getReceiver().equals(senderUid)){
                            chatList.add(chat);
                        }
                        messageListAdapter.notifyDataSetChanged();
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
        binding.rvMessageList.setAdapter(messageListAdapter);
    }

    public void btnSendMessageClicked(View view) {
        toastMessageLong("Button is clicked");
        String msg = binding.etTypeMessage.getText().toString();
        if(isUserTutor){
            if(!msg.equals("")){
                imageUrl = tutorProfile.getImageUrl();
                sendMessage(senderUid, receiverUid, msg, imageUrl);
            }else{
                toastMessageLong("you can't send empty message!");
            }
        }else if(isUserStudent){
            if(!msg.equals("")){
                imageUrl = studentProfile.getImageUrl();
                sendMessage(senderUid, receiverUid, msg, imageUrl);
            }else{
                toastMessageLong("you can't send empty message!");
            }
        }
        binding.etTypeMessage.setText("");
        //setData();
    }

    private void sendMessage(String sender, String receiver, String msg, String imageUrl){

        DatabaseReference chatRef = databaseReference.child("chatMessage");

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg", msg);
        hashMap.put("imageUrl", imageUrl);

        chatRef.push().setValue(hashMap);
    }

    /*private void setData() {
        Date timeStamp = Calendar.getInstance().getTime();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("msg", binding.etTypeMessage.getText().toString());


        if(isUserTutor){
            toastMessageLong("user tutor");
            databaseReference.child("Tutor").child(userUid).child("chatMessage").child(tutor_student).child("sent")
                    .child(timeStamp.toString()).setValue(userMap);
            databaseReference.child("Student").child(oppositeUid).child("chatMessage").child(student_tutor).child("received")
                    .child(timeStamp.toString()).setValue(userMap);
        }else if(isUserStudent){
            toastMessageLong("user student");
            databaseReference.child("Student").child(userUid).child("chatMessage").child(student_tutor).child("sent")
                    .child(timeStamp.toString()).setValue(userMap);
            databaseReference.child("Tutor").child(oppositeUid).child("chatMessage").child(tutor_student).child("received")
                    .child(timeStamp.toString()).setValue(userMap);
        }else{
            toastMessageLong("User type could not be verified");
        }

    }

    private void getData() {

        if(isUserTutor){
            DatabaseReference userRef = databaseReference.child("Tutor").child(userUid).child("chatMessage");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot data : dataSnapshot.getChildren()){

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }*/


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



/*private void checkingDatabase() {
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

    }*/