package hr.foi.nj3m.communications;

import hr.foi.nj3m.interfaces.virtualCommunication.IMsgContainer;

public class VirtualMsgContainer implements IMsgContainer {

    private String Message = "";

    public void setMessage(String msg)
    {
        this.Message = msg;

    }

    public String getMessage()
    {
        return this.Message;
    }

}
