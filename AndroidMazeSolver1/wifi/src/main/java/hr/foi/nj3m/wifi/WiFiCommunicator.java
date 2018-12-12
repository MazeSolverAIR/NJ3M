package hr.foi.nj3m.wifi;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class WiFiCommunicator implements IRobotMessenger {


    private static WiFiCommunicator InstanceOfSender;

    public static WiFiCommunicator createWiFiSender()
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new WiFiCommunicator();

        return InstanceOfSender;
    }

    private WiFiCommunicator()
    {

    }

    @Override
    public boolean sendCommand(String naredba, BluetoothSocket bluetoothSocket) {
        return false;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public byte[] receive(final Handler handler, BluetoothSocket bluetoothSocket) {
        return new byte[0];
    }
}
