package menu.catz.aaron.controller;

import com.google.android.gms.maps.model.LatLng;

public class Turret {
    public LatLng pos;
    public String Name = "Turret";
    public Turret (LatLng _POS) {
        pos = _POS;
    }
}
