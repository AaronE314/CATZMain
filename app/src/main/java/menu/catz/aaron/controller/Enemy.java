package menu.catz.aaron.controller;

import com.google.android.gms.maps.model.LatLng;

public class Enemy {
    public LatLng pos;
    public int Health = 100, maxHealth = 100;
    Enemy (LatLng _POS) {
        pos = _POS;
    }
}
