package hr.foi.nj3m.core.interfaces;

// Ovo sučelje bi trebalo implementirati u bluetooth klasu i u WiFi klasu

//ovaj modul treba koristiti i wiFi modul i bluetooth modul
public interface IControlSender {

    boolean runForward(int speed);
    boolean runBackward(int speed);

    boolean turnLeft(int degrees, int speed);
    boolean turnRight(int degrees, int speed);

    boolean stopMoving();
}
