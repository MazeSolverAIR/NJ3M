package hr.foi.nj3m.androidmazesolver1.Threads;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.Callable;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.IRobotMessenger;

public class SendReceive extends Thread {

    IRobotMessenger iRobotMessenger;

    public SendReceive(String address, Handler handler){
        iRobotMessenger = ConnectionController.getInstanceOfIRobot();
        iRobotMessenger.initializeSocket(address, handler);
    }

    public void run(){
        iRobotMessenger.receive();
    }

    public void write(final String command){
        new Thread(new Runnable() {
            @Override
            public void run() {
                iRobotMessenger.sendCommand(command);
                Log.d("Poslana poruka", command);
            }
        }).start();
    }
}
