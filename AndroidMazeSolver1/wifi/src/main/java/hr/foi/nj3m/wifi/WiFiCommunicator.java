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
    public void initializeSocket(BluetoothSocket socket, Handler handler) {

    }

    @Override
    public void sendCommand(String naredba) {

    }

    @Override
    public void receive() {

    }
}
