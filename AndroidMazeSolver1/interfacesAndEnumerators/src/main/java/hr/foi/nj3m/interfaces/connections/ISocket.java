package hr.foi.nj3m.interfaces.connections;

import android.os.Handler;

import java.io.InputStream;
import java.io.OutputStream;

public interface ISocket {

    void initializeSocket(String address,Handler handler);
}
