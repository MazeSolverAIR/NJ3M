package hr.foi.nj3m.androidmazesolver1;


import android.bluetooth.BluetoothAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import android.widget.ImageButton;
import android.widget.Toast;
import static android.os.Environment.DIRECTORY_DCIM;
import java.io.File;

import hr.foi.nj3m.core.controllers.interfaceControllers.WirelessController;
import hr.foi.nj3m.interfaces.IWireless;

public class ConnectionTypeSelectionFragment extends Fragment {

    private static final String TAG = "MainActivity";
    public static BluetoothAdapter mBluetoothAdapter;
    IWireless iWireless;
    WifiManager wifiManager;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_connection_type_selection,container,false);
    }
    @Override
    public void onStart() {
        super.onStart();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final hr.foi.nj3m.androidmazesolver1.Bluetooth bluetooth = new hr.foi.nj3m.androidmazesolver1.Bluetooth(getActivity(), mBluetoothAdapter, mBroadcastReceiver);

        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        final ImageButton tipkaBluetooth = (ImageButton) getView().findViewById(R.id.btnBluetooth);
        final ImageButton tipkaWifi = (ImageButton) getView().findViewById(R.id.btnWifi);

        tipkaBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iWireless = WirelessController.createInstance(getActivity(), "bluetooth");
                //bluetooth.enableDisableBluetooth();
                if(mBluetoothAdapter.isEnabled()){
                    openListOfDevices();
                }
                else
                    iWireless.enableDisable(mBroadcastReceiver);
            }
        });

        tipkaWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iWireless = WirelessController.createInstance(getActivity().getApplicationContext(), "wifi");
                if(wifiManager.isWifiEnabled())
                    openListOfDevices();
                else
                    iWireless.enableDisable(mBroadcastReceiver);
            }
        });

    }

    public void openListOfDevices(){
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
            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
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
