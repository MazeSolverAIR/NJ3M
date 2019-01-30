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

    /**
     * Kreiranje instance WiFiCommunicator klase koja sadrži metode za komunikaciju s uređajem.
     *
     * @param address Adresa uređaja s kojim smo se povezali. Potrebna za stvaranje priključka za komunikaciju (socketa)
     * @return        Instanca klase koja se priključuje na sučelje IMessenger
     */
    public static IMessenger createWiFiSender(String address)
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new WiFiCommunicator(address);

        return InstanceOfSender;
    }

    /**
     * Konstruktor
     *
     * @param address Adresa uređaja s kojim smo se povezali. Potrebna za stvaranje priključka za komunikaciju (socketa)
     */
    private WiFiCommunicator(String address)
    {
        this.address = address;
        socket = new Socket();
        initializeSocket();
    }

    /**
     * Metoda koja preko izlaznog toka šalje instrukcije povezanom uređaju kao niz bajtova.
     *
     * @param command Instrukcije
     */
    @Override
    public void send(String command) {
        byte[] message = command.getBytes();
        try {
            outputStream.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda koja preko ulaznog toka zaprima poruke koje šalje povezani uređaj kao niz bajtova.
     *
     * @param channel Upravljač porukama. Ova metoda mu prosljeđuje zaprimljene poruke kao objekte tipa Message
     */
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

    /**
     * Metoda za dohvaćanje zaprimljene poruke.
     *
     * @return Zaprimljena poruka
     */
    @Override
    public String getRcvdMsg() {
        return this.obtainedMsg;
    }

    /**
     * Metoda za inicijaliziranje komunikacijskog priključka (socketa) i stvaranje komunikacijskog kanala sa povezanim uređajem.
     */
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
