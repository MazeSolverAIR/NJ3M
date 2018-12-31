package hr.foi.nj3m.androidmazesolver1;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
<<<<<<< HEAD

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.net.wifi.WifiManager;
=======
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
>>>>>>> c94d1164cad4be39c0faa8689db7c5ca4095916f
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import hr.foi.nj3m.interfaces.IWireless;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


<<<<<<< HEAD
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        final ImageButton tipkaBluetooth = (ImageButton) findViewById(R.id.btnBluetooth);
        final ImageButton tipkaWifi = (ImageButton) findViewById(R.id.btnWifi);

        tipkaBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iWireless = WirelessController.createInstance(getApplicationContext(), "bluetooth");
                //bluetooth.enableDisableBluetooth();
                if(mBluetoothAdapter.isEnabled()){
                    openListOfDevices();
                }
                else
                    iWireless.enableDisable(mBroadcastReceiver);
            }
        });

        tipkaWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iWireless = WirelessController.createInstance(getApplicationContext(), "wifi");
                if(wifiManager.isWifiEnabled())
                    openListOfDevices();
                else
                    iWireless.enableDisable(mBroadcastReceiver);
            }
        });

        File mFileTemp = new File(getExternalFilesDir(DIRECTORY_DCIM)+File.separator+"MazeSolverPictures","Maze.png");
        mFileTemp.getParentFile().mkdirs();
        galleryAddPic();
=======
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
>>>>>>> c94d1164cad4be39c0faa8689db7c5ca4095916f

        if (savedInstanceState == null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GalleryFragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_gallery);
                }
            });
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = new ConnectionTypeSelectionFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commitAllowingStateLoss();
            }
        });

        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED

                ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {permission},123);
        }


    }

<<<<<<< HEAD
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "Bluetooth isključen", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context, "Bluetooth uključen", Toast.LENGTH_LONG).show();
                        openListOfDevices();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
            if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                final int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch (state){
                    case WifiManager.WIFI_STATE_DISABLED:
                        Toast.makeText(getApplicationContext(), "WiFi isključen", Toast.LENGTH_LONG).show();
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Toast.makeText(getApplicationContext(), "WiFi uključen", Toast.LENGTH_LONG).show();
                        openListOfDevices();
                        break;
                }
            }
        }
    };
=======
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_gallery:
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_left_to_right,R.anim.enter_right_to_left,R.anim.exit_right_to_left)
                        .replace(R.id.fragment_container,new GalleryFragment())
                        .commit();
                break;
            case R.id.nav_add_photo:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_left_to_right,R.anim.enter_right_to_left,R.anim.exit_right_to_left).replace(R.id.fragment_container,new AddFragment()).commit();
                break;
            case R.id.nav_info:
                Toast.makeText(this,"Info",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sensor_selection:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_left_to_right,R.anim.enter_right_to_left,R.anim.exit_right_to_left).replace(R.id.fragment_container,new SensorSelectionFragment()).commitAllowingStateLoss();
                break;
>>>>>>> c94d1164cad4be39c0faa8689db7c5ca4095916f

        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();}
    }
}
