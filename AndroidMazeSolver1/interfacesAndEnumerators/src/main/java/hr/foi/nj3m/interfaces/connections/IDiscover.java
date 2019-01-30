package hr.foi.nj3m.interfaces.connections;

import android.content.BroadcastReceiver;

import java.util.ArrayList;

public interface IDiscover {
    void discover(BroadcastReceiver mBroadcastReceiver);
    void addDevices(ArrayList devices);
    void clearList();
    String getDeviceName(int position);
    boolean deviceExists(String deviceName);
}
