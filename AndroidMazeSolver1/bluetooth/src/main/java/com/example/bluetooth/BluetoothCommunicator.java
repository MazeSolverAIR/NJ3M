package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import hr.foi.nj3m.interfaces.communications.IMessenger;

public class BluetoothCommunicator implements IMessenger {

    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Context context;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothSocket bluetoothSocket;
    private Handler handler;
    private String deviceAddress;
    private BluetoothAdapter bluetoothAdapter;
    private String obtainedMsg;

    private static IMessenger InstanceOfSender;

    public static IMessenger createBluetoothSender(Context context, String deviceAddress)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothCommunicator(context, deviceAddress);

        return InstanceOfSender;
    }

    public static IMessenger getBluetoothSender(){
        return InstanceOfSender;
    }

    private BluetoothCommunicator(Context context, String deviceAddress)
    {
        this.context = context;
        this.deviceAddress = deviceAddress;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initializeSocket();
    }

    @Override
    public void send(String command) {
        byte[] message = command.getBytes();
        try {
            outputStream.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: 12/6/2018 POTREBNO TESTIRATI!!!
    /*TODO: Potrebno je osmisliti spremanje buffera koji je != null ili != "" u polje byte(ili string).
      TODO:  Metoda receive može vraćati to polje ako je zadnja dobivena informacija == "Over"  - ili možda osmisliti neko drugo rješenje. Razmisliti*/
    //TODO: Postaviti neki delay od npr 2ms
    /*Znaci handler bude vracal polje stringova ili listu, svejedno samo ako je primil poruku Over, inace bude vracal null.*/
    @Override
    public void receive(Object channel) {
        handler = (Handler) channel;
        byte[] buffer = new byte[1024];
        int bytes;
        while (bluetoothSocket != null){
            try {
                bytes = inputStream.read(buffer);
                Message msg = handler.obtainMessage(1, bytes, -1, buffer);
                msg.sendToTarget();
                obtainedMsg = msg.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getRcvdMsg() {
        return obtainedMsg;
    }

    private void initializeSocket(){
        try {
            bluetoothSocket = bluetoothAdapter.getRemoteDevice(deviceAddress).createRfcommSocketToServiceRecord(mUUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
