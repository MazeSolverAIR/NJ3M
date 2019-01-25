package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import hr.foi.nj3m.interfaces.communications.IMessenger;

public class BluetoothCommunicator implements IMessenger {

    Context context;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothSocket bluetoothSocket;
    private Handler handler;
    private BluetoothAdapter mBluetoothAdapter;

    private static IMessenger InstanceOfSender;

    public static IMessenger createBluetoothSender(Context context)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothCommunicator(context);

        return InstanceOfSender;
    }

    public static IMessenger getBluetoothSender(){
        return InstanceOfSender;
    }

    private BluetoothCommunicator(Context context)
    {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.inputStream = Bluetooth.getInputStream();
        this.outputStream = Bluetooth.getOutputStream();
        this.handler = Bluetooth.getHandler();
        this.bluetoothSocket = Bluetooth.getBluetoothSocket();
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
    public void receive() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (bluetoothSocket != null){
            try {
                bytes = inputStream.read(buffer);
                handler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
