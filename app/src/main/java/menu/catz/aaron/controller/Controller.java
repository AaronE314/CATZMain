package menu.catz.aaron.controller;

import android.content.Context;
import android.media.MediaPlayer;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import menu.catz.aaron.catzmain.JSONLoader;
import menu.catz.aaron.catzmain.MainActivity;
import menu.catz.aaron.catzmain.R;

public class Controller {
    public Context context;
    public Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Turret> turrets;
    public ArrayList<String> ids;
    public ArrayList<Integer> costs;
    public BitmapTask bitty;
    private ArrayList<JSONObject> enemydata;
    private Timer spawn, move;

    public Controller(Context _CONTEXT, MainActivity maps) {
        context = _CONTEXT;
        player = new Player(context, maps);
        enemies = new ArrayList<>();
        turrets = new ArrayList<>();
        enemydata = new ArrayList<>();
        ids = new ArrayList<>();
        costs = new ArrayList<>();
        spawn = new Timer();
        move = new Timer();
        bitty = new BitmapTask();
        loadUpgrades();
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
            killCheck(close, ind);
        }
        turrets.get(index).Fire.schedule(new TimerTask() {
            @Override
            public void run() {
                fireTurret(ind);
            }
        }, Math.round(1000 * (1 / turrets.get(index).RoF)));
    }
    private void killCheck(int index, int ind) {
        if (enemies.get(index).Health <= 0) {
            //String sUrl = "http://soundbible.com/grab.php?id=1986&type=mp3"; //URL for a generic explosion sound
            MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.wilhelm);
            mPlayer.setVolume(100,100);
            mPlayer.start();
            if (index < (enemies.size()-1)) {
                player.cash+= enemies.get(index).cash;
                player.EXP+= enemies.get(index).EXP;
                enemies.get(index).attack.cancel();
                enemies.get(enemies.size() - 1).attack.cancel();
                enemies.add(index, enemies.get(enemies.size() - 1));
                enemies.remove(index + 1);
                enemies.remove(enemies.size()-1);
            } else if(index == (enemies.size()-1 )) {
                player.cash+= enemies.get(index).cash;
                player.EXP+= enemies.get(index).EXP;
                enemies.get(index).attack.cancel();
                enemies.remove(index);
            }
            turrets.get(ind).kills++;
            turrets.get(ind).lvlCheck();
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
    private void loadUpgrades() {
        try {
            String jsonString = JSONLoader.parseFileToString(context, "Upgrades.json");
            JSONObject obj = new JSONObject(jsonString);
            JSONArray upgrades = obj.getJSONArray("Upgrades");
            for (int i = 0; i < upgrades.length(); i++) {
                ids.add(upgrades.getJSONObject(i).getString("Id"));
                costs.add(upgrades.getJSONObject(i).getInt("Cost"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void kill() {
        if (player.Health <= 0) {
            System.exit(69);
        }
    }
    public int plusHealth() {
        int index = -1;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals("plusHealth")) {
                index = i;
                break;
            }
        }
        if (player.cash >= costs.get(index)) {
            player.cash-=costs.get(index);
            player.Health+=(player.maxHealth*0.20);
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
        }
        return costs.get(index);
    }
    public int plusView() {
        int index = -1;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals("plusView")) {
                index = i;
                break;
            }
        }
        if (player.cash >= costs.get(index)) {
            player.cash-=costs.get(index);
            player.View+=1;
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
        }
        player.circly.radius(player.distFrom());
        return costs.get(index);
    }
    public int plusDmg() {
        int index = -1;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals("plusDmg")) {
                index = i;
                break;
            }
        }
        if (player.cash >= costs.get(index)) {
            player.cash-=costs.get(index);
            for (int i = 0; i < turrets.size(); i++) {
                turrets.get(i).Dmg = (int) Math.round(turrets.get(i).Dmg*1.25);
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
        }
        return costs.get(index);
    }
    public int plusRange() {
        int index = -1;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals("plusRange")) {
                index = i;
                break;
            }
        }
        if (player.cash >= costs.get(index)) {
            player.cash-=costs.get(index);
            for (int i = 0; i < turrets.size(); i++) {
                turrets.get(i).Range += 0.5;
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
        }
        return costs.get(index);
    }
    public int plusKills() {
        int index = -1;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals("plusKills")) {
                index = i;
                break;
            }
        }
        if (player.cash >= costs.get(index)) {
            player.cash-=costs.get(index);
            for (int i = 0; i < turrets.size(); i++) {
                turrets.get(i).kills += (int) Math.round(turrets.get(i).maxKills*0.25);
                turrets.get(i).lvlCheck();
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
        }
        return costs.get(index);
    }
    public int plusRoF() {
        int index = -1;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals("plusRoF")) {
                index = i;
                break;
            }
        }
        if (player.cash >= costs.get(index)) {
            player.cash-=costs.get(index);
            for (int i = 0; i < turrets.size(); i++) {
                turrets.get(i).RoF += 0.05;
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
        }
        return costs.get(index);
    }
    public void plusEXP(int cost) {
        if (player.cash >= cost) {
            player.cash-=cost;
            player.EXP+= (cost/player.Level);
            player.checkLevel();
        }
    }
}
//Latitude from -85 to 85
//Longitude from -180 to 180