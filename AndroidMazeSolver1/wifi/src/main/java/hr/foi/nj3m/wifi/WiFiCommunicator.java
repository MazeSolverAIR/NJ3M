package hr.foi.nj3m.wifi;

import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import hr.foi.nj3m.communications.IRobotMessenger;

public class WiFiCommunicator implements IRobotMessenger {

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
        socket = new Socket();
    }

    @Override
    public void initializeSocket(String address, Handler handler) {
        try {
            socket.connect(new InetSocketAddress(address, 8888), 500);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.handler = handler;
    }

    @Override
    public void sendCommand(String command) {
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
