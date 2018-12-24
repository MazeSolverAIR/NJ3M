package hr.foi.nj3m.androidmazesolver1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.core.controllers.interfaceControllers.WirelessController;
import hr.foi.nj3m.interfaces.IConnections;
import hr.foi.nj3m.interfaces.IRobotMessenger;
import hr.foi.nj3m.interfaces.IWireless;


public class ListOfDevices extends Fragment implements AdapterView.OnItemClickListener {

    //Bluetooth bluetooth;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    Button btnDiscover;
    private String deviceAddress;
    public static String EXTRA_ADDRESS = null;

    IConnections iConnections;
    public static IRobotMessenger iRobotMessenger;
    IWireless iWireless;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_devices, container, false);
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();


        iWireless = WirelessController.createInstance(getActivity());

        lvNewDevices = (ListView) getView().findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();

        btnDiscover = (Button) getView().findViewById(R.id.btnDiscoverDevices);

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
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
                lvNewDevices.setOnItemClickListener(ListOfDevices.this);
            }
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    openConnectedDialog(deviceAddress);
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        deviceAddress = mBTDevices.get(position).getAddress();

        boolean exists = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        for(BluetoothDevice bluetoothDevice: bluetoothAdapter.getBondedDevices()){
            if(bluetoothDevice.getName().equals("Makeblock"))
                exists = true;
        }
        iConnections = ConnectionController.creteInstance("bluetooth", getActivity());
        if(exists){
            openConnectedDialog(deviceAddress);
        }
        else {
            iRobotMessenger = iConnections.connect(mBTDevices, position);
            IntentFilter bondedFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, bondedFilter);
        }
        iConnections = ConnectionController.creteInstance("bluetooth", getActivity());

        iRobotMessenger = iConnections.connect(mBTDevices, position);

        IntentFilter bondedFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver, bondedFilter);
    }

    private void openConnectedDialog(String deviceAddress){
        Bundle bundle= new Bundle();
        bundle.putSerializable(EXTRA_ADDRESS,deviceAddress);
        Fragment fragment= new ConnectedDialog();
        fragment.setArguments(bundle);
        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}
