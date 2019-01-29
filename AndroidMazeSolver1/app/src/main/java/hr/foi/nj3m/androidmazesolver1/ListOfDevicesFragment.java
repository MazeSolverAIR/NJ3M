package hr.foi.nj3m.androidmazesolver1;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import hr.foi.nj3m.core.controllers.Factory;
import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;


public class ListOfDevicesFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvNewDevices;
    Button btnDiscover;
    SharedPreferences sharedPreferences;
    String deviceAddress;
    static String EXTRA_ADDRESS = null;
    ArrayAdapter<String> adapter;
    IMessenger iMessenger;
    IRobotConnector iRobotConnector;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_devices, container, false);
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        String myfolder=Environment.getExternalStorageDirectory()+"/"+"MazeSolver_Gallery";
        File f=Factory.CreateFile(myfolder);
        if(!f.exists())
            if(!f.mkdir()){
            }
            else{}
        else{}

    }

    @Override
    public void onStart() {
        super.onStart();

        sharedPreferences = getContext().getSharedPreferences("MazeSolver1", Context.MODE_PRIVATE);
        iRobotConnector = ConnectionController.getInstanceOfConnection();
        lvNewDevices = (ListView) getView().findViewById(R.id.lvNewDevices);
        btnDiscover = (Button) getView().findViewById(R.id.btnDiscoverDevices);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, iRobotConnector.getDeviceArray());
        lvNewDevices.setAdapter(adapter);
        lvNewDevices.setOnItemClickListener(ListOfDevicesFragment.this);

        /*switch(sharedPreferences.getString("TypeOfConnection", "DEFAULT")){
            case "bluetooth":
                break;
            case "virtualWifi":
                lvNewDevices.setAdapter(adapter);
                lvNewDevices.setOnItemClickListener(ListOfDevicesFragment.this);
                break;
        }*/

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRobotConnector.clearList();
                iRobotConnector.discover(mBroadcastReceiver);
            }
        });

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevices.add(device);
                iRobotConnector.addDevices(bluetoothDevices);
                lvNewDevices.setAdapter(adapter);
                //lvNewDevices.setOnItemClickListener(ListOfDevicesFragment.this);
            }
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    openConnectedDialog(deviceAddress);
                }
            }
            if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
                final ArrayList<WifiP2pDevice> wifiP2pDevices = new ArrayList<WifiP2pDevice>();
                WifiP2pManager wifiP2pManager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
                WifiP2pManager.Channel wifiP2pChannel = wifiP2pManager.initialize(getContext(), Looper.getMainLooper(), null);
                if (wifiP2pManager != null)
                    wifiP2pManager.requestPeers(wifiP2pChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peerList) {
                            for(WifiP2pDevice device : peerList.getDeviceList()){
                                wifiP2pDevices.add(device);
                            }
                            iRobotConnector.addDevices(wifiP2pDevices);
                            lvNewDevices.setAdapter(adapter);
                        }
                    });
                //lvNewDevices.setOnItemClickListener(ListOfDevicesFragment.this);
            }
            if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected())
                    openConnectedDialog(deviceAddress);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        deviceAddress = iRobotConnector.getDeviceAddress(position);
        iMessenger = iRobotConnector.connect(position);
        ConnectionController.setIMessenger(iMessenger);
        if (!iRobotConnector.deviceExists(iRobotConnector.getDeviceName(position)))
            iRobotConnector.setIntent(mBroadcastReceiver);
        else {
            switch (sharedPreferences.getString("TypeOfConnection", "DEFAULT")){
                case "virtualWifi":
                    openMaze();
                    break;
                default:
                    openConnectedDialog(deviceAddress);
                    break;
            }
        }
    }

    private void openConnectedDialog(String deviceAddress){
        Bundle bundle= new Bundle();
        bundle.putSerializable(EXTRA_ADDRESS,deviceAddress);
        Fragment fragment= new ConnectedDialogFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void openMaze(){
        Bundle bundle= new Bundle();
        Fragment fragment= new MazeFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}
