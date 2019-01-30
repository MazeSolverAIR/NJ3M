package hr.foi.nj3m.communications;

import hr.foi.nj3m.interfaces.virtualCommunication.IMsgContainer;

public class VirtualMsgContainer implements IMsgContainer {

    private String Message = "";


    /**
     * Postavlja poruku u spremnik
     * @param msg poruka koja ce biti postavljena u spremnik
     */
    public void setMessage(String msg)
    {
        this.Message = msg;

    }

    /**
     * Cita poruku koja se nalazi u spremniku
     * @return procitana poruka iz spremnika
     */
    public String getMessage()
    {
        return this.Message;
    }

}
