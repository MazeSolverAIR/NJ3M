package hr.foi.nj3m.androidmazesolver1.Threads;
import android.os.Handler;

import java.util.List;

import hr.foi.nj3m.core.controllers.enumeratorControllers.CommandsToMBotController;
import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.IMessageACK;
import hr.foi.nj3m.interfaces.IRobotMessenger;

public class SendReceive extends Thread {

    IRobotMessenger iRobotMessenger;
    IMessageACK messegeACK;

    public SendReceive(String address, Handler handler){
        messegeACK= new MSMessage();
        iRobotMessenger = ConnectionController.getInstanceOfIRobot();
        iRobotMessenger.initializeSocket(address, handler);
    }

    public void run(){
        iRobotMessenger.receive();
    }

    public void write(final List<CommandsToMBot> listaNaredbi){
        final int numberOfMessages=listaNaredbi.size();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (CommandsToMBot naredba:listaNaredbi)
                {
                    messegeACK.GetMessage(CommandsToMBotController.getStringFromComandEnum(naredba),numberOfMessages);
                    iRobotMessenger.sendCommand(messegeACK.ReturnFinalMessage());
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
