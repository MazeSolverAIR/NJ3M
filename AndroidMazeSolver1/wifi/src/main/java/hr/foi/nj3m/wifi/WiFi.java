package hr.foi.nj3m.wifi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IConnection;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;
import hr.foi.nj3m.interfaces.connections.IWireless;

import static hr.foi.nj3m.wifi.WiFiCommunicator.createWiFiSender;

public class WiFi implements IWireless, IDiscover, IRobotConnector {

    private WifiManager wifiManager;
    private Context context;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel wifiP2pChannel;
    private IntentFilter intentFilter;
    private ArrayList<WifiP2pDevice> wifiP2pDevices;
    private WifiP2pConfig wifiP2pConfig;
    private ArrayList<String> deviceNameArray;
    private static Socket socket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static Handler handler;

    private static IRobotConnector InstanceOfWiFi;

    public static IRobotConnector getWiFiInstance() {
        return InstanceOfWiFi;
    }

    /**
     * Kreiranje instance WiFi klase koja sadrži metode za povezivanje s uređajem.
     *
     * @param context Osigurava pristup aplikacijskim resursima kao što je BroadcastReceiver
     * @return        Instanca klase koja se priključuje na sučelje IRobotConnector
     */
    public static IRobotConnector createWiFiInstance(Context context) {
        if (InstanceOfWiFi == null)
            InstanceOfWiFi = new WiFi(context);

        return InstanceOfWiFi;
    }

    /**
     * Konstruktor
     *
     * @param context Osigurava pristup aplikacijskim resursima kao što je BroadcastReceiver
     */
    private WiFi(Context context) {
        this.context = context;

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        wifiP2pChannel = wifiP2pManager.initialize(context, context.getMainLooper(), null);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifiP2pDevices = new ArrayList<>();
        deviceNameArray = new ArrayList<>();
    }

    /**
     * Metoda koja dodaje pronađene uređaje u listu.
     *
     * @param devices Pronađeni uređaji
     */
    @Override
    public void addDevices(ArrayList devices) {
        this.wifiP2pDevices = devices;
        refreshNameArray();
    }

    /**
     * Privatna metoda koja očisti listu dostupnih uređaja prije nego što se dodaju novi uređaji kako ne bi došlo do duplikata ili postojanja uređaja u listi koji više nisu dostupni.
     */
    private void refreshNameArray() {
        deviceNameArray.clear();
        for (WifiP2pDevice device : wifiP2pDevices)
            deviceNameArray.add(device.deviceName);
    }

    /**
     * Brisanje svih uređaja u listi. Ova metoda poziva se pritiskom na tipku "Traži uređaje" kako bi se osvježio prikaz dostupnih uređaja.
     */
    @Override
    public void clearList() {
        wifiP2pDevices.clear();
    }

    /**
     * Metoda koja vraća listu koja sadrži imena dostupnih uređaja. Ova lista je izvor podataka za popunjavanje liste uređaja koja je prikazana na fragmentu ListOfDevicesFragment.
     *
     * @return Lista imena dostupnih uređaja
     */
    @Override
    public ArrayList<String> getDeviceArray() {
        return deviceNameArray;
    }

    /**
     * Metoda koja vraća adresu uređaja na odabranoj poziciji u listi.
     *
     * @param position Pozicija u listi
     * @return         Adresa uređaja
     */
    private String getDeviceAddress(int position) {
        return wifiP2pDevices.get(position).deviceAddress;
    }

    /**
     * Metoda koja vraća ime uređaja na odabranoj poziciji u listi.
     *
     * @param position Pozicija u listi
     * @return         Ime uređaja
     */
    @Override
    public String getDeviceName(int position) {
        return wifiP2pDevices.get(position).deviceName;
    }

    @Override
    public boolean deviceExists(String deviceName) {
        return false;
    }

    /**
     * Postavljanje namjere koja šalje obavijest slušatelju obavijesti o događaju ako je došlo do promjene stanja povezanosti s uređajem.
     *
     * @param broadcastReceiver Slušatelj obavijesti o događaju
     */
    @Override
    public void setIntent(BroadcastReceiver broadcastReceiver) {
        IntentFilter connectedFilter = new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        context.registerReceiver(broadcastReceiver, connectedFilter);
    }

    /**
     * Metoda koja stvara vezu sa odabranim uređajem ako ona ne postoji od prije.
     *
     * @param position Pozicija uređaja u listi dostupnih uređaja
     * @return         Instanca klase za komunikaciju s uređajem koju je potrebno priključiti na sučelje IMessenger
     */
    @Override
    public IMessenger connect(final int position) {
        wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = getDeviceAddress(position);
        wifiP2pManager.connect(wifiP2pChannel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Spojen sa " + getDeviceName(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Greška prilikom spajanja", Toast.LENGTH_LONG).show();
            }
        });
        return createWiFiSender(wifiP2pConfig.deviceAddress);
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

    /**
     * Metoda koja pokreće metodu WifiP2pManagera za pokretanje pronalaska dostupnih uređaja. Registrira slušatelj obavijesti o događajima na pronalazak novih dostupnih uređaja.
     *
     * @param mBroadcastReceiver Slušatelj obavijesti o događajima koje okida namjera
     */
    @Override
    public void discover(final BroadcastReceiver mBroadcastReceiver) {
        checkWifiPermissions();
        wifiP2pManager.discoverPeers(wifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Tražim uređaje...", Toast.LENGTH_LONG).show();
                context.registerReceiver(mBroadcastReceiver, intentFilter);
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(context, "Greška prilikom traženja uređaja", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Metoda koja provjerava dozvolu za pristup aplikacije lokaciji uređaja. Ako pristup lokaciji nije dozvoljen, traži se od korisnika da ga dozvoli.
     */
    private void checkWifiPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = ContextCompat.checkSelfPermission(context, "Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += ContextCompat.checkSelfPermission(context,"Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0){
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        }
    }
}
