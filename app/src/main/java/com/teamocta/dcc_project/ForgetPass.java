package com.teamocta.dcc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.teamocta.dcc_project.databinding.ActivityForgetPassBinding;

public class ForgetPass extends AppCompatActivity {

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
