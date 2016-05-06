package menu.catz.aaron.controller;

import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;

public class Turret {
    public LatLng pos;
    public String Name;
    public Timer Fire;
    public int Dmg;
    public double RoF, Range;
    public Turret (LatLng _POS, String _NAME, int _DAMAGE, double _RANGE, double _RoF) {
        pos = _POS;
        Name = _NAME;
        Dmg = _DAMAGE;
        RoF = _RoF;
        Range = _RANGE;
        Fire = new Timer();
    }
}
