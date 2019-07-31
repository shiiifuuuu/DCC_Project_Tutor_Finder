package com.teamocta.dcc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StudentHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
    }
}

/*
mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
        TutorInformation tInfo = new TutorInformation();
        tInfo.setFirstName(ds.child(userID).getValue(TutorInformation.class).getFirstName());

        binding.tvName.setText(tInfo.getFirstName());
        }
        }

@Override
public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });*/
