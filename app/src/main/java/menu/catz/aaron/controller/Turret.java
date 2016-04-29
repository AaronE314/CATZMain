package menu.catz.aaron.controller;

import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;

public class Turret {
    public LatLng pos;
    public String Name = "Turret";
    public Timer Fire;
    public int Dmg = 10;
    public double RoF = 0.25, Range = 1;
    public Turret (LatLng _POS) {
        pos = _POS;
        Fire = new Timer();
    }
}
