package com.example.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class BluetoothCommunicator implements IRobotMessenger {

    Context context;
    BluetoothSocket bluetoothSocket = null;

    private static BluetoothCommunicator InstanceOfSender;

    public static BluetoothCommunicator createBluetoothSender(Context context)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothCommunicator(context);

        return InstanceOfSender;
    }

    private BluetoothCommunicator(Context context)
    {
        this.context = context;
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

    // TODO: 12/6/2018 POTREBNO TESTIRATI!!! 
    @Override
    public byte[] receive(final Handler handler, final BluetoothSocket bluetoothSocket) {
        return null;
    }
}
