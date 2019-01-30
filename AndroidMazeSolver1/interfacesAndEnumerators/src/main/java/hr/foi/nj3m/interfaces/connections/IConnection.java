package hr.foi.nj3m.interfaces.connections;

import android.content.BroadcastReceiver;

import hr.foi.nj3m.interfaces.communications.IMessenger;

public interface IConnection {
    void setIntent(BroadcastReceiver broadcastReceiver);
    IMessenger connect(int position);
}
