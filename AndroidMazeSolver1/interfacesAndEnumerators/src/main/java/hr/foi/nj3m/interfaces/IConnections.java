package hr.foi.nj3m.interfaces;

import android.content.BroadcastReceiver;

import java.util.ArrayList;

public interface IConnections {

    boolean isAvailable();
    IRobotMessenger connect(ArrayList mDevices, int position);
    boolean disconnect();
}
