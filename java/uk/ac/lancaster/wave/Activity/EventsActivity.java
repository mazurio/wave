package uk.ac.lancaster.wave.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import uk.ac.lancaster.wave.R;

public class EventsActivity extends FragmentActivity {
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.events_activity);

        /**
         * Setup status bar.
         */
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintColor(getResources().getColor(R.color.branding_light));

        /**
         * Setup action bar.
         */
        this.actionBar = getActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setTitle("Lecture");

        /**
         * Setup location fragment using Google Maps.
         */
        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.location)).getMap();

        LatLng sydney = new LatLng(54.010948, -2.784424);

//        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19));

        map.addMarker(new MarkerOptions()
                .title("Faraday Building")
                .snippet("SCC.311 Distributed Systems\nLecture")
                .position(sydney));
    }
}
