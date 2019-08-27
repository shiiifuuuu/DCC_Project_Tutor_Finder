package com.teamocta.dcc_project.pojo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.teamocta.dcc_project.mainActivity.LoginActivity;

public class Support {

    private static AlertDialog alertDialog;
    //A L E R T   D I A L O G   B O X
    public static void showAlertDialog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public static void cancelAlertDialog(){
        alertDialog.cancel();
    }

    //R E F R E S H   A C T I V I T Y
    public static void refreshActivity(Intent intent, Activity activity) {
        activity.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(intent);
    }

    //L O G O U T    D I A L O G
    public static void logout(final FirebaseAuth firebaseAuth, final Class<LoginActivity> activityClass, final Activity activity, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to log out?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toastMessageShort("Signing out user...", context);
                        firebaseAuth.signOut();
                        activity.startActivity(new Intent(context, activityClass));
                        activity.finishAffinity();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                refreshActivity(activity.getIntent(), activity);
            }
        }).create().show();
    }

    //T O A S T    M E S S A G E
    public static void toastMessageShort(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessageLong(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
