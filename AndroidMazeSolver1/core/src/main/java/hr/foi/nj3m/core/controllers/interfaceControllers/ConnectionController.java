package hr.foi.nj3m.core.controllers.interfaceControllers;

import android.content.Context;

import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IConnection;

import static com.example.bluetooth.Bluetooth.createBluetoothInstance;
import static hr.foi.nj3m.virtualwifi.VirtualWiFi.createVirtualWifiInstance;
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

    /**
     * Sprema objekt tipa IMessenger
     * @param iMsngr objekt IMessenger
     */
    public static void setIMessenger(IMessenger iMsngr){
        iMessenger = iMsngr;
    }

    /**
     * @return instanca objekta IMessenger
     */
    public static IMessenger getIMessenger(){
        return iMessenger;
    }

    /**
     * Kreira instancu IRobotConnect na temelju ulaznog stringa
     * @param odabranNacinKomunikacije nacin komunikacije - bluetooth, wifi, virtualWifi
     * @param context context
     */
    public static IRobotConnector creteInstance(String odabranNacinKomunikacije, Context context)
    {
        new ConnectionController(odabranNacinKomunikacije, context);

        return InstanceOfConnection;
    }

    /**
     * Kreira instancu IRobotConnect na temelju ulaznog stringa
     * @param odabranNacinKomunikacije nacin komunikacije - bluetooth, wifi, virtualWifi
     * @param context context
     */
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
            InstanceOfConnection = createVirtualWifiInstance(context);
            //InstanceOfIRobot = creteVirtualWifiSender();
        }
    }
}
