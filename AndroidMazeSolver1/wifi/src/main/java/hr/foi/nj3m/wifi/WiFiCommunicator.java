package hr.foi.nj3m.wifi;

import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import hr.foi.nj3m.interfaces.communications.IMessenger;

public class WiFiCommunicator implements IMessenger {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;

    private static WiFiCommunicator InstanceOfSender;

    public static WiFiCommunicator createWiFiSender()
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new WiFiCommunicator();

        return InstanceOfSender;
    }

    private WiFiCommunicator()
    {
        this.socket = WiFi.getSocket();
        this.inputStream = WiFi.getInputStream();
        this.outputStream = WiFi.getOutputStream();
        this.handler = WiFi.getHandler();
    }

    @Override
    public void send(String command) {
        byte[] message = command.getBytes();
        try {
            outputStream.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket != null){
            try {
                bytes = inputStream.read(buffer);
                if (bytes > 0){
                    handler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
