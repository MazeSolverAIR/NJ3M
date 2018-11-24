package com.example.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;

import static android.content.ContentValues.TAG;
import static com.example.bluetooth.BluetoothSender.createBluetoothSender;

public class Bluetooth extends Activity implements IConnections {

    BluetoothAdapter mBluetoothAdapter;

    private static Bluetooth instanceOfBluetooth;

    public static Bluetooth getBluetoothInstance(){
        return instanceOfBluetooth;
    }

    public static Bluetooth createBluetoothInstance(){
        if(instanceOfBluetooth == null)
            instanceOfBluetooth = new Bluetooth();

        return instanceOfBluetooth;
    }

    private Bluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        return createBluetoothSender();
    }

    @Override
    public boolean disconnect() {
        return false;
    }

}
