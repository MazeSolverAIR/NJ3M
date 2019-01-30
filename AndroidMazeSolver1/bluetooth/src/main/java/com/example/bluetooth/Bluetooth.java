package com.example.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IConnection;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;
import hr.foi.nj3m.interfaces.connections.ISocket;
import hr.foi.nj3m.interfaces.connections.IWireless;

import static android.content.ContentValues.TAG;

public class Bluetooth extends Activity implements IWireless, IDiscover, IRobotConnector {

    BluetoothAdapter mBluetoothAdapter;
    Context context;
    ArrayList<BluetoothDevice> bluetoothDevices;
    ArrayList<String> deviceNameArray;

    private static IRobotConnector instanceOfBluetooth;

    public static IRobotConnector getBluetoothInstance(){
        return instanceOfBluetooth;
    }

    /**
     * Kreiranje instance Bluetooth klase koja sadrži metode za povezivanje s uređajem.
     *
     * @param context Osigurava pristup aplikacijskim resursima kao što je BroadcastReceiver
     * @return        Instanca klase koja se priključuje na sučelje IRobotConnector
     */
    public static IRobotConnector createBluetoothInstance(Context context){
        if(instanceOfBluetooth == null)
            instanceOfBluetooth = new Bluetooth(context);

        return instanceOfBluetooth;
    }

    /**
     * Konstruktor
     * @param context Osigurava pristup aplikacijskim resursima kao što je BroadcastReceiver
     */
    private Bluetooth(Context context){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        bluetoothDevices = new ArrayList<>();
        deviceNameArray = new ArrayList<>();
    }

    /**
     * Metoda koja dodaje pronađene uređaje u listu.
     *
     * @param devices Pronađeni uređaji
     */
    @Override
    public void addDevices(ArrayList devices) {
        this.bluetoothDevices.addAll(devices);
        refreshNameArray();
    }

    /**
     * Brisanje svih uređaja u listi. Ova metoda poziva se pritiskom na tipku "Traži uređaje" kako bi se osvježio prikaz dostupnih uređaja.
     */
    @Override
    public void clearList() {
        bluetoothDevices.clear();
    }

    /**
     * Metoda koja vraća listu koja sadrži imena dostupnih uređaja. Ova lista je izvor podataka za popunjavanje liste uređaja koja je prikazana na fragmentu ListOfDevicesFragment.
     *
     * @return Lista imena dostupnih uređaja
     */
    @Override
    public ArrayList<String> getDeviceArray() {
        return deviceNameArray;
    }

    /**
     * Privatna metoda koja očisti listu dostupnih uređaja prije nego što se dodaju novi uređaji kako ne bi došlo do duplikata ili postojanja uređaja u listi koji više nisu dostupni.
     */
    private void refreshNameArray() {
        deviceNameArray.clear();
        for (BluetoothDevice device : bluetoothDevices)
            deviceNameArray.add(device.getName());
    }

    /**
     * Metoda koja vraća adresu uređaja na odabranoj poziciji u listi.
     *
     * @param position Pozicija u listi
     * @return         Adresa uređaja
     */
    @Override
    public String getDeviceAddress(int position) {
        return bluetoothDevices.get(position).getAddress();
    }

    /**
     * Metoda koja vraća ime uređaja na odabranoj poziciji u listi.
     *
     * @param position Pozicija u listi
     * @return         Ime uređaja
     */
    @Override
    public String getDeviceName(int position) {
        return bluetoothDevices.get(position).getName();
    }

    /**
     * Metoda koja provjerava nalazi li se odabrani uređaj u listi već uparenih uređaja.
     *
     * @param deviceName Ime uređaja
     * @return           Ako se uređaj nalazi u listi uparenih uređaja, vraća true, inače false
     */
    @Override
    public boolean deviceExists(String deviceName) {
        boolean exists = false;
        for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()){
            if (device.getName().equals(deviceName)) {
                exists = true;
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        return exists;
    }

    /**
     * Postavljanje namjere koja šalje obavijest slušatelju obavijesti o događaju ako je došlo do promjene stanja povezanosti s uređajem.
     *
     * @param broadcastReceiver Slušatelj obavijesti o događaju
     */
    @Override
    public void setIntent(BroadcastReceiver broadcastReceiver) {
        IntentFilter bondedFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        context.registerReceiver(broadcastReceiver, bondedFilter);
    }

    /**
     * Metoda koja stvara sponu sa odabranim uređajem ako ona ne postoji od prije.
     *
     * @param position Pozicija uređaja u listi dostupnih uređaja
     * @return         Instanca klase za komunikaciju s uređajem koju je potrebno priključiti na sučelje IMessenger
     */
    @Override
    public IMessenger connect(int position) {
        mBluetoothAdapter.cancelDiscovery();
        String deviceAddress = getDeviceAddress(position);
        if (!deviceExists(getDeviceName(position)))
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                bluetoothDevices.get(position).createBond();
            }
        return BluetoothCommunicator.createBluetoothSender(context, deviceAddress);
    }

    /**
     * Metoda koja provjerava da li je Bluetooth uključen.
     *
     * @return True ako je Bluetooth uključen, inače false
     */
    @Override
    public boolean isEnabled() {
        if (mBluetoothAdapter.isEnabled())
            return true;
        else
            return false;
    }

    /**
     * Metoda koja pokreće namjeru za uključivanjem Bluetootha ako nije uključen.
     *
     * @param mBroadcastReceiver Slušatelj obavijesti o događajima koje okida namjera
     */
    @Override
    public void enableDisable(BroadcastReceiver mBroadcastReceiver) {
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(mBroadcastReceiver, BTIntent);
        }
    }

    /**
     * Metoda koja pokreće metodu BluetoothAdaptera za pokretanje pronalaska dostupnih uređaja. Registrira slušatelj obavijesti o događajima na pronalazak novih dostupnih uređaja.
     *
     * @param mBroadcastReceiver Slušatelj obavijesti o događajima koje okida namjera
     */
    @Override
    public void discover(BroadcastReceiver mBroadcastReceiver) {
        if(mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
            Toast.makeText(context, "Tražim uređaje...", Toast.LENGTH_LONG).show();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            Toast.makeText(context, "Tražim uređaje...", Toast.LENGTH_LONG).show();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
    }

    /**
     * Metoda koja provjerava dozvolu za pristup aplikacije lokaciji uređaja. Ako pristup lokaciji nije dozvoljen, traži se od korisnika da ga dozvoli.
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }else{
                Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
            }
        }
    }
/*
    @Override
    public void initializeSocket(String address, Handler handler) {
        try {
            bluetoothSocket = mBluetoothAdapter.getRemoteDevice(address).createRfcommSocketToServiceRecord(mUUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.handler = handler;

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

    protected static InputStream getInputStream() {
        return inputStream;
    }

    protected static OutputStream getOutputStream() {
        return outputStream;
    }

    protected static Handler getHandler() {
        return handler;
    }

    protected static BluetoothSocket getBluetoothSocket(){
        return bluetoothSocket;
    }*/
}
