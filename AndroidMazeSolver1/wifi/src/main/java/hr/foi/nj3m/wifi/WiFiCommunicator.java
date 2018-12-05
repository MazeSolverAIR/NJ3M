package hr.foi.nj3m.wifi;

import android.bluetooth.BluetoothSocket;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class WiFiCommunicator implements IRobotMessenger {


    private static WiFiCommunicator InstanceOfSender;

    protected static WiFiCommunicator createWiFiSender()
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
    public byte[] receive() {
        return new byte[0];
    }
}
