package hr.foi.nj3m.interfaces.communications;

import android.os.Handler;

public interface IMessenger {
    void send(String message);
    void receive(Object channel);
    String getRcvdMsg();
}
