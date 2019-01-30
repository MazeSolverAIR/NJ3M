package hr.foi.nj3m.virtualwifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import java.util.ArrayList;
import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;
import hr.foi.nj3m.interfaces.virtualCommunication.IMsgContainer;

public class VirtualWiFi implements IMessenger, IDevice, IDiscover, IRobotConnector {

    public String recvdMessage = "";
    static IMsgContainer vContainer = null;
    ArrayList<String> virtualDevices;
    private WifiManager wifiManager;
    private Context context;

    private static IRobotConnector instanceOfVirtualWifi;

    public static IRobotConnector getVirtualWifiInstance(){
        return instanceOfVirtualWifi;
    }

    /**
     * Kreiranje instance VirtualWifi klase koja sadrži metode za povezivanje s virtualnim uređajem.
     *
     * @param context Osigurava pristup aplikacijskim resursima kao što je BroadcastReceiver
     * @return        Instanca klase koja se priključuje na sučelje IRobotConnector
     */
    public static IRobotConnector createVirtualWifiInstance(Context context){
        if(instanceOfVirtualWifi == null)
            instanceOfVirtualWifi = new VirtualWiFi(context);

        return instanceOfVirtualWifi;
    }

    /**
     * Konstruktor.
     *
     * @param context Osigurava pristup aplikacijskim resursima kao što je BroadcastReceiver
     */
    public VirtualWiFi (Context context)
    {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        virtualDevices = new ArrayList<>();
    }


    /**
     * Metoda koja preko virtualnog kanala šalje instrukcije virtualnom uređaju.
     *
     * @param command Instrukcije
     */
    @Override
    public void send(String command) {
        vContainer.setMessage(command);
    }

    /**
     * Metoda koja preko virtualnog kanala prima poruke od virtualnog uređaja.
     *
     * @param channel Virtualni kanal
     */
    @Override
    public void receive(Object channel) {
        vContainer = (IMsgContainer) channel;
        this.recvdMessage = vContainer.getMessage();
    }

    /**
     * Metoda za dohvaćanje zaprimljene poruke.
     *
     * @return Zaprimljena poruka
     */
    @Override
    public String getRcvdMsg() {
        return this.recvdMessage;
    }


    /**
     * Metoda koja vraća hardkodirani virtualni uređaj. Ova lista je izvor podataka za popunjavanje liste uređaja koja je prikazana na fragmentu ListOfDevicesFragment
     *
     * @return Hardkodirano ime virtualnog uređaja
     */
    @Override
    public ArrayList<String> getDeviceArray() {
        virtualDevices.add("Virtual mBot");
        return virtualDevices;
    }

    @Override
    public void discover(BroadcastReceiver mBroadcastReceiver) {

    }

    @Override
    public void addDevices(ArrayList devices) {
        virtualDevices.add("Virtual mBot");
    }

    @Override
    public void clearList() {
        virtualDevices.clear();
    }

    @Override
    public String getDeviceAddress(int position) {
        return null;
    }

    @Override
    public String getDeviceName(int position) {
        return null;
    }

    @Override
    public boolean deviceExists(String deviceName) {
        return true;
    }

    @Override
    public void setIntent(BroadcastReceiver broadcastReceiver) {

    }

    /**
     * Metoda koja vraća instancu kreiranog virtualnog uređaja.
     *
     * @param position Pozicija u listi dostupnih uređaja
     * @return         Instanca kreiranog virtualnog uređaja
     */
    @Override
    public IMessenger connect(int position) {
        return (IMessenger) instanceOfVirtualWifi;
    }

    /**
     * Metoda koja provjerava da li je WiFi uključen.
     *
     * @return True ako je WiFi uključen, inače false
     */
    @Override
    public boolean isEnabled() {
        if (wifiManager.isWifiEnabled())
            return true;
        else
            return false;
    }

    /**
     * Metoda koja pokreće namjeru za uključivanjem WiFi-ja ako nije uključen.
     *
     * @param mBroadcastReceiver Slušatelj obavijesti o događajima koje okida namjera
     */
    @Override
    public void enableDisable(BroadcastReceiver mBroadcastReceiver) {
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
            IntentFilter WiFiIntent = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
            context.registerReceiver(mBroadcastReceiver, WiFiIntent);
        }
    }
}
