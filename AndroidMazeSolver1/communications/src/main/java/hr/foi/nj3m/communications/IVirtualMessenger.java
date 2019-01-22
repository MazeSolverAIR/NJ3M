package hr.foi.nj3m.communications;

import android.os.Message;

public interface IVirtualMessenger {
    String receieveMsg(String msg);
    boolean sendMsg(String msg);
}
