package hr.foi.nj3m.communications;

import android.os.Handler;

public interface ISocket {

    void initializeSocket(String address,Handler handler);
}
