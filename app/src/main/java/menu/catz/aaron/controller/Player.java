package menu.catz.aaron.controller;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class Player {
    GPSTracker gps;
    public LatLng pos;
    public int Health=200, maxHealth=200, View = 2, Level = 1, cash = 1000, EXP=0, maxEXP=100;
    Context context;
    Player (Context _CONTEXT) {
        context = _CONTEXT;
        pos = new LatLng(0,0);
        gps = new GPSTracker(context, this);
        updateLocation();
    }
    public void updateLocation() {
        if (gps.pos!=null)
        pos = gps.pos;
    }
    private void lvlUp() {
        maxHealth*=1.15;
        Health = maxHealth;
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
}
