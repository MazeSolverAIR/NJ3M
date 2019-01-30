package hr.foi.nj3m.virtualwifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Button;

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
    ArrayList<String> device;
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
        device = new ArrayList<>();
        device.add("Virtual mBot");
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
        addDevices(device);
        return virtualDevices;
    }

    /**
     * Simulirana metoda koja poziva metodu za dodavanje uređaja u listu dostupnih uređaja. Svrha je osvježavanje liste dostupnih virtualnih uređaja.
     *
     * @param mBroadcastReceiver Slušatelj obavijesti o događaju
     */
    @Override
    public void discover(BroadcastReceiver mBroadcastReceiver) {
        addDevices(device);
    }

    /**
     * Metoda koja dodaje hardkodirani virtualni uređaj u listu dostupnih virtualnih uređaja.
     *
     * @param devices Hardkodirani virtualni uređaj koji se dodaje u listu dostupnih virtualih uređaja
     */
    @Override
    public void addDevices(ArrayList devices) {
        virtualDevices.add(String.valueOf(devices.get(0)));
    }

    /**
     * Brisanje svih uređaja u listi. Ova metoda poziva se pritiskom na tipku "Traži uređaje" kako bi se osvježio prikaz dostupnih uređaja.
     */
    @Override
    public void clearList() {
        virtualDevices.clear();
    }

    @Override
    public String getDeviceAddress(int position) {
        return null;
    }

    /**
     * Metoda koja vraća ime virtualnog uređaja na odabranoj poziciji u listi.
     *
     * @param position Pozicija u listi
     * @return         Ime virtualnog uređaja
     */
    @Override
    public String getDeviceName(int position) {
        return virtualDevices.get(0);
    }

    /**
     * Simulirana metoda koja uvijek vraća true, zato jer je virtualni uređaj uvijek dostupan, pošto je hardkodiran.
     *
     * @param deviceName Ime virtualnog uređaja.
     * @return           True
     */
    @Override
    public boolean deviceExists(String deviceName) {
        boolean exists = false;
        for (String device : virtualDevices)
            if (device.equals(String.valueOf(deviceName)))
                exists = true;
        return exists;
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
