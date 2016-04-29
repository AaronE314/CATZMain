package menu.catz.aaron.controller;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class Player {
    GPSTracker gps;
    public LatLng pos;
    public int Health=200, maxHealth=200, View = 1, Level = 1;
    Context context;
    Player (Context _CONTEXT) {
        context = _CONTEXT;
        pos = new LatLng(0,0);
        //gps = new GPSTracker(context, this);
    }
    public void updateLocation() {
        pos = gps.pos;
    }
}
