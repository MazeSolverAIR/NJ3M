package hr.foi.nj3m.core.controllers.threads;

import hr.foi.nj3m.communications.VirtualMsgContainer;

public class ConsumerThread extends Thread{

    private VirtualMsgContainer virtualMsgContainer;
    private String rcvdMsg = "";

    public ConsumerThread(VirtualMsgContainer vc)
    {
        virtualMsgContainer = vc;
    }

    @Override
    public void run() {
        while(true)
        {
            this.getMessage();
        }

    }

    private void getMessage()
    {
        this.rcvdMsg = virtualMsgContainer.getMessage();
    }

    public String GetRcvdMsg()
    {
        return this.rcvdMsg;
    }
}
