package hr.foi.nj3m.core.controllers.interfaceControllers;

import android.content.Context;

import hr.foi.nj3m.interfaces.IWireless;

import static com.example.bluetooth.Bluetooth.createBluetoothInstance;

public class WirelessController {
    private static IWireless InstanceOfIWireless;

    public static IWireless getInstanceOfIWireless(){
        return InstanceOfIWireless;
    }

    public static IWireless createInstance(Context context){
        new WirelessController(context);
        return InstanceOfIWireless;
    }

    private WirelessController(Context context){
        InstanceOfIWireless = createBluetoothInstance(context);
    }
}
