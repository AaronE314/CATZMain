package menu.catz.aaron.catzmain;

import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import menu.catz.aaron.fragments.*;
import menu.catz.aaron.controller.*;

//has to implement NavigationView.OnNavigationItemSelectedListener for the navigation drawer
//and have to implement OnMapReadyCallback for map
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, SurfaceHolder.Callback {

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
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private android.support.v4.app.FragmentManager sFm;
    private Boolean newGame = null;
    private ZAPIClient zClient;
    private DriveZAPI zDrive;
    private String data;
    private int sSelectedMap = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zClient = new ZAPIClient();
        zDrive = new DriveZAPI();
        zClient.AddAPI(zDrive);
        zClient.registerClient(this, this);
        /*if (new FileHandler(getApplicationContext(), "data.txt").hasText()) {
            zClient.addCallback(zClient.new onCreateCallback(){
                @Override
                public void onCreate() {
                    DriveZAPI.GoogleDocument settingsDoc = zDrive.new GoogleDocument(zClient){
                        @Override
                        public void LoadFromId(String id) {
                            //Boolean + Lat + Long + Map Id
                            data = id;
                            editdata();
                        }
                    };

                    settingsDoc.LoadFromId(new FileHandler(getApplicationContext(), "data.txt").readFromFile());
                }
            });
        } else {*/
            zClient.addCallback(zClient.new onCreateCallback() {
                @Override
                public void onCreate() {
                    //Create a new GoogleDocument
                    DriveZAPI.GoogleDocument myDoc = zDrive.new GoogleDocument(zClient) {
                        //Sets up code to be run when the document is successfully generated
                        @Override
                        public void onGenerateSuccess(String id) {
                            content = "";
                            this.Write();
                        }

                        //Sets up code to be run when the document is successfully written to
                        @Override
                        public void onWriteSuccess() {
                            System.out.println("Made a file with id: " + getID());
                            new FileHandler(getApplicationContext(), "data.txt").writeToFile(getID());
                        }
                    };

                    //Generates document
                    myDoc.generateDocument("Wafflepocalypse Settings", true);
                }
            });
        //}
        //Creating a new instance of the supportMapFragment
        supportMapFragment = SupportMapFragment.newInstance();

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        //Grab new Game from Starting Activity
        Bundle getBasket = getIntent().getExtras();
        newGame = getBasket.getBoolean("NewGame");
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
        control = new Controller(this, zClient, zDrive);
        shop = new ShopFragment();
        upgrades = new UpgradesFragment();
        option = new OptionsFragment();

        //setting up Surface View
        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        assert surfaceView != null;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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
                surfaceView.setVisibility(View.VISIBLE);

                break;
            //switching fragments
            case R.id.nav_store:
                fm.beginTransaction().replace(R.id.content_frame, shop).commit();
                shop.setInfo(control, this.getBaseContext());
                surfaceView.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_upgrades:
                fm.beginTransaction().replace(R.id.content_frame, upgrades).commit();
                surfaceView.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_options:
                fm.beginTransaction().replace(R.id.content_frame, option).commit();
                surfaceView.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    //Called when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (sSelectedMap == -1) {
            sSelectedMap = GoogleMap.MAP_TYPE_HYBRID;
        }
        mMap = googleMap;
        mMap.setMapType(sSelectedMap);
        control.mapLoaded(this, newGame);
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
        mMap.addCircle(new CircleOptions().center(control.player.pos).radius(100000f));
        mMap.addMarker(new MarkerOptions().position(control.player.pos).title(String.valueOf(control.player.Health) + "/" + String.valueOf(control.player.maxHealth)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(control.player.pos));
        for (int i = 0; i < control.enemies.size(); i++) {
            try {
                mMap.addGroundOverlay(control.enemies.get(i).ground);
            } catch (NullPointerException e) {
            }
        }
        for (int i = 0; i < control.turrets.size(); i++) {
            try {
                mMap.addGroundOverlay(control.turrets.get(i).ground);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = null;
        try {

            canvas = surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                canvas.drawColor(Color.LTGRAY);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                canvas.drawText("Health: " + control.player.Health, 600, 100, paint);
                canvas.drawText("Cash: " + control.player.cash, 50, 100, paint);
            }
        } catch (Exception e) {
            //Log.e(TAG, "run() lockCanvas()", e);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    public void editdata() {
        String datas[] = data.split(" ");
        if (datas[0].equals("true")) {
            try {
                control.player.gps.stopUsingGPS();
            } catch (Exception e) {
            }
            control.player.pos = new LatLng(Double.valueOf(datas[1]), Double.valueOf(datas[2]));
        }
        sSelectedMap = Integer.valueOf(datas[3]);
    }
}

