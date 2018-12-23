package hr.foi.nj3m.androidmazesolver1.Threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ServerThread implements Callable {
    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothServerSocket bluetoothServerSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler handler;
    SendReceive sendReceive;
    Context context;

    public ServerThread(Handler handler, Context context){
        this.context = context;
        this.handler = handler;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            bluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MazeSolver1", mUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SendReceive call() throws Exception {
        BluetoothSocket socket = null;
        while (socket == null){
            socket = bluetoothServerSocket.accept();
            if (socket != null){
                sendReceive = new SendReceive(socket, handler);
                sendReceive.start();
            }
        }
        return sendReceive;
    }
}
