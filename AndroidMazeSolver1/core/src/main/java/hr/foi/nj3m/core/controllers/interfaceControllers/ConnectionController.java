package hr.foi.nj3m.core.controllers.interfaceControllers;

import com.example.bluetooth.Bluetooth;

import hr.foi.nj3m.interfaces.IConnections;

import static hr.foi.nj3m.wifi.WiFi.createWiFiInstance;

public class ConnectionController {

    private static IConnections InstanceOfConnection;

    public static IConnections getInstanceOfConnection()
    {
        return InstanceOfConnection;
    }

    public static IConnections creteInstance(String odabranNacinKomunikacije)
    {
        new ConnectionController(odabranNacinKomunikacije);

        return InstanceOfConnection;
    }

    private ConnectionController(String odabranNacinKomunikacije)
    {
        if(odabranNacinKomunikacije == "bluetooth")
        {
            InstanceOfConnection = new Bluetooth();
        }
        else if(odabranNacinKomunikacije == "wifi")
        {
            InstanceOfConnection = createWiFiInstance();
        }
    }
}
