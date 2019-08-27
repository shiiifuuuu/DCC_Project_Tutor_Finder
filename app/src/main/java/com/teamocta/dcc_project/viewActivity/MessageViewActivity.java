package com.teamocta.dcc_project.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.adapter.ChatListAdapter;
import com.teamocta.dcc_project.databinding.ActivityMessageViewBinding;
import com.teamocta.dcc_project.pojo.Chat;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class MessageViewActivity extends AppCompatActivity {

    private ActivityMessageViewBinding binding;

    private Boolean isUserTutor, isUserStudent;
    private String userUid, oppositeUid;
    private String receiverUid, senderUid;
    private String imageUrl;
    private UserProfile userProfile;
    private Chat chat;
    private ArrayList<Chat> chatList;

    private ChatListAdapter chatListAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_view);

        getIntentExtras();
        init();
        checkUserType();
        getMessage();
        configRecyclerView();
        //binding.rvMessageList.smoothScrollToPosition(chatList.size());
    }

    private void getIntentExtras() {
        userProfile = (UserProfile) getIntent().getSerializableExtra("userProfile");
    }

    private void init() {
        binding.tvReceiverName.setText(userProfile.getFirstName());
        chatList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        oppositeUid = userProfile.getUid();
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
                    getTutorInfo(senderUid);
                    /*toastMessageShort("user is tutor");
                    toastMessageShort("opposite is student");*/
                    isUserStudent = false;
                }
                else{
                    isUserStudent = true;
                    senderUid = userUid;
                    receiverUid = oppositeUid;
                    getStudentInfo(senderUid);
                    /*toastMessageShort("user is student");
                    toastMessageShort("opposite is tutor");*/
                    isUserTutor = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }
    private void getTutorInfo(String tutorId) {
        databaseReference.child("Tutor").child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userProfile = dataSnapshot.getValue(UserProfile.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getStudentInfo(String studentId) {
        databaseReference.child("Student").child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userProfile = dataSnapshot.getValue(UserProfile.class);
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
                        chat = data.getValue(Chat.class);
                        if(chat.getSender().equals(senderUid) && chat.getReceiver().equals(receiverUid) ||
                                chat.getSender().equals(receiverUid) && chat.getReceiver().equals(senderUid)){
                            chatList.add(chat);
                        }
                        chatListAdapter.notifyDataSetChanged();
                    }
                    binding.rvMessageList.smoothScrollToPosition(chatListAdapter.getItemCount());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configRecyclerView() {
        chatListAdapter = new ChatListAdapter(chatList);
        binding.rvMessageList.setHasFixedSize(true);
        binding.rvMessageList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessageList.setAdapter(chatListAdapter);
    }


    public void btnSendMessageClicked(View view) {
        Support.toastMessageLong("Button is clicked", MessageViewActivity.this);
        String msg = binding.etTypeMessage.getText().toString();
        if(isUserTutor){
            if(!msg.equals("")){
                imageUrl = userProfile.getImageUrl();
                sendMessage(msg);
            }else{
                Support.toastMessageLong("you can't send empty message!", MessageViewActivity.this);
            }
        }else if(isUserStudent){
            if(!msg.equals("")){
                imageUrl = userProfile.getImageUrl();
                sendMessage(msg);
            }else{
                Support.toastMessageLong("you can't send empty message!", MessageViewActivity.this);
            }
        }
        binding.etTypeMessage.setText("");
    }

    private void sendMessage(String msg){

        DatabaseReference chatRef = databaseReference.child("chatMessage");

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", senderUid);
        hashMap.put("receiver", receiverUid);
        hashMap.put("msg", msg);
        hashMap.put("imageUrl", imageUrl);

        chatRef.push().setValue(hashMap);
    }

    public void btnBackClicked(View view) {
        onBackPressed();
    }
}