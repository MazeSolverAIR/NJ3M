package com.example.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class BluetoothCommunicator implements IRobotMessenger {

    Context context;
    String deviceAddress;
    BluetoothSocket bluetoothSocket = null;

    private static BluetoothCommunicator InstanceOfSender;

    protected static BluetoothCommunicator createBluetoothSender(Context context, String deviceAddress)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothCommunicator(context, deviceAddress);

        return InstanceOfSender;
    }

    private BluetoothCommunicator(Context context, String deviceAddress)
    {
        this.context = context;
        this.deviceAddress = deviceAddress;
    }

    @Override
    public boolean sendCommand(String command, BluetoothSocket bluetoothSocket) {
        byte[] message = command.getBytes();
        try{
            bluetoothSocket.getOutputStream().write(message);
            Log.d("TAG", "poslana poruka" + message.toString());
        }catch (IOException e){

        }
        return false;
    }

    @Override
    public String getAddress() {
        return deviceAddress;
    }

    @Override
    public byte[] receive() {
        return new byte[0];
    }
}
