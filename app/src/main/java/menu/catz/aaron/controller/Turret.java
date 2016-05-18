package menu.catz.aaron.controller;

import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;

public class Turret {
    public LatLng pos;
    public String Name, url;
    public Timer Fire;
    public int Dmg, kills = 0, maxKills = 10;
    public double RoF, Range;
    public Turret (LatLng _POS, String _NAME, int _DAMAGE, double _RANGE, double _RoF, String _URL) {
        pos = _POS;
        Name = _NAME;
        Dmg = _DAMAGE;
        RoF = _RoF;
        Range = _RANGE;
        url = _URL;
        Fire = new Timer();
    }
    public void lvlCheck() {
        if (kills>=maxKills) {
            kills-=maxKills;
            Dmg*=1.1;
            Range*=1.05;
            RoF+=0.05;
        }
    }
}
