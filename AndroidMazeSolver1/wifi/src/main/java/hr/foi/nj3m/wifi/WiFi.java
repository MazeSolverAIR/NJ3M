package hr.foi.nj3m.wifi;

import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;

import static hr.foi.nj3m.wifi.WiFiSender.createWiFiSender;

public class WiFi implements IConnections {

    private static WiFi InstanceOfWiFi;

    public static WiFi getWiFiInstance() {
        return InstanceOfWiFi;
    }

    public static WiFi createWiFiInstance() {
        if (InstanceOfWiFi == null)
            InstanceOfWiFi = new WiFi();

        return InstanceOfWiFi;
    }

    private WiFi() {
        //konstruktor
    }


    @Override
    public IRobotMessenger connect() {
        return createWiFiSender();
    }

    @Override
    public boolean disconnect() {
        InstanceOfWiFi = null;
        return true;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

}
