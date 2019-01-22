package hr.foi.nj3m.core.controllers.interfaceControllers;

import android.content.Context;

import hr.foi.nj3m.communications.IWireless;

import static com.example.bluetooth.Bluetooth.createBluetoothInstance;
import static hr.foi.nj3m.wifi.WiFi.createWiFiInstance;

public class WirelessController {
    private static IWireless InstanceOfIWireless;

    public static IWireless getInstanceOfIWireless(){
        return InstanceOfIWireless;
    }

    public static IWireless createInstance(Context context, String typeOfConnection){
        new WirelessController(context, typeOfConnection);
        return InstanceOfIWireless;
    }

    private WirelessController(Context context, String typeOfConnection){
        if(typeOfConnection.equals("bluetooth"))
            InstanceOfIWireless = createBluetoothInstance(context);
        else if (typeOfConnection.equals("wifi"))
            InstanceOfIWireless = createWiFiInstance(context);
    }
}
