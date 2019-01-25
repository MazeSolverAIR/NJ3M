package hr.foi.nj3m.core.controllers.interfaceControllers;

import android.content.Context;

import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IConnection;

import static com.example.bluetooth.Bluetooth.createBluetoothInstance;
import static hr.foi.nj3m.wifi.WiFi.createWiFiInstance;

public class ConnectionController {

    Context context;
    String deviceAddress;

    private static IRobotConnector InstanceOfConnection;
    public static IRobotConnector getInstanceOfConnection()
    {
        return InstanceOfConnection;
    }

    private static IMessenger iMessenger;
    public static void setIMessenger(IMessenger iMsngr){
        iMessenger = iMsngr;
    }

    public static IMessenger getiMessenger(){
        return iMessenger;
    }

    public static IRobotConnector creteInstance(String odabranNacinKomunikacije, Context context)
    {
        new ConnectionController(odabranNacinKomunikacije, context);

        return InstanceOfConnection;
    }

    private ConnectionController(String odabranNacinKomunikacije, Context context)
    {
        if(odabranNacinKomunikacije.equals("bluetooth"))
        {
            InstanceOfConnection = createBluetoothInstance(context);
            //InstanceOfIRobot = createBluetoothSender(context);
        }
        else if(odabranNacinKomunikacije.equals("wifi"))
        {
            InstanceOfConnection = createWiFiInstance(context);
            //InstanceOfIRobot = createWiFiSender();
        }
        else if(odabranNacinKomunikacije.equals("virtualWifi"))
        {

            //InstanceOfIRobot = creteVirtualWifiSender();
        }
    }
}
