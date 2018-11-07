package hr.foi.nj3m.androidmazesolver1;


import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;

import com.example.bluetooth.Bluetooth;

import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final hr.foi.nj3m.androidmazesolver1.Bluetooth bluetooth = new hr.foi.nj3m.androidmazesolver1.Bluetooth(this, mBluetoothAdapter, mBroadcastReceiver1);

        final ImageButton tipkaBluetooth = (ImageButton) findViewById(R.id.btnBluetooth);

        tipkaBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.enableDisableBluetooth();
            }
        });

    }

    public void openListOfDevices(){
        Intent intent = new Intent(this,ListOfDevices.class);
        startActivity(intent);
    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        Toast.makeText(context, "Bluetooth on", Toast.LENGTH_LONG).show();
                        openListOfDevices();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };
}
