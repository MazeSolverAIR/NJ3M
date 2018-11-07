package hr.foi.nj3m.androidmazesolver1;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Bluetooth {
    private final Context context;
    BluetoothAdapter mBluetoothAdapter;
    BroadcastReceiver mBroadcastReceiver;

    public Bluetooth(Context context, BluetoothAdapter mBluetoothAdapter, BroadcastReceiver mBroadcastReceiver){
        this.context = context;
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.mBroadcastReceiver = mBroadcastReceiver;
    }

    public void enableDisableBluetooth(){
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(mBroadcastReceiver, BTIntent);
        }
    }

    public void discover(){
        if(mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
            Toast.makeText(context, "Discovering", Toast.LENGTH_LONG).show();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            //Toast.makeText(context, "Discovering", Toast.LENGTH_LONG).show();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
    }

    public void createBond(ArrayList<BluetoothDevice> mBTDevices, int position){
        mBluetoothAdapter.cancelDiscovery();
        String deviceName = mBTDevices.get(position).getName();
        String deviceAddress = mBTDevices.get(position).getAddress();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            mBTDevices.get(position).createBond();
        }
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }else{
                Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
            }
        }
    }
}
