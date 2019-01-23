package hr.foi.nj3m.core.controllers.threads;
import android.os.Handler;
import android.util.Log;


import hr.foi.nj3m.communications.IRobotMessenger;
import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;

public class SendReceive extends Thread {

    private IRobotMessenger iRobotMessenger;

    public SendReceive(String address, Handler handler){
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
}
