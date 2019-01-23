package hr.foi.nj3m.communications;

// Ovo sučelje bi trebalo implementirati u bluetooth klasu i u WiFi klasu

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

//ovaj modul treba koristiti i wiFi modul i bluetooth modul
public interface IRobotMessenger extends ISocket, IMessenger{
}
