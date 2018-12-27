package hr.foi.nj3m.core.controllers.interfaceControllers;

import android.content.Context;

import com.example.bluetooth.BluetoothCommunicator;

import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;
import hr.foi.nj3m.wifi.WiFiCommunicator;

import static com.example.bluetooth.Bluetooth.createBluetoothInstance;
import static com.example.bluetooth.BluetoothCommunicator.createBluetoothSender;
import static hr.foi.nj3m.wifi.WiFi.createWiFiInstance;
import static hr.foi.nj3m.wifi.WiFiCommunicator.createWiFiSender;

public class ConnectionController {

    Context context;
    String deviceAddress;

    private static IConnections InstanceOfConnection;
    private static IRobotMessenger InstanceOfIRobot;

    public static IRobotMessenger getInstanceOfIRobot() {return InstanceOfIRobot;}
    public static IConnections getInstanceOfConnection()
    {
        return InstanceOfConnection;
    }

    public static IConnections creteInstance(String odabranNacinKomunikacije, Context context)
    {
        new ConnectionController(odabranNacinKomunikacije, context);

        return InstanceOfConnection;
    }

    private ConnectionController(String odabranNacinKomunikacije, Context context)
    {
        if(odabranNacinKomunikacije.equals("bluetooth"))
        {
            InstanceOfConnection = createBluetoothInstance(context);
            InstanceOfIRobot = createBluetoothSender(context);
        }
        else if(odabranNacinKomunikacije.equals("wifi"))
        {
            InstanceOfConnection = createWiFiInstance();
            InstanceOfIRobot = createWiFiSender();
        }
    }
}
