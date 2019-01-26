package hr.foi.nj3m.androidmazesolver1;


import android.bluetooth.BluetoothAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageButton;
import android.widget.Toast;
import static android.os.Environment.DIRECTORY_DCIM;
import static java.lang.Thread.sleep;

import java.io.File;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.connections.IWireless;
import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.core.controllers.algorithms.AlgoritamVirtualRobot;
import hr.foi.nj3m.core.controllers.interfaceControllers.WirelessController;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.virtualmbot.VirtualMBot;
import hr.foi.nj3m.virtualwifi.VirtualWiFi;

public class ConnectionTypeSelectionFragment extends Fragment {

    IRobotConnector iRobotConnector;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_connection_type_selection,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final ImageButton tipkaBluetooth = (ImageButton) getView().findViewById(R.id.btnBluetooth);
        final ImageButton tipkaWifi = (ImageButton) getView().findViewById(R.id.btnWifi);
        final ImageButton tipkaVirtualRobot = (ImageButton) getView().findViewById(R.id.btnVirtualRobot);

        sharedPreferences = getContext().getSharedPreferences("MazeSolver1", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        tipkaBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                prefEditor.putString("TypeOfConnection", "bluetooth");
                prefEditor.commit();
                iRobotConnector = ConnectionController.creteInstance(sharedPreferences.getString("TypeOfConnection", "DEFAULT"), getActivity());
                if(iRobotConnector.isEnabled())
                    openListOfDevices();
                else
                    iRobotConnector.enableDisable(mBroadcastReceiver);
            }
        });

        tipkaWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                prefEditor.putString("TypeOfConnection", "wifi");
                prefEditor.commit();
                iRobotConnector = ConnectionController.creteInstance(sharedPreferences.getString("TypeOfConnection", "DEFAULT"), getActivity());
                if(iRobotConnector.isEnabled())
                    openListOfDevices();
                else
                    iRobotConnector.enableDisable(mBroadcastReceiver);



            }
        });

        tipkaVirtualRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putString("TypeOfConnection", "virtualWifi");
                prefEditor.commit();
                iRobotConnector = ConnectionController.creteInstance(sharedPreferences.getString("TypeOfConnection", "DEFAULT"), getActivity());
                openListOfDevices();
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    private void openListOfDevices(){
        Fragment fragment= new ListOfDevicesFragment();
        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "Bluetooth isključen", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context, "Bluetooth uključen", Toast.LENGTH_LONG).show();
                        openListOfDevices();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
            if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                final int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch (state){
                    case WifiManager.WIFI_STATE_DISABLED:
                        Toast.makeText(getActivity().getApplicationContext(), "WiFi isključen", Toast.LENGTH_LONG).show();
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Toast.makeText(getActivity().getApplicationContext(), "WiFi uključen", Toast.LENGTH_LONG).show();
                        openListOfDevices();
                        break;
                }
            }
        }
    };

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(getActivity().getExternalFilesDir(DIRECTORY_DCIM)+File.separator+"MazeSolverPictures");
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }

}
