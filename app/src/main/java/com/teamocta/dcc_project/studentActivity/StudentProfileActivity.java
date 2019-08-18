package com.teamocta.dcc_project.studentActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.databinding.ActivityStudentProfileBinding;
import com.teamocta.dcc_project.mainActivity.LoginActivity;
import com.teamocta.dcc_project.pojo.StudentProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StudentProfileActivity extends AppCompatActivity {

    private ActivityStudentProfileBinding binding;
    private static final int CAMERA_CODE=0;
    private static final int GALLERY_CODE=1;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private String uid;

    private StudentProfile currentStudent;
    private Uri mImageUri, downloadUri;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_profile);

        init();
        getStudentProfile();
    }

    private void init() {
        binding.navView.getMenu().getItem(0).setChecked(true);
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();

        builder = new AlertDialog.Builder(this);
        currentStudent = new StudentProfile();
    }

    //-------ReadFromDatabase-------
    private void getStudentProfile() {
        DatabaseReference studentRef = databaseReference.child("Student").child(uid);
        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentStudent = dataSnapshot.getValue(StudentProfile.class);
                    setData();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        /*String email = dataSnapshot.child("Email").getValue().toString();
                toastMessageShort(email);*/
    }
    private void setData() {
        binding.tvUserName.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());
        binding.tvUserEmail.setText(currentStudent.getEmail());
        binding.tvUserMobile.setText(currentStudent.getMobile());
        binding.tvUserLocation.setText(currentStudent.getLocation() + ", Dhaka");
        binding.tvUserGender.setText(currentStudent.getGender());
        if(currentStudent.getImageUrl()!=null){
            Glide.with(this).load(currentStudent.getImageUrl()).into(binding.ivProfilePic);
        }
        binding.tvUserClass.setText(currentStudent.getStudentClass());
        binding.tvDepartment.setText(currentStudent.getDepartment());
        binding.tvUserInstitute.setText(currentStudent.getInstitute());
        binding.tvUserAddress.setText(currentStudent.getStreetAddress() + ", "
                + currentStudent.getAreaAddress() + ", " + "Dhaka, "
                + currentStudent.getZipCode());
        binding.tvGuardianName.setText(currentStudent.getGuardianName());
        binding.tvGuardianMobile.setText(currentStudent.getGuardianMobile());

        binding.tvDaysPerWeek.setText(currentStudent.getDaysPerWeek());
        binding.tvSubjects.setText(currentStudent.getSubjects());
        binding.tvSalaryRange.setText(currentStudent.getSalaryRange());
        binding.tvAdditionalInfo.setText(currentStudent.getAdditionalInfo());
    }
    //-------ReadFromDatabase-------

    //-------PROFILE PIC UPDATE-------
    public void btnUpdatePicClicked(View view) {

        builder.setMessage("Choose Image using...").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CODE);
            }
        }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            mImageUri = data.getData();
            saveImage();

        }else if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            binding.ivProfilePic.setImageBitmap(imageBitmap);
            mImageUri = getImageUri(getApplicationContext(), imageBitmap);
            saveImage();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void saveImage(){
        binding.ivProfilePic.setImageURI(mImageUri);
        builder.setMessage("Save Image?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadPic();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                refreshActivity();
            }
        }).create().show();
    }
    private void uploadPic() {
        if(mImageUri!=null){
            final StorageReference filePath = storageRef.child("Student").child(uid).child("proPic." + getFileExtension(mImageUri));
            filePath.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        downloadUri = task.getResult();
                        //toastMessageLong(downloadUri.toString());
                        //Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("imageUrl", downloadUri.toString());
                        DatabaseReference studentReference = databaseReference.child("Student");
                        studentReference.child(uid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                toastMessageShort("Image upload complete");
                                binding.ivProfilePic.setImageURI(downloadUri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.getMessage();
                            }
                        });
                    }
                }
            });
        }else{
            toastMessageShort("no file selected!");
        }
    }
    //-------PROFILE PIC UPDATE-------

    //-------EDIT PROFILE-------
    public void updateProfileClicked(View view) {
        Intent intent = new Intent(StudentProfileActivity.this, UpdateStudentProfileActivity.class);

        intent.putExtra("firstName", currentStudent.getFirstName());
        intent.putExtra("lastName", currentStudent.getLastName());
        intent.putExtra("mobile", binding.tvUserMobile.getText().toString());
        intent.putExtra("studentClass", binding.tvUserClass.getText().toString());
        intent.putExtra("department", currentStudent.getDepartment());
        intent.putExtra("institute", binding.tvUserInstitute.getText().toString());
        intent.putExtra("streetAddress", currentStudent.getStreetAddress());
        intent.putExtra("areaAddress", currentStudent.getAreaAddress());
        intent.putExtra("zipCode", currentStudent.getZipCode());
        intent.putExtra("guardianName", binding.tvGuardianName.getText().toString());
        intent.putExtra("guardianMobile", binding.tvGuardianMobile.getText().toString());

        intent.putExtra("daysPerWeek", binding.tvDaysPerWeek.getText().toString());
        intent.putExtra("subjects", binding.tvSubjects.getText().toString());
        intent.putExtra("salaryRange", binding.tvSalaryRange.getText().toString());
        intent.putExtra("additionalInfo", binding.tvAdditionalInfo.getText().toString());

        startActivity(intent);

    }
    //-------EDIT PROFILE-------

    //N A V I G A T I O N   I T E M    L I S T E N E R
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    /*startActivity(new Intent(StudentProfileActivity.this, StudentProfileActivity.class));
                    finish();*/
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(StudentProfileActivity.this, StudentSearchActivity.class));
                    return true;
                case R.id.navigation_message:
                    startActivity(new Intent(StudentProfileActivity.this, StudentMessageActivity.class));
                    return true;
                case R.id.navigation_logout:
                    logoutCurrentUser();
                    return true;
            }
            return false;
        }
    };
    //L O G O U T    D I A L O G
    private void logoutCurrentUser() {
        builder.setMessage("Are you sure you want to log out?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toastMessageShort("Signing out user...");
                        firebaseAuth.signOut();
                        startActivity(new Intent(StudentProfileActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();
    }
    //A L E R T   D I A L O G   B O X
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfileActivity.this);
        builder.setMessage(message).setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        //Closing Alert Dialog use this (alertDialog.cancel();)
    }
    //Refresh Current Activity
    public void refreshActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
