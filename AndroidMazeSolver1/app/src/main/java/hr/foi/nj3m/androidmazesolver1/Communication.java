package hr.foi.nj3m.androidmazesolver1;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.UUID;

public class Communication {
    Context context;
    String deviceAddress;
    BluetoothSocket bluetoothSocket = null;

    public Communication(Context context, String deviceAddress) {
        this.context = context;
        this.deviceAddress = deviceAddress;
    }

<<<<<<< HEAD
    public void SendData(BluetoothSocket bluetoothSocket){
        byte[] message = "LLijevo".getBytes();
        try{
            bluetoothSocket.getOutputStream().write(message);
            Log.d("TAG", "poslana poruka" + message.toString());
=======
    public void SendData(BluetoothSocket bluetoothSocket, String message){
        byte[] messageToSend = message.getBytes();
        try{
            bluetoothSocket.getOutputStream().write(messageToSend);
            Log.d("Message:", messageToSend.length+"");
>>>>>>> MatijaK
        }catch (IOException e){

        }

    }

    public String GetAddress(){
        return deviceAddress;
    }
}
