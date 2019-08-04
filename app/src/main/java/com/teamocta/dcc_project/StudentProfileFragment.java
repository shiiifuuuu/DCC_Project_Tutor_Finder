package com.teamocta.dcc_project;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileFragment extends Fragment {
    private ImageButton btnUpdateProfilePic;
    private CircleImageView ivProfilePic;
    private TextView tvUserName;
    private TextView tvUserClass;
    private TextView tvUserDept;
    private TextView tvUserGender;
    private TextView tvUserLocation;
    private TextView tvUserMobile;
    private Text tvUserEmail;

    public StudentProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_student_profile, container, false);

        init();
        btnUpdateProfilePicClicked();

        tvUserName.setText("User name Changed!");
        return view;
    }

    private void init() {
        btnUpdateProfilePic = getView().findViewById(R.id.btnUpdateProfilePic);
        ivProfilePic = getView().findViewById(R.id.ivProfilePic);
        tvUserName = getView().findViewById(R.id.tvUserName);
        tvUserClass = getView().findViewById(R.id.tvUserClass);
        tvUserDept = getView().findViewById(R.id.tvUserDept);
        tvUserGender = getView().findViewById(R.id.tvUserGender);
        tvUserLocation = getView().findViewById(R.id.tvUserLocation);
        tvUserMobile = getView().findViewById(R.id.tvUserMobile);
        tvUserEmail = getView().findViewById(R.id.tvUserEmail);
    }

    private void btnUpdateProfilePicClicked() {
        btnUpdateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
