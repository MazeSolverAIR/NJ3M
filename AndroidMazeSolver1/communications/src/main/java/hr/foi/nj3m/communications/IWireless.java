package hr.foi.nj3m.communications;

//Sučelje za rad s Bluetoothom i WiFi konekcijama

import android.content.BroadcastReceiver;

public interface IWireless {
    void enableDisable(BroadcastReceiver mBroadcastReceiver);
    void discover(BroadcastReceiver mBroadcastReceiver);
}
