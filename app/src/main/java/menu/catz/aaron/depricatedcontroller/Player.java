package menu.catz.aaron.depricatedcontroller;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class Player {
    Controller control;
    Context context;
    public LatLng pos;
    public int maxHealth, Health, Damage, Defence, Money, EXP, Level, EXPLevel;
    double radius;
    GPSTracker gps;

    Player (Context _CONTEXT, Controller _CONTROL) {
        control = _CONTROL;
        context = _CONTEXT;
        gps = new GPSTracker(context, this);
        //render();
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
        pos = new LatLng(/*gps.location.getLatitude(), gps.location.getLongitude()*/1,1);
    }
    /*private void render() {
        control.mMap.addMarker(new MarkerOptions().position(pos).title(String.valueOf(Health)+ "/" + String.valueOf(maxHealth)));
        control.mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }*/
}
