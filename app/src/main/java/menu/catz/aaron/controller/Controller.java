package menu.catz.aaron.controller;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import menu.catz.aaron.catzmain.JSONLoader;
import menu.catz.aaron.catzmain.R;

public class Controller {
    public Context context;
    public Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Turret> turrets;
    private ArrayList<JSONObject> enemydata;
    private Timer spawn, move;

    public Controller(Context _CONTEXT) {
        context = _CONTEXT;
        player = new Player(context);
        enemies = new ArrayList<>();
        turrets = new ArrayList<>();
        enemydata = new ArrayList<>();
        spawn = new Timer();
        move = new Timer();
        loadEnemy();
        moveEnemy();
        newEnemy();
    }
    public void newTurret(String _NAME, int _DAMAGE, double _RANGE, int _PRICE, double _RoF) {
        if (player.cash >= _PRICE) {
            player.cash -= _PRICE;
            turrets.add(new Turret(player.pos, _NAME, _DAMAGE, _RANGE, _RoF));
            scheduleTAttack();
        }
    }
    private void newEnemy() {
        int enemy = (int) Math.round(Math.random() * (player.Level - 1)) + 1;
        int time = (int) Math.round(Math.random() * 3000) + 2000;
        int type = (int) Math.round(Math.random() * (enemydata.size() - 1));
            LatLng pos;
            double Lat, Lng;
            for (int i = 0; i < enemy; i++) {
                Lat = Math.random() * (player.View);
                Lng = player.View * Math.cos(Math.asin((Lat / player.View)));
                int quad = (int) Math.round(Math.random() * 4);
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

                enemies.add(new Enemy(pos, enemydata.get(type)));
                for (int y = 2; y < player.Level; y += 2) {
                    enemies.get(enemies.size() - 1).lvlUp();
                }
                scheduleEAttack();
            }
            spawn.schedule(new TimerTask() {
                @Override
                public void run() {
                    newEnemy();
                }
            }, time);
        }

    private void loadEnemy() {
        try {
            String jsonString = JSONLoader.parseFileToString(context, "Enemy.json");
            JSONObject obj = new JSONObject(jsonString);
            JSONArray enemies = obj.getJSONArray("Enemies");
            for (int i = 0; i < enemies.length(); i++) {
                enemydata.add(enemies.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    private void scheduleTAttack() {
        turrets.get(turrets.size() - 1).Fire.schedule(new TimerTask() {
            @Override
            public void run() {
                fireTurret(turrets.size() - 1);
            }
        }, Math.round(1000 * (1 / turrets.get(turrets.size() - 1).RoF)));
    }
    private void fireTurret(int index) {
        final int ind = index;
        double dist, cdist = 0;
        int close = -1;
        for (int i = 0; i < enemies.size(); i++) {
            dist = Math.sqrt((Math.pow((turrets.get(index).pos.latitude - enemies.get(i).pos.latitude), 2) + Math.pow((turrets.get(index).pos.longitude - enemies.get(i).pos.longitude), 2)));
            if (dist <= cdist) {
                close = i;
                cdist = dist;
            }
        }
        if (turrets.get(index).Range > cdist && close != -1) {
            MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.pewpew);
            mPlayer.setVolume(100,100);
            mPlayer.start();
            enemies.get(close).Health -= turrets.get(index).Dmg;
            killCheck(close);
        }
        turrets.get(index).Fire.schedule(new TimerTask() {
            @Override
            public void run() {
                fireTurret(ind);
            }
        }, Math.round(1000 * (1 / turrets.get(index).RoF)));
    }
    private void killCheck(int index) {
        if (enemies.get(index).Health <= 0) {
            //String sUrl = "http://soundbible.com/grab.php?id=1986&type=mp3"; //URL for a generic explosion sound
            MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.wilhelm);
            mPlayer.setVolume(100,100);
            mPlayer.start();
            player.cash+= enemies.get(index).cash;
            player.EXP+= enemies.get(index).EXP;
            if (index < (enemies.size()-1)) {
                enemies.get(index).attack.cancel();
                enemies.get(enemies.size() - 1).attack.cancel();
                enemies.add(index, enemies.get(enemies.size() - 1));
                enemies.remove(index + 1);
                enemies.remove(enemies.size()-1);
            } else {
                enemies.get(index).attack.cancel();
                enemies.remove(index);
            }
            player.checkLevel();
        }
    }
    private void scheduleEAttack() {
        enemies.get(enemies.size() - 1).attack.schedule(new TimerTask() {
            @Override
            public void run() {
                enemyAttack(enemies.size() - 1);
            }
        }, Math.round(enemies.get(enemies.size() - 1).atckSpeed));
    }
    private void enemyAttack(int index) {
        final int ind = index;
        if (enemies.get(index).pos.latitude == player.pos.latitude && enemies.get(index).pos.longitude == player.pos.longitude) {
            player.Health-= enemies.get(index).dmg;
            kill();
        }
            enemies.get(index).attack.schedule(new TimerTask() {
            @Override
            public void run() {
                enemyAttack(ind);
            }
        }, Math.round(enemies.get(ind).atckSpeed));
    }
    private void kill() {
        if (player.Health <= 0) {
            System.exit(69);
        }
    }
}
//Latitude from -85 to 85
//Longitude from -180 to 180