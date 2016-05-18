package menu.catz.aaron.depricatedcontroller;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

@Deprecated public class Player {
    @Deprecated Controller control;
    @Deprecated Context context;
    @Deprecated public LatLng pos;
    @Deprecated  public int maxHealth, Health, Damage, Defence, Money, EXP, Level, EXPLevel;
    @Deprecated double radius;
    @Deprecated GPSTracker gps;

    @Deprecated Player (Context _CONTEXT, Controller _CONTROL) {
        control = _CONTROL;
        context = _CONTEXT;
        gps = new GPSTracker(context, this);
        //render();
    }
    @Deprecated private void lvlUp() {
        maxHealth*=1.15;
        Health = maxHealth;
        EXP-=EXPLevel;
        EXPLevel*=1.25;
        Damage*=1.1;
        Defence*=1.05;
        Level++;
    }
    @Deprecated public void checkLevel () {
        if (EXP >= EXPLevel) {
            lvlUp();
        }
    }
    @Deprecated public void updateLocation(){
        pos = new LatLng(/*gps.location.getLatitude(), gps.location.getLongitude()*/1,1);
    }
    /*private void render() {
        control.mMap.addMarker(new MarkerOptions().position(pos).title(String.valueOf(Health)+ "/" + String.valueOf(maxHealth)));
        control.mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }*/
}
