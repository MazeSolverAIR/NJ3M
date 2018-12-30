package com.example.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class BluetoothCommunicator implements IRobotMessenger {

    Context context;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothSocket bluetoothSocket;
    private Handler handler;

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
    public void initializeSocket(BluetoothSocket socket, Handler handler) {
        bluetoothSocket = socket;
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

    @Override
    public void sendCommand(String command) {
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

        while (true){
            try {
                bytes = inputStream.read(buffer);
                handler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
