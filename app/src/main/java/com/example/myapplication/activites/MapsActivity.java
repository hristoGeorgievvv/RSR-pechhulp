package com.example.myapplication.activites;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.R;
import com.example.myapplication.utils.InfoWindowAdapter;
import com.example.myapplication.utils.LocationTrack;
import com.example.myapplication.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private IntentFilter locationServiceIntentFilter;
    private ImageView locationObtainingImage;
    private GoogleMap mMap;
    private Marker marker;

    public static final String ACTION_NEW_LOCATION_ = "newLocation";

    LocationTrack locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationObtainingImage = (ImageView)(findViewById(R.id.maps_location_obtaining));

        locationServiceIntentFilter = new IntentFilter();
        locationServiceIntentFilter.addAction(ACTION_NEW_LOCATION_);
        locationTrack = new LocationTrack(MapsActivity.this);
        bindToLocationService();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(locationBroadcastReceiver, locationServiceIntentFilter);
    }

    private ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            locationTrack = ((LocationTrack.LocalBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    private void bindToLocationService() {
        Intent intent = new Intent(this, LocationTrack.class);
        bindService(intent, locationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * onReceive method is called when LocationTrack sends new location.
     */

    private BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (null == action)
                return;

            //Only checking actions from LocationService
            if (action.equals(MapsActivity.ACTION_NEW_LOCATION_)) {
                final Location location = intent.getParcelableExtra("location");
                setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                locationObtainingImage.setVisibility(View.GONE);

            }
        }
    };

    /**
     * Callback triggered when map is ready and set the settings for the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMyLocationEnabled(false);
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(this));
    }

    /**
     * Update the camera and marker on the location.
     */
    public void setLocation(LatLng latLng) {
        if (mMap == null)
            return;
        if (marker == null) {
            marker = mMap.addMarker(createInitMarkerOptions(latLng));
            marker.showInfoWindow();
        }
        Address address = getAdress(latLng);

        if (address != null) {
            setTitleOfMarker(address);
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();
        CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
        marker.setPosition(latLng);
        marker.showInfoWindow();
        mMap.animateCamera(cu);

    }

    /**
     * Set the corresponding title of the Marker with address information.
     */
    private void setTitleOfMarker(Address address) {
        String addressStreet = address.getAddressLine(0).split(",")[0];
        String postalCode = address.getPostalCode();
        String city = address.getLocality();
        System.out.println("setTitleOfMarker");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(addressStreet + "," + postalCode + ", " + city);
        marker.setTitle(stringBuilder.toString());
    }


    /**
     * Get the address of the current location.
     *
     * @param latLng location of the user
     * @return address of current location
     */
    private Address getAdress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            System.out.println("getAdress");
            return addresses.get(0);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Set the settings for the marker.
     *
     * @param latLng location of the user
     */
    private MarkerOptions createInitMarkerOptions(LatLng latLng) {
        System.out.println("createInit");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        markerOptions.position(latLng);
        markerOptions.infoWindowAnchor(0.5f, -0.3f);
        return markerOptions;
    }


    @Override
    protected void onPause() {
        super.onPause();
        locationTrack.stopListener();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    public void btnPopup(View view) {
        findViewById(R.id.maps_call_popup).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_maps_open_popup).setVisibility(View.GONE);
    }

    public void cancelDial(View view) {
        findViewById(R.id.maps_call_popup).setVisibility(View.GONE);
        findViewById(R.id.btn_maps_open_popup).setVisibility(View.VISIBLE);
    }

    public void btnMainScreen(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void maps_call(View view) {
        Utils.dialNumber(this, getString(R.string.telephoneNumber));
    }
}