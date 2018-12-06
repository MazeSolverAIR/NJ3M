package hr.foi.nj3m.core.controllers.interfaceControllers;

import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.example.bluetooth.Bluetooth;

import hr.foi.nj3m.interfaces.IConnections;

import static com.example.bluetooth.Bluetooth.createBluetoothInstance;
import static hr.foi.nj3m.wifi.WiFi.createWiFiInstance;

public class ConnectionController {

    Context context;
    String deviceAddress;

    private static IConnections InstanceOfConnection;

    public static IConnections getInstanceOfConnection()
    {
        return InstanceOfConnection;
    }

    public static IConnections creteInstance(String odabranNacinKomunikacije, Context context, String deviceAddress)
    {
        new ConnectionController(odabranNacinKomunikacije, context, deviceAddress);

        return InstanceOfConnection;
    }

    private ConnectionController(String odabranNacinKomunikacije, Context context, String deviceAddress)
    {
        if(odabranNacinKomunikacije == "bluetooth")
        {
            InstanceOfConnection = createBluetoothInstance(context);
        }
        else if(odabranNacinKomunikacije == "wifi")
        {
            InstanceOfConnection = createWiFiInstance();
        }
    }
}
