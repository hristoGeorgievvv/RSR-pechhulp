package com.example.myapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activites.MainActivity;

public class Utils {

    /**
     * Checks if user is connected.
     */
    public static boolean isNetowrkAvailable(Context contex) {
        try {
            ConnectivityManager manager = (ConnectivityManager) contex.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null) {
                networkInfo = manager.getActiveNetworkInfo();
            }

            return networkInfo != null && networkInfo.isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Creates an alert dialog with information about disabled network.
     */
    public static AlertDialog buildAlertMessageNoInternet(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.error_network_message)
                .setTitle(R.string.error_network_title)
                .setCancelable(false)
                .setPositiveButton(R.string.error_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.cancel_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    /**
     * Creates an alert dialog with information about disabled GPS.
     */
    public static AlertDialog buildAlertMessageGPSnotEnabled(final MainActivity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.gps_Disabled)
                .setTitle(R.string.gps_disabled_title)
                .setCancelable(false)
                .setPositiveButton(R.string.error_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.cancel_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    /**
     * Checks if GPS is enabled.
     */
    public static boolean checkGPSenabled(Context context) {
        final LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Open dialog menu with the pass telephone number.
     */
    public static void dialNumber(Context context, String telephoneNumber) {
        Uri number = Uri.parse("tel:" + telephoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        context.startActivity(callIntent);
    }


    /**
     * Creates alert dialog for information policy of the application.
     */
    public static AlertDialog buildAlertMessageInfo(final Context activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(R.string.info_description)
                .setTitle(R.string.info_title)
                .setCancelable(false)
                .setPositiveButton(R.string.info_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().getAttributes();

        Button btn = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        btn.setTextSize(20);

        TextView textView = alert.findViewById(android.R.id.message);
        Linkify.addLinks(textView, Linkify.ALL);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return alert;
    }


}
