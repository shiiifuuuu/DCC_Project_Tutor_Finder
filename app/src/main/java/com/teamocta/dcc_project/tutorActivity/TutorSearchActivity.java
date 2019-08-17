package com.teamocta.dcc_project.tutorActivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityTutorSearchBinding;

public class TutorSearchActivity extends AppCompatActivity {

    private ActivityTutorSearchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_search);
    }

    public void btnBackClicked(View view) {
    }
}
