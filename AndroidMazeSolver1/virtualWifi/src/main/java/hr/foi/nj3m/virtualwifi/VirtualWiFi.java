package hr.foi.nj3m.virtualwifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;

public class VirtualWiFi implements IMessenger, IDevice, IDiscover, IRobotConnector {

    public String recvdMessage = "";
    static VirtualMsgContainer vContainer = null;
    ArrayList<String> virtualDevices;
    private WifiManager wifiManager;
    private Context context;

    private static IRobotConnector instanceOfVirtualWifi;

    public static IRobotConnector getVirtualWifiInstance(){
        return instanceOfVirtualWifi;
    }

    public static IRobotConnector createVirtualWifiInstance(Context context){
        if(instanceOfVirtualWifi == null)
            instanceOfVirtualWifi = new VirtualWiFi(context);

        return instanceOfVirtualWifi;
    }

    public VirtualWiFi (Context context)
    {
        //vContainer = vc;
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        virtualDevices = new ArrayList<>();
    }


    @Override
    public void send(String command) {
        vContainer.setMessage(command);
    }

    @Override
    public void receive(Object channel) {
        vContainer = (VirtualMsgContainer) channel;
        this.recvdMessage = vContainer.getMessage();
    }

    @Override
    public String getRcvdMsg() {
        return this.recvdMessage;
    }


    @Override
    public ArrayList<String> getDeviceArray() {
        virtualDevices.add("Virtual mBot");
        return virtualDevices;
    }

    @Override
    public void discover(BroadcastReceiver mBroadcastReceiver) {

    }

    @Override
    public void addDevices(ArrayList devices) {
        virtualDevices.add("Virtual mBot");
    }

    @Override
    public void clearList() {
        virtualDevices.clear();
    }

    @Override
    public String getDeviceAddress(int position) {
        return null;
    }

    @Override
    public String getDeviceName(int position) {
        return null;
    }

    @Override
    public boolean deviceExists(String deviceName) {
        return true;
    }

    @Override
    public void setIntent(BroadcastReceiver broadcastReceiver) {

    }

    @Override
    public IMessenger connect(int position) {
        return (IMessenger) instanceOfVirtualWifi;
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
}
