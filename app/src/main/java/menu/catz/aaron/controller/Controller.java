package menu.catz.aaron.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import menu.catz.aaron.catzmain.DriveZAPI;
import menu.catz.aaron.catzmain.JSONLoader;
import menu.catz.aaron.catzmain.MainActivity;
import menu.catz.aaron.catzmain.R;
import menu.catz.aaron.catzmain.FileHandler;

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
    private DateFormat df;

    public Controller(Context _CONTEXT) {
        context = _CONTEXT;
        df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        enemies = new ArrayList<>();
        turrets = new ArrayList<>();
        enemydata = new ArrayList<>();
        ids = new ArrayList<>();
        costs = new ArrayList<>();
        spawn = new Timer();
        move = new Timer();
        bitty = new BitmapTask();
    }
    public void mapLoaded(MainActivity maps, Boolean newgame) {
        player = new Player(context, maps);
        if (newgame) {
            newGame();
        } else {
            loadGame();
        }
        loadUpgrades();
        loadEnemy();
        moveEnemy();
        newEnemy();
    }
    public void newTurret(String _NAME, int _DAMAGE, double _RANGE, int _PRICE, double _RoF, String _URL, Bitmap _BITMAP) {
        if (player.cash >= _PRICE) {
            player.cash -= _PRICE;
            turrets.add(new Turret(player.pos, _NAME, _DAMAGE, _RANGE, _RoF, _URL, _BITMAP));
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Creating New Turret With ID: " + (turrets.size()-1));
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
                enemies.add(new Enemy(enemydata.get(type), bitty, false, pos));
                for (int y = 2; y < player.Level; y += 2) {
                    enemies.get(enemies.size() - 1).lvlUp();
                }
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Creating New Enemy With Id: " + (enemies.size()-1));
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
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Enemy Data Loaded");
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
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Scheduled New Attack For Turret " + (turrets.size() - 1));
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
            try {
                MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.pewpew);
                mPlayer.setVolume(100, 100);
                mPlayer.start();
                enemies.get(close).Health -= turrets.get(index).Dmg;
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Turret " + index + " Attacked Enemy " + close + " For " + turrets.get(index).Dmg);
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Enemy " + close + " Has " + enemies.get(close).Health + "Health");
                killCheck(close, ind);
            } catch (Exception e) {}
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
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Enemy " + index + " Was Killed By Turret " + ind);
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Turret " + ind + " Has " + turrets.get(ind).kills + " Kills");
        }
    }
    private void scheduleEAttack() {
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Schedule Attack For Enemy " + (enemies.size()-1));
        enemies.get(enemies.size() - 1).attack.schedule(new TimerTask() {
            @Override
            public void run() {
                enemyAttack(enemies.size() - 1);
            }
        }, Math.round(enemies.get(enemies.size() - 1).atckSpeed));
    }
    private void enemyAttack(int index) {
        final int ind = index;
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Enemy " + index + " Is Attempting To Attack The Player");
        if (enemies.get(index).pos.latitude == player.pos.latitude && enemies.get(index).pos.longitude == player.pos.longitude) {
            player.Health-= enemies.get(index).dmg;
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Enemy " + index + " Has Attacked The Player");
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.Health + " Health");
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
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Upgrade Data Loaded");
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
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has Died");
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
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.Health + " Health");
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.cash + " Money");
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
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.View + " View");
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.cash + " Money");
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
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Turrt " + i + " Deals " + turrets.get(i).Dmg + " Damage");
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.cash + " Money");
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
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Turret " + i + " Has " + turrets.get(i).Range + " Range");
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.cash + " Money");
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
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Turret " + i + " Has " + turrets.get(i).kills + " Kills");
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Turret " + i + " Is Level" + turrets.get(i).Level);
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.cash + " Money");
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
                System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Turret " + i + " Has a RoF of " + turrets.get(i).RoF);
            }
            costs.set(index,(int) Math.round(costs.get(index)*1.25));
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.cash + " Money");
        }
        return costs.get(index);
    }
    public void plusEXP(int cost) {
        if (player.cash >= cost) {
            player.cash-=cost;
            player.EXP+= (cost/player.Level);
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Gained " + (cost/player.Level) + " EXP");
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Player Has " + player.cash + " Money");
            player.checkLevel();
        }
    }
    private void newGame() {
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": New Game");
        String json = JSONLoader.parseFileToString(context, "Player.json");
        try {
            JSONObject obj = new JSONObject(json);
            player.Health = player.maxHealth = obj.getInt("Health");
            player.cash = obj.getInt("Cash");
            player.View = obj.getInt("View");
            player.Level = obj.getInt("Level");
            player.maxEXP = obj.getInt("EXP");
            player.EXP = 0;
            Save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void loadGame() {
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Loading");
        try {
            FileHandler file = new FileHandler(context, "Data.json");
            JSONObject obj = new JSONObject(file.readFromFile());
            JSONArray ar;
            JSONObject obj1 = obj.getJSONObject("Player");
            player.cash = obj1.getInt("Cash");
            player.Health = obj1.getInt("Health");
            player.maxHealth = obj1.getInt("maxHealth");
            player.View = obj1.getDouble("View");
            player.EXP = obj1.getInt("EXP");
            player.maxEXP = obj1.getInt("maxEXP");
            player.Level = obj1.getInt("Level");
            player.pos = new LatLng(obj1.getDouble("Lat"), obj1.getDouble("Long"));
            ar = obj.getJSONArray("Enemies");
            for (int i = 0; i < ar.length(); i++) {
                obj1 = ar.getJSONObject(i);
                enemies.add(new Enemy(obj1, bitty, true, new LatLng(0,0)));
            }
            ar = obj.getJSONArray("Turrets");
            for (int i = 0; i < ar.length(); i++) {
                obj1 = ar.getJSONObject(i);
                turrets.add(new Turret(new LatLng(obj1.getDouble("lat"), obj1.getDouble("long")), obj1.getString("Name"), obj1.getInt("Dmg"), obj1.getDouble("Range"), obj1.getDouble("RoF"), obj1.getString("URL"), BitmapFactory.decodeResource(Resources.getSystem(), R.raw.error)));
                turrets.get(i).additionalData(obj.getInt("kills"), obj.getInt("maxKills"), obj.getInt("Level"));
            }
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Save() {
        System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Saving");
        try {
            JSONObject obj = new JSONObject();
            JSONObject players = new JSONObject();
            JSONArray Aturrets = new JSONArray();
            JSONArray Aenemies = new JSONArray();
            players.put("maxHealth", player.maxHealth);
            players.put("Health", player.Health);
            players.put("View", player.View);
            players.put("Level", player.Level);
            players.put("maxEXP", player.maxEXP);
            players.put("EXP", player.EXP);
            players.put("Cash", player.cash);
            players.put("Lat", player.pos.latitude);
            players.put("Long", player.pos.longitude);
            for (int i = 0; i < enemies.size(); i++) {
                JSONObject Oenemy = new JSONObject();
                Oenemy.put("atckSpeed", enemies.get(i).atckSpeed);
                Oenemy.put("cash", enemies.get(i).cash);
                Oenemy.put("dmg", enemies.get(i).dmg);
                Oenemy.put("EXP", enemies.get(i).EXP);
                Oenemy.put("Health", enemies.get(i).Health);
                Oenemy.put("maxHealth", enemies.get(i).maxHealth);
                Oenemy.put("lat", enemies.get(i).pos.latitude);
                Oenemy.put("long", enemies.get(i).pos.longitude);
                Oenemy.put("URL", enemies.get(i).URL);
                Aenemies.put(Oenemy);
            }
            for (int i = 0; i < turrets.size(); i++) {
                JSONObject Oturret = new JSONObject();
                Oturret.put("Dmg", turrets.get(i).Dmg);
                Oturret.put("kills", turrets.get(i).kills);
                Oturret.put("maxKills", turrets.get(i).maxKills);
                Oturret.put("Name", turrets.get(i).Name);
                Oturret.put("long", turrets.get(i).pos.longitude);
                Oturret.put("lat", turrets.get(i).pos.latitude);
                Oturret.put("Range", turrets.get(i).Range);
                Oturret.put("RoF", turrets.get(i).RoF);
                Oturret.put("URL", turrets.get(i).url);
                Oturret.put("Level", turrets.get(i).Level);
                Aturrets.put(Oturret);
            }
            obj.put("Player", players);
            obj.put("Enemies", Aenemies);
            obj.put("Turrets", Aturrets);
            FileHandler file = new FileHandler(context, "Data.json");
            file.writeToFile(obj.toString());
            System.out.println("\n" + df.format(Calendar.getInstance().getTime()) + ": Saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
//Latitude from -85 to 85
//Longitude from -180 to 180
