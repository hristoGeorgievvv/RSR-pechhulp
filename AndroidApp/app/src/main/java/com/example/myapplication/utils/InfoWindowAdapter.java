package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater layoutInflater;

    public InfoWindowAdapter(Activity activity) {
        this.layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = layoutInflater.inflate(R.layout.map_custom_info_window, null);
        ((TextView) view.findViewById(R.id.txv_custom_info_window)).setText(marker.getTitle());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
