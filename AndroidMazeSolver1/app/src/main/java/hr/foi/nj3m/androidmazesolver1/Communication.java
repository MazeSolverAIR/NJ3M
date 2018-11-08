package hr.foi.nj3m.androidmazesolver1;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.UUID;

public class Communication {
    //Insecure UUID
    private static final UUID mUUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    Context context;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;
    private ConnectedThread mConnectedThread;

    public Communication(Context context) {
        this.context = context;
    }

    private class ConnectThread extends Thread{
        private BluetoothSocket mSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmpSocket = null;

            try {
                Toast.makeText(context, "Trying to connect...", Toast.LENGTH_LONG).show();
                tmpSocket = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Toast.makeText(context, "Connection failed " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            mSocket = tmpSocket;
            MainActivity.mBluetoothAdapter.cancelDiscovery();

            try {
                mSocket.connect();
                Toast.makeText(context, "Connection successful", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                try {
                    mSocket.close();
                    Toast.makeText(context, "Connection closed", Toast.LENGTH_LONG).show();
                } catch (IOException e1) {
                    Toast.makeText(context, "Unable to close connection " + e1.getMessage(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(context, "Could not connect to UUID: " /*+ MY_UUID_INSECURE*/, Toast.LENGTH_LONG).show();
            }

            //connected(mSocket, mmDevice);
        }

        public void cancel(){
            try {
                mSocket.close();
                Toast.makeText(context, "Socket closed", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context, "Unable to close socket", Toast.LENGTH_LONG).show();
            }
        }

        public void start(BluetoothDevice device, UUID uuid){
            mProgressDialog = ProgressDialog.show(context, "Establishing Bluetooth connection", "Please wait...", true);
            mConnectThread = new ConnectThread(device, uuid);
            mConnectThread.start();
        }
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mSocket;
        private final InputStream mInputStream;
        private final OutputStream mOutputStream;

        public ConnectedThread(BluetoothSocket socket) {
            mSocket = socket;
            InputStream tmpInputStream = null;
            OutputStream tmpOutputStream = null;

            //dismiss progress dialog when connection is established
            mProgressDialog.dismiss();

            try {
                tmpInputStream = mSocket.getInputStream();
                tmpOutputStream = mSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mInputStream = tmpInputStream;
            mOutputStream = tmpOutputStream;

        }

        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            try {
                mOutputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel(){
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] out){
        ConnectedThread r;
        mConnectedThread.write(out);
    }
}
