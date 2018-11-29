package hr.foi.nj3m.interfaces;

// Ovo sučelje bi trebalo implementirati u bluetooth klasu i u WiFi klasu

//ovaj modul treba koristiti i wiFi modul i bluetooth modul
public interface IRobotMessenger {

    boolean sendCommand(String naredba);

    byte[] receive();
}
