package menu.catz.aaron.depricatedcontroller;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

@Deprecated public class Zombie {
    @Deprecated Controller control;
    @Deprecated public LatLng pos;
    @Deprecated public int Health, maxHealth, Damage, Defence, Atck, Def, Money, EXP;
    @Deprecated private double Lat, Long, spdLat, spdLong;
    @Deprecated double spdMax= 1;

    @Deprecated  Zombie(Controller _CONTROL, String _TYPE) {
        control = _CONTROL;
        try {zombieLoad(_TYPE);}
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        startPos();
    }
    @Deprecated public void getAttacked (int _DAMAGE) {
        defend(_DAMAGE);
        Health -= Def;
    }
    @Deprecated public void move () {
        getSpdLat();
        getSpdLong();
        Lat = pos.latitude;
        Long = pos.longitude;
        if (spdLat > Math.abs(pos.latitude - control.player.pos.latitude)) {
            spdLat = Math.abs(pos.latitude - control.player.pos.latitude);
        }
        if (spdLong > Math.abs(pos.longitude - control.player.pos.longitude)) {
            spdLong = Math.abs(pos.longitude - control.player.pos.longitude);
        }
        if (pos.latitude > control.player.pos.latitude) {

            Lat -= spdLat;
        } else {
            Lat+= spdLat;
        }
        if (pos.longitude > control.player.pos.longitude) {
            Long-= spdLong;
        } else {
            Long+= spdLong;
        }
        pos = new LatLng(Lat, Long);
    }
    @Deprecated private void getSpdLat (){
        spdLat = Math.abs(spdMax * Math.cos(Math.atan(((Lat - control.player.pos.latitude)/(Long - control.player.pos.longitude)))));
    }
    @Deprecated private void getSpdLong (){
        spdLong = Math.abs(spdMax * Math.sin(Math.atan((Lat - control.player.pos.latitude)/(Long - control.player.pos.longitude))));
    }
    @Deprecated private void attack (int _DEFENCE) {
        Atck = (Damage / _DEFENCE) * 10;
    }
    @Deprecated private void defend (int _DAMAGE) {
        Def = (_DAMAGE / Defence) * 10;
    }
    @Deprecated private void startPos() {
        Random ran = new Random();
        Double angle = ran.nextDouble()*90;
        int quad = ran.nextInt(4);
        Lat = Math.sin(angle)* control.player.radius;
        Long = Math.cos(angle) * control.player.radius;
        if (quad == 0) {
            Lat = control.player.pos.latitude + Lat;
            Long = control.player.pos.longitude + Long;
        } else if (quad == 1) {
            Lat = control.player.pos.latitude - Lat;
            Long = control.player.pos.longitude + Long;
        } else if (quad == 2) {
            Lat = control.player.pos.latitude + Lat;
            Long = control.player.pos.longitude - Long;
        } else {
            Lat = control.player.pos.latitude - Lat;
            Long = control.player.pos.longitude - Long;
        }
        pos = new LatLng(Lat, Long);
    }
    @Deprecated private void zombieLoad(String _TYPE) throws FileNotFoundException, JSONException {
        String jsonString = "";
        Scanner fin = new Scanner(new FileReader("Enemy.json"));
        while (fin.hasNextLine()) {
            jsonString+=fin.nextLine();
        }
        JSONObject obj = new JSONObject(jsonString);
        JSONArray zombies =  obj.getJSONArray("Zombies");
        for (int i = 0; i < zombies.length(); ++i) {
            obj = zombies.getJSONObject(i);
            if (obj.getString("Id").equals(_TYPE)) {
                maxHealth = Health = Integer.valueOf(obj.getString("Health"));
                spdMax = Double.valueOf(obj.getString("Speed"));
                Damage = Integer.valueOf(obj.getString("Damage"));
                Defence = Integer.valueOf(obj.getString("Defence"));
                Money = Integer.valueOf(obj.getString("Money"));
                EXP = Integer.valueOf(obj.getString("EXP"));
            }
        }
    }
}
