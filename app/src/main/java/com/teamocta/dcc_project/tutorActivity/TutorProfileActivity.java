package com.teamocta.dcc_project.tutorActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.teamocta.dcc_project.databinding.ActivityTutorProfileBinding;
import com.teamocta.dcc_project.mainActivity.LoginActivity;
import com.teamocta.dcc_project.pojo.TutorProfile;

import java.util.HashMap;
import java.util.Map;

public class TutorProfileActivity extends AppCompatActivity {

    private ActivityTutorProfileBinding binding;
    private static final int CAMERA_CODE=0;
    private static final int GALLERY_CODE=1;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private String uid;
    private TutorProfile currentTutor;
    private Uri mImageUri, downloadUri;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_profile);

        init();
        showData();
    }

    private void init() {
        binding.navView.getMenu().getItem(0).setChecked(true);
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        uid = firebaseAuth.getCurrentUser().getUid();

        builder = new AlertDialog.Builder(this);
        currentTutor = new TutorProfile();
    }

    //-------ReadFromDatabase-------
    private void showData() {
        DatabaseReference tutorRef = databaseReference.child("Tutor").child(uid);
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentTutor = dataSnapshot.getValue(TutorProfile.class);
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
        binding.tvUserName.setText(currentTutor.getFirstName() + " " + currentTutor.getLastName());
        binding.tvUserEmail.setText(currentTutor.getEmail());
        binding.tvUserMobile.setText(currentTutor.getMobile());
        binding.tvUserLocation.setText(currentTutor.getLocation());
        binding.tvUserGender.setText(currentTutor.getGender());
        if(currentTutor.getImageUrl()!=null){
            Glide.with(this).load(currentTutor.getImageUrl()).into(binding.ivProfilePic);
        }
        binding.tvUserInstitute.setText(currentTutor.getInstitute());
        binding.tvTuitionType.setText(currentTutor.getTuitionType());
        if(binding.tvTuitionType.getText().toString().equals("Available")){
            binding.tvTuitionType.setTextColor(getResources().getColor(R.color.oneTimeTuitionTrue));
        }else{
            binding.tvTuitionType.setTextColor(getResources().getColor(R.color.oneTimeTuitionFalse));
        }
        binding.tvExperience.setText(currentTutor.getExperience());
        binding.tvProfession.setText(currentTutor.getProfession());
        binding.tvAreaCovered.setText(currentTutor.getAreaCovered());
        binding.tvDaysPerWeek.setText(currentTutor.getDaysPerWeek());
        binding.tvMinimumSalary.setText(currentTutor.getMinimumSalary());
        binding.tvTeachingSubjects.setText(currentTutor.getTeachingSubjects());
    }
    //-------ReadFromDatabase-------

    //-------PROFILE PIC UPDATE-------
    public void btnUpdatePicClicked(View view) {
        builder.setMessage("Choose Image using...").setCancelable(true).setPositiveButton("Camera", new DialogInterface.OnClickListener() {
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
        if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            binding.ivProfilePic.setImageBitmap(imageBitmap);
            mImageUri = getImageUri(getApplicationContext(), imageBitmap);

            saveImage();

        }else if(requestCode == GALLERY_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            mImageUri = data.getData();

            saveImage();

        }else{
            builder.setMessage("Could not get any image.!!").create().show();
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
            final StorageReference filePath = storageRef.child("Tutor").child(uid).child("proPic." + getFileExtension(mImageUri));
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
                        DatabaseReference studentReference = databaseReference.child("Tutor");
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
        Intent intent = new Intent(TutorProfileActivity.this, UpdateTutorProfileActivity.class);

        intent.putExtra("firstName", currentTutor.getFirstName());
        intent.putExtra("lastName", currentTutor.getLastName());
        intent.putExtra("mobile", binding.tvUserMobile.getText().toString());
        intent.putExtra("profession", binding.tvProfession.getText().toString());
        intent.putExtra("institute", binding.tvUserInstitute.getText().toString());
        intent.putExtra("gender", binding.tvUserGender.getText().toString());
        intent.putExtra("location", binding.tvUserLocation.getText().toString());
        intent.putExtra("experience", binding.tvExperience.getText().toString());
        intent.putExtra("tuitionType", binding.tvTuitionType.getText().toString());
        intent.putExtra("daysPerWeek", binding.tvDaysPerWeek.getText().toString());
        intent.putExtra("areaCovered", binding.tvAreaCovered.getText().toString());
        intent.putExtra("teachingSubjects", binding.tvTeachingSubjects.getText().toString());
        intent.putExtra("minimumSalary", binding.tvMinimumSalary.getText().toString());
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
                    startActivity(new Intent(TutorProfileActivity.this, TutorSearchActivity.class));
                    return true;
                case R.id.navigation_message:
                    startActivity(new Intent(TutorProfileActivity.this, TutorMessageActivity.class));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toastMessageShort("Signing out user...");
                        firebaseAuth.signOut();
                        startActivity(new Intent(TutorProfileActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();
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
    //A L E R T   D I A L O G   B O X
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TutorProfileActivity.this);
        builder.setMessage(message).setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        //Closing Alert Dialog use this (alertDialog.cancel();)
    }
    //T O A S T    M E S S A G E
    private void toastMessageShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void toastMessageLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
