package menu.catz.aaron.controller;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class Enemy {
    public LatLng pos;
    public int Health, maxHealth, atckSpeed, dmg, cash, EXP;
    private String name;
    private double speed;
    public Timer attack;
    GroundOverlayOptions ground;
    Enemy (LatLng _POS, JSONObject obj) {
        pos = _POS;
        load(obj);
        //GroundOverlayOptions newarkMap = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.newark_nj_1922)).position(pos, 1000f);
        attack = new Timer();
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
    private void load(JSONObject obj) {
        try {
            maxHealth = Health = obj.getInt("Health");
            speed = obj.getDouble("Speed");
            atckSpeed = obj.getInt("AtckSpd");
            name = obj.getString("Name");
            dmg = obj.getInt("Damage");
            cash = obj.getInt("Money");
            EXP = obj.getInt("EXP");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void lvlUp() {
        maxHealth*=1.15;
        Health = maxHealth;
        dmg *= 1.2;
        speed *= 1.025;
    }
}
