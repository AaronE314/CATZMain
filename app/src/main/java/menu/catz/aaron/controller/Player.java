package menu.catz.aaron.controller;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;

public class Player {
    Controller control;
    Context context;
    LatLng pos;
    int maxHealth, Health, Damage, Defence, Money, EXP, Level, EXPLevel;
    double radius;
    GPSTracker gps;

    Player (Context _CONTEXT, Controller _CONTROL) {
        control = _CONTROL;
        context = _CONTEXT;
        gps = new GPSTracker(context, this);
        //TODO create a call to get online data
        //TODO could have possibility for local save
    }
    private void lvlUp() {
        maxHealth*=1.15;
        Health = maxHealth;
        EXP-=EXPLevel;
        EXPLevel*=1.25;
        Damage*=1.1;
        Defence*=1.05;
        Level++;
    }
    public void checkLevel () {
        if (EXP >= EXPLevel) {
            lvlUp();
        }
    }
    public void updateLocation(){
        pos = new LatLng(gps.location.getLatitude(), gps.location.getLongitude());
    }
}
