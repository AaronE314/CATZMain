package menu.catz.aaron.controller;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


import menu.catz.aaron.catzmain.MainActivity;

public class Player {
    GPSTracker gps;
    public LatLng pos;
    public int Health=200, maxHealth=200, View = 2, Level = 1, cash = 1000, EXP=0, maxEXP=100;
    public CircleOptions circly;
    Context context;
    Player (Context _CONTEXT, MainActivity maps) {
        context = _CONTEXT;
        pos = new LatLng(0,0);
        gps = new GPSTracker(context, this, maps);
        circly = new CircleOptions();
        circly.center(pos);
        circly.radius(distFrom());
        circly.strokeColor(Color.MAGENTA);
        updateLocation();
    }
    public void updateLocation() {
        if (gps.pos!=null)
        pos = gps.pos;
        circly.center(pos);
    }
    private void lvlUp() {
        maxHealth*=1.15;
        Health += maxHealth*0.25;
        EXP-=maxEXP;
        maxEXP*=1.25;
        Level++;
        checkLevel();
    }
    public void checkLevel () {
        if (EXP >= maxEXP) {
            lvlUp();
        }
    }
        public double distFrom () {
            double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
            double conv = 1609.34;
            double dLat = Math.toRadians(2);
            double dLng = Math.toRadians(0);
            double sindLat = Math.sin(dLat / 2);
            double sindLng = Math.sin(dLng / 2);
            double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(pos.latitude)) * Math.cos(Math.toRadians((pos.latitude+2)));
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = earthRadius * c * conv;
            return dist;
    }
}
