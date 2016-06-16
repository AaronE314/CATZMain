package menu.catz.aaron.depricatedcontroller;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

@Deprecated public class Turret {
    @Deprecated Controller control;
    @Deprecated LatLng pos;
    @Deprecated int Damage;
    @Deprecated double Range, RoF;
    @Deprecated private Timer Fire;
    @Deprecated String Name;

    @Deprecated Turret (Controller _CONTROL, String _TYPE) {
        control = _CONTROL;
        try {turretLoad(_TYPE);}
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        startPos();
        Fire = new Timer();
        Fire.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                attack();
            }
        }, 0, Math.round((1000/RoF)));
    }
    @Deprecated public void attack() {
        int target = -1;
        double tdis = -1, dis;
        for (int i = 0; i < control.zombies.size(); i++) {
            dis = Math.sqrt(Math.pow(pos.latitude-control.zombies.get(i).pos.latitude, 2) + Math.pow(pos.longitude-control.zombies.get(i).pos.longitude, 2));
            if (dis > tdis) {
                target = i;
                tdis = dis;
            }
        }
        if (target != -1 && tdis <= Range) {
            control.zombies.get(target).getAttacked(Damage);
            control.killCheck(target);
        }
    }
    @Deprecated private void startPos () { pos = new LatLng(control.player.pos.latitude, control.player.pos.longitude); }
    @Deprecated private void turretLoad(String _TYPE) throws FileNotFoundException, JSONException {
        String jsonString = "";
        Scanner fin = new Scanner(new FileReader("Turrets.json"));
        while (fin.hasNextLine()) {
            jsonString+=fin.nextLine();
        }
        JSONObject obj = new JSONObject(jsonString);
        JSONArray turrets =  obj.getJSONArray("Turrets");
        for (int i = 0; i < turrets.length(); ++i) {
            obj = turrets.getJSONObject(i);
            if (obj.getString("Id").equals(_TYPE)) {
                Damage = obj.getInt("Damage");
                RoF = obj.getDouble("RoF");
                Range = obj.getInt("Range");
                Name = obj.getString("Name");
            }
        }
    }
}
