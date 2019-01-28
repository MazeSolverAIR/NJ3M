package hr.foi.nj3m.wifi;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import hr.foi.nj3m.interfaces.communications.IMessenger;

public class WiFiCommunicator implements IMessenger {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private String address;
    private Socket socket;
    private String obtainedMsg;

    private static WiFiCommunicator InstanceOfSender;

    public static WiFiCommunicator createWiFiSender(String address)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new WiFiCommunicator(address);

        return InstanceOfSender;
    }

    private WiFiCommunicator(String address)
    {
        this.address = address;
        socket = new Socket();
        initializeSocket();
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
    public void receive(Object channel) {
        handler = (Handler) channel;
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket != null){
            try {
                bytes = inputStream.read(buffer);
                if (bytes > 0){
                    Message msg = handler.obtainMessage(1, bytes, -1, buffer);
                    msg.sendToTarget();
                    obtainedMsg = msg.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getRcvdMsg() {
        return this.obtainedMsg;
    }

    private void initializeSocket(){
        try {
            if (socket != null){
                socket.connect(new InetSocketAddress(address, 8888), 500);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            }
            else
                handler.obtainMessage(2, "WiFi komunikacija nije implementirana");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
