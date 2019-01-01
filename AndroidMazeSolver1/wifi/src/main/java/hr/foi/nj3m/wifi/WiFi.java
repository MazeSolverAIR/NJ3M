package hr.foi.nj3m.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
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
    public IRobotMessenger connect(ArrayList mDevices, int position) {
        return createWiFiSender();
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
    public void enableDisable(BroadcastReceiver mBroadcastReceiver) {
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
            IntentFilter WiFiIntent = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
            context.registerReceiver(mBroadcastReceiver, WiFiIntent);
        }
    }

    @Override
    public void discover(final BroadcastReceiver mBroadcastReceiver) {
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
}
