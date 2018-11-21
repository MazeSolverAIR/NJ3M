package hr.foi.nj3m.core.interfaces;

import com.example.bluetooth.Bluetooth;

import hr.foi.nj3m.interfaces.IRobotMessenger;
import hr.foi.nj3m.wifi.WiFi;

public class MessageController {

    private static IRobotMessenger InstancaRobotMessenger = null;


    public static IRobotMessenger getInstanceOfMessenger()
    {
        return InstancaRobotMessenger;
    }

    public static IRobotMessenger createMessenger(String odabranNacinKomunikacije)
    {
            new MessageController(odabranNacinKomunikacije);

            return InstancaRobotMessenger;
    }

    private MessageController(String odabranNacinKomunikacije) {
        if(odabranNacinKomunikacije == "bluetooth")
        {
            InstancaRobotMessenger = new Bluetooth();
        }
        else if(odabranNacinKomunikacije == "wifi")
        {
            InstancaRobotMessenger = new WiFi();
        }
    }
}
