package hr.foi.nj3m.androidmazesolver1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;

public class ConnectedDialog extends AppCompatActivity {
    //Communication communication;
    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    private boolean isBluetoothConnected = false;

    Button btnSendControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_dialog);

        String deviceAddress = getIntent().getStringExtra(ListOfDevices.EXTRA_ADDRESS);

        btnSendControl = (Button) findViewById(R.id.btnSendControl);

        new ConnectBT().execute();

        //communication = new Communication(this, deviceAddress);

        btnSendControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfDevices.iRobotMessenger.sendCommand("LLijevo", bluetoothSocket);
                //communication.SendData(bluetoothSocket);
            }
        });
    }

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
    }
}
