package hr.foi.nj3m.androidmazesolver1.Threads;
import android.os.Handler;
import java.util.concurrent.Callable;

import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.IRobotMessenger;

public class SendReceive extends Thread implements Callable {

    private Handler handler;
    private String address;

    IRobotMessenger iRobotMessenger;

    public SendReceive(String address, Handler handler){
        this.handler = handler;
        this.address = address;
        iRobotMessenger = ConnectionController.getInstanceOfIRobot();
        iRobotMessenger.initializeSocket(address, handler);
    }

    public void run(){
        iRobotMessenger.receive();
    }

    public void write(String command){
        iRobotMessenger.sendCommand(command);
    }

    @Override
    public SendReceive call() {
        //iRobotMessenger.initializeSocket(address, handler);
        return this;
    }
}
