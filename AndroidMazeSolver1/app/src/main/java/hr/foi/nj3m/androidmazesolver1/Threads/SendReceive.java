package hr.foi.nj3m.androidmazesolver1.Threads;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import hr.foi.nj3m.core.controllers.enumeratorControllers.CommandsToMBotController;
import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
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

    public void write(final List<CommandsToMBot> listaNaredbi){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (CommandsToMBot naredba:listaNaredbi)
                {
                    String stringNaredba = CommandsToMBotController.getStringFromComandEnum(naredba);
                    iRobotMessenger.sendCommand(stringNaredba);
                    try {
                        sleep(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
