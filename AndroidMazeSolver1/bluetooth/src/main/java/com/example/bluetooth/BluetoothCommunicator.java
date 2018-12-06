package com.example.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class BluetoothCommunicator implements IRobotMessenger {

    volatile boolean stopWorker;
    byte[] readBuffer;
    int readBufferPosition;
    byte[] receivedData;

    Context context;
    String deviceAddress;
    BluetoothSocket bluetoothSocket = null;

    private static BluetoothCommunicator InstanceOfSender;

    protected static BluetoothCommunicator createBluetoothSender(Context context, String deviceAddress)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothCommunicator(context, deviceAddress);

        return InstanceOfSender;
    }

    private BluetoothCommunicator(Context context, String deviceAddress)
    {
        this.context = context;
        this.deviceAddress = deviceAddress;
    }

    @Override
    public boolean sendCommand(String command, BluetoothSocket bluetoothSocket) {
        byte[] message = command.getBytes();
        try{
            bluetoothSocket.getOutputStream().write(message);
            Log.d("TAG", "poslana poruka" + message.toString());
        }catch (IOException e){

        }
        return false;
    }

    @Override
    public String getAddress() {
        return deviceAddress;
    }

    // TODO: 12/6/2018 POTREBNO TESTIRATI!!! 
    @Override
    public byte[] receive(final Handler handler) {
        final byte delimiter = 10; //ASCII kod za newline
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker){
                    try {
                        int bytesAvailable = bluetoothSocket.getInputStream().available();
                        if(bytesAvailable > 0){
                            byte[] packetBytes = new byte[bytesAvailable];
                            bluetoothSocket.getInputStream().read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++){
                                byte b = packetBytes[i];
                                if(b== delimiter){
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO: 12/6/2018 Spremiti podatke nekam
                                            // myLabel.setText(data);
                                            receivedData = data.getBytes();
                                        }
                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }catch (IOException e){
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
        return receivedData;
    }
}
