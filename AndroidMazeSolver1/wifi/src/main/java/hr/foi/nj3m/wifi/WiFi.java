package hr.foi.nj3m.wifi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;
import hr.foi.nj3m.interfaces.IWireless;

import static hr.foi.nj3m.wifi.WiFiCommunicator.createWiFiSender;

public class WiFi implements IConnections, IWireless {

    private WifiManager wifiManager;
    private Context context;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiP2pChannel;
    private IntentFilter intentFilter;
    private ArrayList<WifiP2pDevice> peers;
    private WifiP2pConfig wifiP2pConfig;

    private static WiFi InstanceOfWiFi;

    public static WiFi getWiFiInstance() {
        return InstanceOfWiFi;
    }

    public static WiFi createWiFiInstance(Context context) {
        if (InstanceOfWiFi == null)
            InstanceOfWiFi = new WiFi(context);

        return InstanceOfWiFi;
    }

    private WiFi(Context context) {
        //konstruktor
        this.context = context;

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        wifiP2pChannel = wifiP2pManager.initialize(context, context.getMainLooper(), null);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    public boolean disconnect() {
        InstanceOfWiFi = null;
        return true;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void addDevices(ArrayList devices) {
        this.peers = devices;
    }

    @Override
    public String getDeviceAddress(int position) {
        return peers.get(position).deviceAddress;
    }

    @Override
    public String getDeviceName(int position) {
        return peers.get(position).deviceName;
    }

    @Override
    public boolean deviceExists(String deviceName) {
        return false;
    }

    @Override
    public IRobotMessenger connect(final int position) {
        wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = getDeviceAddress(position);
        wifiP2pManager.connect(wifiP2pChannel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Spojen sa " + getDeviceName(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Greška prilikom spajanja", Toast.LENGTH_LONG).show();
            }
        });
        return createWiFiSender();
    }

    @Override
    public void enableDisable(BroadcastReceiver mBroadcastReceiver) {
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
            IntentFilter WiFiIntent = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
            context.registerReceiver(mBroadcastReceiver, WiFiIntent);
        }
    }

    @Override
    public void discover(final BroadcastReceiver mBroadcastReceiver) {
        checkWifiPermissions();
        wifiP2pManager.discoverPeers(wifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Tražim uređaje...", Toast.LENGTH_LONG).show();
                context.registerReceiver(mBroadcastReceiver, intentFilter);
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Greška prilikom traženja uređaja", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkWifiPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = ContextCompat.checkSelfPermission(context, "Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0){
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        }
    }
}
