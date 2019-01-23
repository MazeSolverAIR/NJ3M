package hr.foi.nj3m.core.controllers.threads;

import hr.foi.nj3m.communications.VirtualMsgContainer;

public class ProducerThread extends Thread {

    private VirtualMsgContainer virtualMsgContainer;
    private String msgToSend = "";

    public ProducerThread (VirtualMsgContainer vc)
    {
        virtualMsgContainer = vc;
    }

    public void setMsgToSend(String msg)
    {
        this.msgToSend = msg;
    }

    @Override
    public void run() {
        while(true)
        {
            virtualMsgContainer.setMessage(msgToSend);
        }
    }

}
