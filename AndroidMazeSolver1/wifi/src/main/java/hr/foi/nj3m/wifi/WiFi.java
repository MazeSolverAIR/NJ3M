package hr.foi.nj3m.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import java.util.ArrayList;

import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;
import hr.foi.nj3m.interfaces.IWireless;

import static hr.foi.nj3m.wifi.WiFiCommunicator.createWiFiSender;

public class WiFi implements IConnections, IWireless {

    private WifiManager wifiManager;
    private Context context;

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
    public void discover(BroadcastReceiver mBroadcastReceiver) {

    }
}
