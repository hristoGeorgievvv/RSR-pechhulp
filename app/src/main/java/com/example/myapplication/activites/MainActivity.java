package com.example.myapplication.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.utils.Utils;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AlertDialog infoAlertDialog;

    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkGPSAndInternetAvailability();
    }

    /**
     * Checks if GPS is enabled and Internet connectivity.
     * Creates alert dialog if there are any errors.
     */
    private boolean checkGPSAndInternetAvailability() {

        if (alertDialog != null && alertDialog.isShowing())
            return false;
        if (!Utils.isNetowrkAvailable(this)) {
            (alertDialog = Utils.buildAlertMessageNoInternet(this)).show();
            return false;
        } else if (!Utils.checkGPSenabled(this)) {
            (alertDialog = Utils.buildAlertMessageGPSnotEnabled(this)).show();
            return false;
        } else return true;
    }

    public void btnMapScreen(View view) {
        if (checkGPSAndInternetAvailability()) ;
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void btnInfoScreen(View view) {
        Utils.buildAlertMessageInfo(this).show();
    }
}
