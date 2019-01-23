package hr.foi.nj3m.virtualwifi;


import hr.foi.nj3m.communications.IMessenger;
import hr.foi.nj3m.communications.VirtualMsgContainer;

public class VirtualWiFi implements IMessenger {

    public String recvdMessage = "";
    public int recvdUdaljenost = 0;
    VirtualMsgContainer vContainer = null;

    public VirtualWiFi (VirtualMsgContainer vc)
    {
        vContainer = vc;
    }


    @Override
    public void sendCommand(String command) {
        vContainer.setMessage(command);
    }

    @Override
    public void receive() {
        this.recvdMessage = vContainer.getMessage();

    }

}
