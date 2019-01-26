package hr.foi.nj3m.core.controllers.threads;
import android.os.Handler;
import android.util.Log;


import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.communications.IMessenger;

public class SendReceive extends Thread {

    private IMessenger iMessenger;
    private Handler handler;

    public SendReceive(Handler handler){
        iMessenger = ConnectionController.getiMessenger();
        //iMessenger.initializeSocket(address, handler);
        this.handler = handler;
    }

    public void run(){
        iMessenger.receive(handler);
    }

    public void write(final String msg){
        iMessenger.send(msg);
    }
}
