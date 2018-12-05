package com.example.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;

import static com.example.bluetooth.BluetoothCommunicator.createBluetoothSender;

public class Bluetooth extends Activity implements IConnections {

    BluetoothAdapter mBluetoothAdapter;
    Context context;
    String deviceAddress;
    BluetoothSocket bluetoothSocket = null;

    private static Bluetooth instanceOfBluetooth;

    public static Bluetooth getBluetoothInstance(){
        return instanceOfBluetooth;
    }

    public static Bluetooth createBluetoothInstance(Context context, String deviceAddress){
        if(instanceOfBluetooth == null)
            instanceOfBluetooth = new Bluetooth(context, deviceAddress);

        return instanceOfBluetooth;
    }

    private Bluetooth(Context context, String deviceAddress){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        this.deviceAddress = deviceAddress;
    }

    @Override
    public boolean isAvailable() {
            return false;
    }

    @Override
    public IRobotMessenger connect(ArrayList devices, int position) {
        mBluetoothAdapter.cancelDiscovery();
        ArrayList<BluetoothDevice> mDevices = devices;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            mDevices.get(position).createBond();
        }
        return createBluetoothSender(context, deviceAddress);
    }

    @Override
    public boolean disconnect() {
        return false;
    }

}
