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
    private Timer spawn, move;
    public Controller (Context _CONTEXT) {
        context = _CONTEXT;
        player = new Player(context);
        enemies = new ArrayList<>();
        turrets = new ArrayList<>();
        spawn = new Timer();
        move = new Timer();
        moveEnemy();
        newEnemy();
    }
    public void newTurret() {
        turrets.add(new Turret(new LatLng(player.pos.latitude, player.pos.longitude+1.01)));
        scheduleAttack();
        turrets.add(new Turret(new LatLng(player.pos.latitude, player.pos.longitude-1.01)));
        scheduleAttack();
        turrets.add(new Turret(new LatLng(player.pos.latitude+1.01, player.pos.longitude)));
        scheduleAttack();
        turrets.add(new Turret(new LatLng(player.pos.latitude-1.01, player.pos.longitude)));
        scheduleAttack();
    }
    private void newEnemy() {
        int enemy = (int) Math.round(Math.random()*(player.Level-1))+1;
        int time = (int) Math.round(Math.random()*3000)+2000;
        LatLng pos;
        double Lat, Lng;
        for (int i = 0; i < enemy; i++) {
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
    private void moveEnemy() {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).move(player.pos);
        }
        move.schedule(new TimerTask() {
            @Override
            public void run() {
                moveEnemy();
            }
        }, 50);
    }
    private void scheduleAttack() {
        turrets.get(turrets.size()-1).Fire.schedule(new TimerTask() {
            @Override
            public void run() {
                fireTurret(turrets.size()-1);
            }
        }, Math.round(1000*(1/turrets.get(turrets.size()-1).RoF)));
    }
    private void fireTurret(int index) {
        final int ind = index;
        double dist, cdist = 0;
        int close = -1;
        for (int i = 0; i < enemies.size(); i++) {
            dist = Math.sqrt((Math.pow((turrets.get(index).pos.latitude-enemies.get(i).pos.latitude), 2) + Math.pow((turrets.get(index).pos.longitude-enemies.get(i).pos.longitude), 2)));
            if (dist >= cdist) {
                close = i;
                dist = cdist;
            }
        }
        if (turrets.get(index).Range > cdist && close != -1) {
            enemies.get(close).Health-=turrets.get(index).Dmg;
            check(close);
        }
        turrets.get(index).Fire.schedule(new TimerTask() {
            @Override
            public void run() {
                fireTurret(ind);
            }
        }, Math.round(1000*(1/turrets.get(index).RoF)));
    }
    private void check (int index) {
        if (enemies.get(index).Health <= 0) {
            enemies.remove(index);
        }
    }
}
//Latitude from -85 to 85
//Longitude from -180 to 180