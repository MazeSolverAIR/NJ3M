package hr.foi.nj3m.virtualwifi;

import android.os.Message;

import hr.foi.nj3m.communications.IVirtualMessenger;


public class VirtualWiFi implements IVirtualMessenger {
    @Override
    public String receieveMsg(String msg) {
        return null;
    }

    @Override
    public boolean sendMsg(String msg) {
        return false;
    }
}
