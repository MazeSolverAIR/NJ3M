package hr.foi.nj3m.virtualwifi;


import android.content.BroadcastReceiver;

import java.util.ArrayList;

import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;

public class VirtualWiFi implements IMessenger, IDevice, IDiscover {

    public String recvdMessage = "";
    public int recvdUdaljenost = 0;
    static VirtualMsgContainer vContainer = null;
    ArrayList<String> virtualDevices;

    private static IRobotConnector instanceOfVirtualWifi;

    public static IRobotConnector getVirtualWifiInstance(){
        return instanceOfVirtualWifi;
    }

    public static IRobotConnector createVirtualWifiInstance(){
        if(instanceOfVirtualWifi == null)
            instanceOfVirtualWifi = new VirtualWiFi(vContainer);
    }

    public VirtualWiFi (VirtualMsgContainer vc)
    {
        vContainer = vc;
    }


    @Override
    public void send(String command) {
        vContainer.setMessage(command);
    }

    @Override
    public void receive() {
        this.recvdMessage = vContainer.getMessage();

    }

    @Override
    public ArrayList<String> getDeviceArray() {
        return virtualDevices;
    }

    @Override
    public void discover(BroadcastReceiver mBroadcastReceiver) {
        virtualDevices = new ArrayList<>();
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
        return false;
    }
}
