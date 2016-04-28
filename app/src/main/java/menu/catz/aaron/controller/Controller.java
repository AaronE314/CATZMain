package menu.catz.aaron.controller;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    public Context context;
    public Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Turret> turrets;
    private Timer spawn;
    public Controller (Context _CONTEXT) {
        //context = _CONTEXT;
        //player = new Player(context);
        enemies = new ArrayList<>();
        //turrets = new ArrayList<>();
        //spawn = new Timer();
        //newEnemy();
        grid();
    }
    public void newTurret() {
        turrets.add(new Turret(player.pos));
    }
    private void newEnemy() {
        int enemy = (int) Math.round(Math.random()*(player.Level-1))+1;
        int time = (int) Math.round(Math.random()*3000)+2000;
        LatLng pos;
        double Lat, Lng;
        for (int i = 0; i < 1000; i++) {
                Lat = Math.random() * (player.View);
            Lng = player.View * Math.cos(Math.asin((Lat/player.View)));
            int quad = (int) Math.round(Math.random()*4);
            if (quad == 1) {
                Lat = player.pos.latitude + Lat;
                Lng = player.pos.longitude + Lng;
            } else if (quad == 2) {
                Lat = player.pos.latitude - Lat;
                Lng = player.pos.longitude + Lng;
            } else if (quad == 3) {
                Lat = player.pos.latitude + Lat;
                Lng = player.pos.longitude - Lng;
            } else {
                Lat = player.pos.latitude - Lat;
                Lng = player.pos.longitude - Lng;
            }
            pos = new LatLng(Lat, Lng);
            enemies.add(new Enemy(pos));
        }
        spawn.schedule(new TimerTask() {
            @Override
            public void run() {
                newEnemy();
            }
        }, time);
    }
    private void grid () {
        for (double i = -85; i <=85; i+=0.01) {
            for (double y = -180; y <= 180; y+=0.01) {
                enemies.add(new Enemy(new LatLng(i, y)));
            }
        }
    }
}
//Latitude from -85 to 85
//Longitude from -180 to 180