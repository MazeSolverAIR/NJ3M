package hr.foi.nj3m.core.controllers.threads;
import android.os.Handler;


import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.communications.IMessenger;

public class SendReceive extends Thread {

    private IMessenger iMessenger;
    private Handler handler;

    /**
     * Konstruktor.
     *
     * @param handler Upravljač porukama. Ova metoda mu prosljeđuje zaprimljene poruke kao objekte tipa Message
     */
    public SendReceive(Handler handler){
        iMessenger = ConnectionController.getIMessenger();
        this.handler = handler;
    }

    /**
     * Prilikom pokretanja ove dretve počinju se zaprimati poruke od povezanog uređaja; poziva se metoda receive klase za komunikaciju.
     */
    public void run(){
        iMessenger.receive(handler);
    }

    /**
     * Pozivanje metode send klase za komunikaciju. Ova metoda pokreće se kada se zaprimi poruka od robota te mu se šalju instrukcije o tome što mu je dalje činiti.
     *
     * @param msg Instrukcije
     */
    public void write(final String msg){
        iMessenger.send(msg);

        System.out.println("Saljem"+msg);
    }
}
