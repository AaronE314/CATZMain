package menu.catz.aaron.controller;

import com.google.android.gms.maps.model.LatLng;

public class Enemy {
    public LatLng pos;
    public int Health = 100, maxHealth = 100;
    private double speed = 0.01;
    Enemy (LatLng _POS) {
        pos = _POS;
    }
    public void move (LatLng _POS) {
        double dLat, dLong, sLat, sLong, tLat, tLong;
        dLat = Math.abs(pos.latitude - _POS.latitude);
        dLong = Math.abs(pos.longitude - _POS.longitude);
        sLat = speed * (Math.sin(Math.atan((dLat/dLong))));
        sLong = speed * (Math.cos(Math.atan((dLat/dLong))));
        tLat = pos.latitude;
        tLong = pos.longitude;
        if (sLat > dLat) {
            sLat = dLat;
        } if (sLong > dLong) {
            sLong = dLong;
        }
        if (pos.latitude > _POS.latitude) {
            tLat-=sLat;
        } else {
            tLat+=sLat;
        } if (pos.longitude > _POS.longitude) {
            tLong-=sLong;
        } else {
            tLong+=sLong;
        }
        pos = new LatLng(tLat, tLong);
    }
}
