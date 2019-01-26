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
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IConnection;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;
import hr.foi.nj3m.interfaces.connections.IWireless;

import static hr.foi.nj3m.wifi.WiFiCommunicator.createWiFiSender;

public class WiFi implements IWireless, IDiscover, IRobotConnector {

    private WifiManager wifiManager;
    private Context context;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiP2pChannel;
    private IntentFilter intentFilter;
    private ArrayList<WifiP2pDevice> wifiP2pDevices;
    private WifiP2pConfig wifiP2pConfig;
    private ArrayList<String> deviceNameArray;
    private static Socket socket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static Handler handler;

    private static IRobotConnector InstanceOfWiFi;

    public static IRobotConnector getWiFiInstance() {
        return InstanceOfWiFi;
    }

    public static IRobotConnector createWiFiInstance(Context context) {
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

        wifiP2pDevices = new ArrayList<>();
        deviceNameArray = new ArrayList<>();
    }

    @Override
    public void addDevices(ArrayList devices) {
        this.wifiP2pDevices = devices;
        refreshNameArray();
    }

    private void refreshNameArray() {
        deviceNameArray.clear();
        for (WifiP2pDevice device : wifiP2pDevices)
            deviceNameArray.add(device.deviceName);
    }

    @Override
    public void clearList() {
        wifiP2pDevices.clear();
    }

    @Override
    public ArrayList<String> getDeviceArray() {
        return deviceNameArray;
    }

    @Override
    public String getDeviceAddress(int position) {
        return wifiP2pDevices.get(position).deviceAddress;
    }

    @Override
    public String getDeviceName(int position) {
        return wifiP2pDevices.get(position).deviceName;
    }

    @Override
    public boolean deviceExists(String deviceName) {
        return false;
    }

    @Override
    public void setIntent(BroadcastReceiver broadcastReceiver) {
        IntentFilter connectedFilter = new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        context.registerReceiver(broadcastReceiver, connectedFilter);
    }

    @Override
    public IMessenger connect(final int position) {
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
    public boolean isEnabled() {
        if (wifiManager.isWifiEnabled())
            return true;
        else
            return false;
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

    @Override
    public void initializeSocket(String address, Handler handler) {
        try {
            socket.connect(new InetSocketAddress(address, 8888), 500);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.handler = handler;
    }

    protected static OutputStream getOutputStream(){
        return outputStream;
    }

    protected static InputStream getInputStream(){
        return inputStream;
    }

    protected static Socket getSocket(){
        return socket;
    }

    protected static Handler getHandler(){
        return handler;
    }
}
