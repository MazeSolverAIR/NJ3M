package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import hr.foi.nj3m.interfaces.communications.IMessenger;

public class BluetoothCommunicator implements IMessenger {

    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Context context;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothSocket bluetoothSocket;
    private Handler handler;
    private String deviceAddress;
    private BluetoothAdapter bluetoothAdapter;
    private String obtainedMsg;

    private static IMessenger InstanceOfSender;

    /**
     * Kreiranje instance BluetoothCommunicator klase koja sadrži metode za komunikaciju s uređajem.
     *
     * @param context       Osigurava pristup aplikacijskim resursima
     * @param deviceAddress Adresa uređaja s kojim smo se povezali. Potrebna za stvaranje priključka za komunikaciju (socketa)
     * @return              Instanca klase koja se priključuje na sučelje IMessenger
     */
    public static IMessenger createBluetoothSender(Context context, String deviceAddress)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothCommunicator(context, deviceAddress);

        return InstanceOfSender;
    }

    public static IMessenger getBluetoothSender(){
        return InstanceOfSender;
    }

    /**
     * Konstruktor
     *
     * @param context       Osigurava pristup aplikacijskim resursima
     * @param deviceAddress Adresa uređaja s kojim smo se povezali. Potrebna za stvaranje priključka za komunikaciju (socketa)
     */
    private BluetoothCommunicator(Context context, String deviceAddress)
    {
        this.context = context;
        this.deviceAddress = deviceAddress;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initializeSocket();
    }

    /**
     * Metoda koja preko izlaznog toka šalje instrukcije povezanom uređaju kao niz bajtova.
     *
     * @param command Instrukcije
     */
    @Override
    public void send(String command) {
        byte[] message = command.getBytes();
        try {
            outputStream.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda koja preko ulaznog toka zaprima poruke koje šalje povezani uređaj kao niz bajtova.
     *
     * @param channel Upravljač porukama. Ova metoda mu prosljeđuje zaprimljene poruke kao objekte tipa Message
     */
    @Override
    public void receive(Object channel) {
        handler = (Handler) channel;
        byte[] buffer = new byte[1024];
        int bytes;
        while (bluetoothSocket != null){
            try {
                bytes = inputStream.read(buffer);
                Message msg = handler.obtainMessage(1, bytes, -1, buffer);
                msg.sendToTarget();
                obtainedMsg = msg.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(NullPointerException ex)
            {
                obtainedMsg = "";
            }
        }
    }

    /**
     * Metoda za dohvaćanje zaprimljene poruke.
     *
     * @return Zaprimljena poruka
     */
    @Override
    public String getRcvdMsg() {
        return obtainedMsg;
    }

    /**
     * Metoda za inicijaliziranje komunikacijskog priključka (socketa) i stvaranje komunikacijskog kanala sa povezanim uređajem.
     */
    private void initializeSocket(){
        try {
            bluetoothSocket = bluetoothAdapter.getRemoteDevice(deviceAddress).createRfcommSocketToServiceRecord(mUUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
