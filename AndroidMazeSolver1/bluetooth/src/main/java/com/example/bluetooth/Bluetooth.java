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

    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public IRobotMessenger connect() {
        return createBluetoothSender();
    }

    @Override
    public boolean disconnect() {
        return false;
    }

}
