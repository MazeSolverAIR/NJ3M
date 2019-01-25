package hr.foi.nj3m.virtualwifi;


import java.util.ArrayList;

import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IDevice;

public class VirtualWiFi implements IMessenger, IDevice {

    public String recvdMessage = "";
    public int recvdUdaljenost = 0;
    VirtualMsgContainer vContainer = null;

    public VirtualWiFi (VirtualMsgContainer vc)
    {
        vContainer = vc;
    }


    @Override
    public void send(String command) {
        vContainer.setMessage(command);
    }

    @Override
    public void receive() {
        this.recvdMessage = vContainer.getMessage();

    }

    @Override
    public ArrayList<String> getDeviceArray() {
        return null;
    }
}
