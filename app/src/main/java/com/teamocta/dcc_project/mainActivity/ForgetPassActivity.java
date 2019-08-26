package com.teamocta.dcc_project.mainActivity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityForgetPassBinding;
import com.teamocta.dcc_project.pojo.Support;

public class ForgetPassActivity extends AppCompatActivity {

    private ActivityForgetPassBinding binding;

    private String userEmail;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_pass);

        init();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnSendEmailClicked(View view) {
        userEmail=binding.etEmail.getText().toString();
        if(userEmail.equals("")){
            Support.toastMessageShort("Field Empty!!",ForgetPassActivity.this);
        }else{
            firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Support.toastMessageShort("Email Not Found!!", ForgetPassActivity.this);
                    }else{
                        Support.toastMessageLong("Password sent to your Email.", ForgetPassActivity.this);
                    }
                }
            });
        }
    }

    public void btnBackClicked(View view) {
        onBackPressed();
    }
}
