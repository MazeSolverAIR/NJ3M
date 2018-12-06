package hr.foi.nj3m.interfaces;

// Ovo sučelje bi trebalo implementirati u bluetooth klasu i u WiFi klasu

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

//ovaj modul treba koristiti i wiFi modul i bluetooth modul
public interface IRobotMessenger {

    boolean sendCommand(String naredba, BluetoothSocket bluetoothSocket);

    String getAddress();

    byte[] receive(Handler handler);
}
