package hr.foi.nj3m.androidmazesolver1;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.IRobotConnector;

public class ConnectionTypeSelectionFragment extends Fragment {

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
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
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                final int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch (state) {
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
    IRobotConnector iRobotConnector;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    /**
     * Metoda koja se poziva za instanciranje View UI - a.
     *
     * @param inflater           objekt koji se koristi za inflateanje bilo kojeg Viewa u fragment
     * @param container          View objekta koji je roditelj Viewa koji fragment instancira
     * @param savedInstanceState Ako nije null tada se fragment rekonstruira iz prošlog spremljenog stanja
     * @return vraća View za fragment View, inače null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connection_type_selection, container, false);
    }

    /**
     * Metoda koja se poziva kad je fragment vidljiv korisniku. Obavezno se na početku
     * metode poziva bazna metoda.
     */
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
                if (iRobotConnector.isEnabled())
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
                if (iRobotConnector.isEnabled())
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

    /**
     * Metoda koja se poziva kad fragment više nije aktivan.
     */
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * Metoda čija je svrha otvratanje fragmenta ListOfDevicesFragment
     */
    private void openListOfDevices() {
        Fragment fragment = new ListOfDevicesFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

}
