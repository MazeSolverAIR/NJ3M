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
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IConnection;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;
import hr.foi.nj3m.interfaces.connections.ISocket;
import hr.foi.nj3m.interfaces.connections.IWireless;

import static android.content.ContentValues.TAG;

public class Bluetooth extends Activity implements IWireless, IDiscover, IRobotConnector, ISocket {

    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter;
    Context context;
    ArrayList<BluetoothDevice> bluetoothDevices;
    ArrayList<String> deviceNameArray;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static BluetoothSocket bluetoothSocket;
    private static Handler handler;

    private static IRobotConnector instanceOfBluetooth;

    public static IRobotConnector getBluetoothInstance(){
        return instanceOfBluetooth;
    }

    public static IRobotConnector createBluetoothInstance(Context context){
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
            if (device.getName().equals(deviceName)) {
                exists = true;
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        return exists;
    }

    @Override
    public void setIntent(BroadcastReceiver broadcastReceiver) {
        IntentFilter bondedFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        context.registerReceiver(broadcastReceiver, bondedFilter);
    }

    @Override
    public IMessenger connect(int position) {
        mBluetoothAdapter.cancelDiscovery();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            bluetoothDevices.get(position).createBond();
        }
        return BluetoothCommunicator.createBluetoothSender(context);
    }

    @Override
    public boolean isEnabled() {
        if (mBluetoothAdapter.isEnabled())
            return true;
        else
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

    @Override
    public void initializeSocket(String address, Handler handler) {
        try {
            bluetoothSocket = mBluetoothAdapter.getRemoteDevice(address).createRfcommSocketToServiceRecord(mUUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.handler = handler;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = bluetoothSocket.getInputStream();
            tmpOut = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    protected static InputStream getInputStream() {
        return inputStream;
    }

    protected static OutputStream getOutputStream() {
        return outputStream;
    }

    protected static Handler getHandler() {
        return handler;
    }

    protected static BluetoothSocket getBluetoothSocket(){
        return bluetoothSocket;
    }
}
