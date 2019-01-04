package hr.foi.nj3m.interfaces;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pDevice;

import java.util.ArrayList;

public interface IConnections {

    boolean isAvailable();
    void addDevices(ArrayList devices);
    void clearList();
    ArrayList<String> getDeviceArray();
    String getDeviceAddress(int position);
    String getDeviceName(int position);
    boolean deviceExists(String deviceName);
    IRobotMessenger connect(int position);
    boolean disconnect();
}
