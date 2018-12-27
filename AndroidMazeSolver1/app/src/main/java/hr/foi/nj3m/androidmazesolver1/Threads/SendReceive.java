package hr.foi.nj3m.androidmazesolver1.Threads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.telecom.Call;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.IRobotMessenger;

public class SendReceive extends Thread implements Callable {

    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket bluetoothSocket;
    private Handler handler;

    IRobotMessenger iRobotMessenger;

    public SendReceive(BluetoothDevice device, Handler handler){
        this.handler = handler;
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(mUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        iRobotMessenger = ConnectionController.getInstanceOfIRobot();
        iRobotMessenger.initializeSocket(bluetoothSocket, handler);
    }

    public void run(){
        iRobotMessenger.receive();
    }

    public void write(String command){
        iRobotMessenger.sendCommand(command);
    }

    @Override
    public SendReceive call() throws Exception {
        bluetoothSocket.connect();
        return this;
    }
}
