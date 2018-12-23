package hr.foi.nj3m.androidmazesolver1.Threads;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.IRobotMessenger;

public class SendReceive extends Thread {

    IRobotMessenger iRobotMessenger;

    public SendReceive(BluetoothSocket socket, Handler handler){
        iRobotMessenger = ConnectionController.getInstanceOfIRobot();
        iRobotMessenger.initializeSocket(socket, handler);
    }

    public void run(){
        iRobotMessenger.receive();
    }

    public void write(String command){
        iRobotMessenger.sendCommand(command);
    }
}
