package hr.foi.nj3m.androidmazesolver1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.enumeratorControllers.CommandsToMBotController;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

import static java.lang.Thread.sleep;

public class ConnectedDialog extends AppCompatActivity {
    //Communication communication;
    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    private boolean isBluetoothConnected = false;

    Button btnSendControl;
    Button btnConnect;
    Button btnListen;

    SendReceive sendReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_dialog);

        final String deviceAddress = getIntent().getStringExtra(ListOfDevices.EXTRA_ADDRESS);

        btnSendControl = (Button) findViewById(R.id.btnSendControl);
        btnConnect = (Button) findViewById(R.id.btnStartConnection);
        btnListen = (Button) findViewById(R.id.btnListen);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //new ConnectBT().execute();

        //communication = new Communication(this, deviceAddress);

        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerClass serverClass = new ServerClass();
                serverClass.start();
            }
        });

        btnSendControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                /*MBotPathFinder finder = MBotPathFinder.createInstance();

                List<CommandsToMBot> listaNaredbi = finder.TestMethod();
                for (CommandsToMBot naredba:listaNaredbi)
                {
                    String stringNaredba = CommandsToMBotController.getStringFromComandEnum(naredba);
                    ListOfDevices.iRobotMessenger.sendCommand(stringNaredba, bluetoothSocket);
                    try {
                        sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

                String string = "RotateLeft";
                sendReceive.write(string.getBytes());
                Log.d("Poslana poruka: ", string);
                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientClass clientClass = new ClientClass(mBluetoothAdapter.getRemoteDevice(deviceAddress));
                clientClass.start();
                //ListOfDevices.iRobotMessenger.receive(handler, bluetoothSocket);
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                byte[] readBuffer = (byte[]) msg.obj;
                String message = new String(readBuffer, 0, msg.arg1);
                String workingMessage = "";
                try {
                    workingMessage = message.substring(0, message.lastIndexOf(';'));
                }catch (Exception e){

                }
                Log.d("Primio sam", message);
                if(workingMessage.contains("KreceWrite")) {
                    Log.d("Tocna poruka", workingMessage);
                    try {

                        sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendReceive.write("RotateLeft".getBytes());
                }
                /*Log.d("Primljena poruka: ", message);
                //if(message == "Krece")
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();*/
            }
            return true;
        }
    });

    private class ServerClass extends Thread{
        private BluetoothServerSocket bluetoothServerSocket;

        public ServerClass(){
            try {
                bluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MazeSolver1", mUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            BluetoothSocket socket = null;

            while (socket == null){
                try {
                    socket = bluetoothServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(socket != null){
                    sendReceive = new SendReceive(socket);
                    sendReceive.start();
                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread{
        private BluetoothDevice bluetoothDevice;
        private BluetoothSocket bluetoothSocket;

        public ClientClass(BluetoothDevice device){
            bluetoothDevice = device;
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(mUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                bluetoothSocket.connect();
                sendReceive = new SendReceive(bluetoothSocket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket){
            bluetoothSocket = socket;
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

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (true){
                try {
                    bytes = inputStream.read(buffer);
                    String string = new String(buffer, "UTF-8");


                    //Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    handler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    //Provjeriti izvor
    public class ConnectBT extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(),"Povezivanje....",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (bluetoothSocket == null || !isBluetoothConnected)
                {
                    ActivityCompat.requestPermissions(ConnectedDialog.this,new String[]{Manifest.permission.BLUETOOTH},1);
                    ActivityCompat.requestPermissions(ConnectedDialog.this,new String[]{Manifest.permission.BLUETOOTH_ADMIN},1);
                    ActivityCompat.requestPermissions(ConnectedDialog.this,new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED},1);

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    BluetoothDevice bluetoothRobot = mBluetoothAdapter.getRemoteDevice(ListOfDevices.iRobotMessenger.getAddress());

                    bluetoothSocket = bluetoothRobot.createInsecureRfcommSocketToServiceRecord(mUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast.makeText(getApplicationContext(),"Povezivanje neuspješno",Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Povezivanje uspješno",Toast.LENGTH_SHORT).show();
                isBluetoothConnected = true;
            }

        }
    }*/
}
