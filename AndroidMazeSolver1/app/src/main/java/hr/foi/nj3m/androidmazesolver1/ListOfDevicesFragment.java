package hr.foi.nj3m.androidmazesolver1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.core.controllers.interfaceControllers.WirelessController;
import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;
import hr.foi.nj3m.interfaces.IWireless;


public class ListOfDevicesFragment extends Fragment implements AdapterView.OnItemClickListener {

    //Bluetooth bluetooth;
    //public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    //public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    Button btnDiscover;
    private String deviceAddress;
    public static String EXTRA_ADDRESS = null;

    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    IConnections iConnections;
    public static IRobotMessenger iRobotMessenger;
    IWireless iWireless;
    BluetoothAdapter bluetoothAdapter;
    //Set<BluetoothDevice> bluetoothDevices;

    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_devices, container, false);
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        String myfolder=Environment.getExternalStorageDirectory()+"/"+"MazeSolver_Gallery";
        File f=new File(myfolder);
        if(!f.exists())
            if(!f.mkdir()){
            }
            else{}
        else{}

    }

    @Override
    public void onStart() {
        super.onStart();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //bluetoothDevices = bluetoothAdapter.getBondedDevices();

        iWireless = WirelessController.getInstanceOfIWireless();

        //iWireless = WirelessController.createInstance(getActivity());

        lvNewDevices = (ListView) getView().findViewById(R.id.lvNewDevices);
        //mBTDevices = new ArrayList<>();

        btnDiscover = (Button) getView().findViewById(R.id.btnDiscoverDevices);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArrayList);

        //bluetooth = new Bluetooth(this, MainActivity.mBluetoothAdapter, mBroadcastReceiver);

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bluetooth.discover();
                iWireless.discover(mBroadcastReceiver);
            }
        });

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                iConnections = ConnectionController.creteInstance("bluetooth", getActivity());
                ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                stringArrayList.add(device.getName());
                adapter.notifyDataSetChanged();
                lvNewDevices.setAdapter(adapter);
                bluetoothDevices.add(device);
                iConnections.addDevices(bluetoothDevices);
                /*mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);*/
                lvNewDevices.setOnItemClickListener(ListOfDevicesFragment.this);
            }
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    openConnectedDialog(deviceAddress);
                }
            }
            if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
                final List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
                WifiP2pManager wifiP2pManager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
                WifiP2pManager.Channel wifiP2pChannel = wifiP2pManager.initialize(getContext(), Looper.getMainLooper(), null);
                wifiP2pManager.requestPeers(wifiP2pChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peerList) {
                        if(!peerList.getDeviceList().equals(peers)){
                            peers.clear();
                            peers.addAll(peerList.getDeviceList());

                            deviceNameArray = new String[peerList.getDeviceList().size()];
                            deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                            int index = 0;

                            for(WifiP2pDevice device : peerList.getDeviceList()){
                                deviceNameArray[index] = device.deviceName;
                                deviceArray[index] = device;
                                index++;
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, deviceNameArray);
                            lvNewDevices.setAdapter(adapter);
                        }

                        if(peers.size() == 0){
                            Toast.makeText(getContext(), "Uređaji nisu pronađeni", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //deviceAddress = mBTDevices.get(position).getAddress();
        deviceAddress = iConnections.getDeviceAddress(position);

        /*boolean exists = false;
        for(BluetoothDevice bluetoothDevice: bluetoothDevices){
            if(bluetoothDevice.getName().equals("Makeblock"))
                exists = true;
        }*/

        //iConnections = ConnectionController.creteInstance("bluetooth", getActivity());

        if(iConnections.deviceExists(iConnections.getDeviceName(position)))
            openConnectedDialog(deviceAddress);
        /*
        if(exists){
            openConnectedDialog(deviceAddress);
        }*/
        else {
            iRobotMessenger = iConnections.connect(position);
            //iRobotMessenger = iConnections.connect(mBTDevices, position);
            IntentFilter bondedFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, bondedFilter);
            getActivity().registerReceiver(mBroadcastReceiver, bondedFilter);
        }
        //iConnections = ConnectionController.creteInstance("bluetooth", getActivity());

        //iRobotMessenger = iConnections.connect(mBTDevices, position);

        /*IntentFilter bondedFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver, bondedFilter);*/
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
}
