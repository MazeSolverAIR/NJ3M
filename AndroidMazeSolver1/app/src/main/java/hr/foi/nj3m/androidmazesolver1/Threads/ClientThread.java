package hr.foi.nj3m.androidmazesolver1.Threads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ClientThread implements Callable {
    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private Handler handler;
    SendReceive sendReceive;
    Context context;

    public ClientThread(BluetoothDevice device, Handler handler, Context context){
        this.context = context;
        this.handler = handler;
        bluetoothDevice = device;
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(mUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SendReceive call() throws Exception {
        bluetoothSocket.connect();
        sendReceive = new SendReceive(bluetoothSocket, handler);
        sendReceive.start();
        return sendReceive;
    }
}
