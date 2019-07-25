package com.teamocta.dcc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {

    private EditText etEmail;
    private String userEmail;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        init();
    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnResetPassClicked(View view) {
        userEmail=etEmail.getText().toString();
        if(userEmail.equals("")){
            toastMessageShort("Field Empty!!");
        }else{
            firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        toastMessageShort("Email Not Found!!");
                    }else{
                        toastMessageLong("Password sent to your Email.");
                    }
                }
            });
        }
    }

    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
