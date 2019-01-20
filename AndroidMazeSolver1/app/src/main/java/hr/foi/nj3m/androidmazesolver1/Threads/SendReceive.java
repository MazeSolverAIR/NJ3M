package hr.foi.nj3m.androidmazesolver1.Threads;
import android.os.Handler;
import android.util.Log;

import java.util.List;

import hr.foi.nj3m.core.controllers.enumeratorControllers.CommandsToMBotController;
import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.core.controllers.interfaceControllers.MSMessageToACK;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.IMessageACK;
import hr.foi.nj3m.interfaces.IRobotMessenger;

public class SendReceive extends Thread {

    private IRobotMessenger iRobotMessenger;
    private IMessageACK messegeACK;

    private List<CommandsToMBot> listOfLastCommands;

    public SendReceive(String address, Handler handler){
        //messegeACK= new MSMessageToACK();
        iRobotMessenger = ConnectionController.getInstanceOfIRobot();
        iRobotMessenger.initializeSocket(address, handler);
    }

    public void run(){
        iRobotMessenger.receive();
    }

    public void write(final String msg){

        new Thread(new Runnable() {
            @Override
            public void run() {
                    iRobotMessenger.sendCommand(msg);
                    Log.d("Saljem", msg);



                }
        }).start();
    }

    public void writeAgain()
    {
        //if(this.listOfLastCommands != null && this.listOfLastCommands.size() > 1)
            //write(this.listOfLastCommands);
    }
}
