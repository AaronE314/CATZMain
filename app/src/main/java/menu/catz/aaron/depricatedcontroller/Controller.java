package menu.catz.aaron.depricatedcontroller;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Timer;

@Deprecated public class Controller {

    @Deprecated Context context;
    @Deprecated public Player player;
    @Deprecated public ArrayList<Zombie> zombies;
    @Deprecated ArrayList<Turret> turrets;
    @Deprecated private Timer Update;
    @Deprecated private Timer Spawn;
    @Deprecated GoogleMap mMap;
    @Deprecated
    public Controller(Context _CONTEXT, GoogleMap _MAP) {
        mMap = _MAP;
        context = _CONTEXT;
        player = new Player(context, this);
        zombies = new ArrayList<>();
        turrets = new ArrayList<>();
        Update = new Timer();
        /*Update.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 100);*/
        Spawn = new Timer();
        /*Update.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                newEnemy("1");
            }
        }, 0, 1);*/
    }
    @Deprecated public void newEnemy (String _TYPE) {
        zombies.add(new Zombie(this, _TYPE));
        //render();
    }
    @Deprecated public void newTurret (String _TYPE) {
        turrets.add(new Turret(this, _TYPE));
        //render();
    }
    @Deprecated public void killCheck (int _ZOMBIE) {
            if (zombies.get(_ZOMBIE).Health <= 0) {
                player.Money+=zombies.get(_ZOMBIE).Money;
                player.EXP+=zombies.get(_ZOMBIE).EXP;
                player.checkLevel();
                zombies.remove(_ZOMBIE);
            }
        }
    @Deprecated private void update() {
        zombieWalk();
        //render();
    }
    @Deprecated private void zombieWalk(){
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).move();
        }
    }
    /*private void render() {
        //mMap.clear();
        mMap.addMarker(new MarkerOptions().position(player.pos).title(String.valueOf(player.Health)+ "/" + String.valueOf(player.maxHealth)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(player.pos));
        for (int i = 0; i < zombies.size(); i++) {
             mMap.addMarker(new MarkerOptions().position(zombies.get(i).pos).title(String.valueOf(zombies.get(i).Health) + "/" + String.valueOf(zombies.get(i).maxHealth)));
        }
        for (int i = 0; i < turrets.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(turrets.get(i).pos).title(String.valueOf(turrets.get(i).Name)));
        }
    }*/
}
