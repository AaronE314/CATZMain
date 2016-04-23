package menu.catz.aaron.controller;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    Context context;
    Player player;
    ArrayList<Zombie> zombies;
    ArrayList<Turret> turrets;
    private Timer Update;

    Controller(Context _CONTEXT, Class _CLASS) {
        onNewActivity(_CONTEXT, _CLASS);
        player = new Player(context, this);
        zombies = new ArrayList<>();
        Update = new Timer();
        Update.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 100);
    }
    public void onNewActivity(Context _CONTEXT, Class _CLASS) {
        context = _CONTEXT;
        updateContext();
    }
    public void newEnemy (String _TYPE) {
        zombies.add(new Zombie(this, _TYPE));
    }
    public void newTurret (String _TYPE) { turrets.add(new Turret(this, _TYPE)); }
    public void killCheck (int _ZOMBIE) {
            if (zombies.get(_ZOMBIE).Health <= 0) {
                player.Money+=zombies.get(_ZOMBIE).Money;
                player.EXP+=zombies.get(_ZOMBIE).EXP;
                player.checkLevel();
                zombies.remove(_ZOMBIE);
            }
        }
    private void updateContext () {
        player.gps.stopUsingGPS();
        player.context = context;
        player.gps.context = context;
        player.gps.getLocation();
    }
    private void update() {
        zombieWalk();
        render();
    }
    private void zombieWalk(){
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).move();
        }
    }
    private void render() {
      /*TODO have variable for the map
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(player.pos).title(String.valueOf(player.Health)+ "/" + String.valueOf(player.maxHealth)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(player.pos));
        for (int i = 0; i < zombies.size(); i++) {
             mMap.addMarker(new MarkerOptions().position(zombies.get(i).pos).title(String.valueOf(zombies.get(i).Health) + "/" + String.valueOf(zombies.get(i).maxHealth)));
        }
        for (int i = 0; i < turrets.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(turrets.get(i).pos).title(String.valueOf(turrets.get(i).Name)));
        }*/
    }
}
