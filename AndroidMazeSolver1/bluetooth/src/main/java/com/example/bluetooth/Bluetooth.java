package com.example.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;
import hr.foi.nj3m.interfaces.IWireless;

import static android.content.ContentValues.TAG;
import static com.example.bluetooth.BluetoothCommunicator.createBluetoothSender;

public class Bluetooth extends Activity implements IConnections, IWireless {

    BluetoothAdapter mBluetoothAdapter;
    Context context;
    ArrayList<BluetoothDevice> bluetoothDevices;
    ArrayList<String> deviceNameArray;

    private static Bluetooth instanceOfBluetooth;

    public static Bluetooth getBluetoothInstance(){
        return instanceOfBluetooth;
    }

    public static Bluetooth createBluetoothInstance(Context context){
        if(instanceOfBluetooth == null)
            instanceOfBluetooth = new Bluetooth(context);

        return instanceOfBluetooth;
    }

    private Bluetooth(Context context){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        bluetoothDevices = new ArrayList<>();
        deviceNameArray = new ArrayList<>();
    }

    @Override
    public boolean isAvailable() {
            return false;
    }

    @Override
    public void addDevices(ArrayList devices) {
        this.bluetoothDevices.addAll(devices);
        refreshNameArray();
    }

    @Override
    public void clearList() {
        bluetoothDevices.clear();
    }

    @Override
    public ArrayList<String> getDeviceArray() {
        return deviceNameArray;
    }

    private void refreshNameArray() {
        deviceNameArray.clear();
        for (BluetoothDevice device : bluetoothDevices)
            deviceNameArray.add(device.getName());
    }

    @Override
    public String getDeviceAddress(int position) {
        return bluetoothDevices.get(position).getAddress();
    }

    @Override
    public String getDeviceName(int position) {
        return bluetoothDevices.get(position).getName();
    }

    @Override
    public boolean deviceExists(String deviceName) {
        boolean exists = false;
        for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()){
            if (device.getName().equals(deviceName))
                exists = true;
        }
        return exists;
    }

    @Override
    public IRobotMessenger connect(int position) {
        mBluetoothAdapter.cancelDiscovery();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            bluetoothDevices.get(position).createBond();
        }
        return createBluetoothSender(context);
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public void enableDisable(BroadcastReceiver mBroadcastReceiver) {
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(mBroadcastReceiver, BTIntent);
        }
        /*if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(mBroadcastReceiver, BTIntent);
        }*/
    }

    @Override
    public void discover(BroadcastReceiver mBroadcastReceiver) {
        if(mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
            Toast.makeText(context, "Tražim uređaje...", Toast.LENGTH_LONG).show();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            Toast.makeText(context, "Tražim uređaje...", Toast.LENGTH_LONG).show();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }else{
                Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
            }
        }
    }
}
