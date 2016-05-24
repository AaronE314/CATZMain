package menu.catz.aaron.catzmain;

import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import menu.catz.aaron.fragments.*;
import menu.catz.aaron.controller.*;

//has to implement NavigationView.OnNavigationItemSelectedListener for the navigation drawer
//and have to implement OnMapReadyCallback for map
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    //Video on how to set up fragments with navigation drawer
    //https://www.youtube.com/watch?annotation_id=annotation_2755209737&feature=iv&src_vid=ZQSu48J9TBg&v=tguOfRD8vYo

    //Videos on how to set up the map into the navigation drawer
    //https://www.youtube.com/watch?v=JWk3PXV6O98
    //https://www.youtube.com/watch?v=ZQSu48J9TBg

    SupportMapFragment supportMapFragment;
    public GoogleMap mMap;
    public Controller control;
    private ShopFragment shop;
    private OptionsFragment option;
    private UpgradesFragment upgrades;
    private Toolbar toolbar;
    private android.support.v4.app.FragmentManager sFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creating a new instance of the supportMapFragment
        supportMapFragment = SupportMapFragment.newInstance();

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        //Grab new Game from Starting Activity
        Bundle getBasket = getIntent().getExtras();
        Boolean newGame = getBasket.getBoolean("NewGame");
        System.out.println("New Game = " + newGame);


        //setting up Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //instantiate fragments
        control = new Controller(this, this, newGame);
        shop = new ShopFragment();
        upgrades = new UpgradesFragment();
        option = new OptionsFragment();

        option.setInfo(control, this);
        upgrades.setControl(control);
        //set default fragment
        //FragmentManager fm = getFragmentManager();

        //set location of on map ready function
        supportMapFragment.getMapAsync(this);

        sFm = getSupportFragmentManager();
        sFm.beginTransaction().add(R.id.map, supportMapFragment).commit();
        //fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Step 1: Create a new Fragment manager for switching fragments
        FragmentManager fm = getFragmentManager();

        //Step 2: Create a new Support Fragment Manager
        sFm = getSupportFragmentManager();

        //Get id of item selected
        int id = item.getItemId();

        //hide Support map fragment if it's added, if you don't hide it, the map will always be showing
        if (supportMapFragment.isAdded()) {
            sFm.beginTransaction().hide(supportMapFragment).commit();
        }

        //replaces the Content_frame FrameLayout in Content_main (in layout) with the fragment clicked
        switch (id) {
            case R.id.nav_map:
                //Checking if map is added
                if (!supportMapFragment.isAdded()) {
                    //add map if its not added
                    sFm.beginTransaction().add(R.id.map, supportMapFragment).commit();
                } else {
                    //show map if already added
                    sFm.beginTransaction().show(supportMapFragment).commit();
                }
                break;
            //switching fragments
            case R.id.nav_store:
                fm.beginTransaction().replace(R.id.content_frame, shop).commit();
                shop.setInfo(control, this.getBaseContext());
                break;
            case R.id.nav_upgrades:
                fm.beginTransaction().replace(R.id.content_frame, upgrades).commit();
                break;
            case R.id.nav_options:
                fm.beginTransaction().replace(R.id.content_frame, option).commit();
                break;
            default:
                break;
        }
        //closing the drawer after button is clicked
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Called when map is ready
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        int sSelectedMap = GoogleMap.MAP_TYPE_HYBRID;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.addMarker(new MarkerOptions().position(control.player.pos).title(String.valueOf(control.player.Health) + "/" + String.valueOf(control.player.maxHealth)));
        new Thread() {
            @Override
            public void run() {
                PseudoTimer pT = new PseudoTimer();
                while (true) {
                    pT.Update();
                    if (pT.frameReady(30f)) {
                        new PseudoTimer().new UIThreadCommand() {
                            @Override
                            public void runCommand() {
                                render();
                                try {
                                    upgrades.cashCheck();
                                    shop.cashCheck();
                                } catch (Exception e) {

                                }

                            }
                        }.start(MainActivity.this);

                    }
                }
            }
        }.start();
    }

    public void render() {
        mMap.clear();
        /*CircleOptions circly = new CircleOptions();
        circly.center(control.player.pos);
        circly.radius(control.player.View);
        circly.fillColor(Color.RED);
        mMap.addCircle(circly);*/
        mMap.addCircle(new CircleOptions().center(control.player.pos).radius(100000f));
        mMap.addMarker(new MarkerOptions().position(control.player.pos).title(String.valueOf(control.player.Health) + "/" + String.valueOf(control.player.maxHealth)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(control.player.pos));
        for (int i = 0; i < control.enemies.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(control.enemies.get(i).pos).title(String.valueOf(control.enemies.get(i).Health) + "/" + String.valueOf(control.enemies.get(i).maxHealth)));
        }
        for (int i = 0; i < control.turrets.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(control.turrets.get(i).pos).title(control.turrets.get(i).Name));
        }
    }


}
