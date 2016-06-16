package menu.catz.aaron.controller;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

import menu.catz.aaron.catzmain.R;

public class Enemy {
    public LatLng pos;
    public int Health, maxHealth, atckSpeed, dmg, cash, EXP;
    private String name;
    private double speed;
    public Timer attack;
    public String URL = "";
    private Bitmap bit;
    //public GroundOverlayOptions ground;
    Enemy (JSONObject obj, BitmapTask bitty, Boolean old, LatLng _POS) {
        pos = _POS;
        bit = BitmapFactory.decodeResource(Resources.getSystem(), R.raw.error);
        if (old) {
            loadOld(obj, bitty);
        } else {
            loadNew(obj, bitty);
        }
        //ground = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromBitmap(bit)).position(pos, 1000f, 1000f);
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
    private void loadOld(JSONObject obj, BitmapTask bitty) {
        try {
            atckSpeed = obj.getInt("atckSpeed");
            cash = obj.getInt("cash");
            dmg = obj.getInt("dmg");
            EXP = obj.getInt("EXP");
            Health = obj.getInt("Health");
            maxHealth = obj.getInt("maxHealth");
            URL = obj.getString("URL");
            pos = new LatLng(obj.getDouble("lat"), obj.getDouble("long"));
            bitty.setOnLoadCallback(new BitmapTask().new OnLoadCallback(){
                @Override
                public void onLoad(Bitmap bitmap) {
                    bit = bitmap;
                    //ground.image(BitmapDescriptorFactory.fromBitmap(bit));
                }
            });
            bitty.loadBitmap(URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void loadNew(JSONObject obj, BitmapTask bitty) {
        try {
            maxHealth = Health = obj.getInt("Health");
            speed = obj.getDouble("Speed");
            atckSpeed = obj.getInt("AtckSpd");
            name = obj.getString("Name");
            dmg = obj.getInt("Damage");
            cash = obj.getInt("Money");
            EXP = obj.getInt("EXP");
            URL = obj.getString("URL");
            bitty.setOnLoadCallback(new BitmapTask().new OnLoadCallback(){
                @Override
                public void onLoad(Bitmap bitmap) {
                    bit = bitmap;
                    //ground.image(BitmapDescriptorFactory.fromBitmap(bit));
                }
            });
            bitty.loadBitmap(URL);
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
