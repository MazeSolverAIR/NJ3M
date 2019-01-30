package hr.foi.nj3m.interfaces.connections;

//Sučelje za rad s Bluetoothom i WiFi konekcijama

import android.content.BroadcastReceiver;

public interface IWireless {
    boolean isEnabled();
    void enableDisable(BroadcastReceiver mBroadcastReceiver);
}
