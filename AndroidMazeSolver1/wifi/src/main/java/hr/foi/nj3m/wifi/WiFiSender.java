package hr.foi.nj3m.wifi;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class WiFiSender implements IRobotMessenger {


    private static WiFiSender InstanceOfSender;

    protected static WiFiSender createWiFiSender()
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new WiFiSender();

        return InstanceOfSender;
    }

    private  WiFiSender()
    {

    }

    @Override
    public boolean runForward(int speed) {
        return false;
    }

    @Override
    public boolean runBackward(int speed) {
        return false;
    }

    @Override
    public boolean turnLeft(int degrees, int speed) {
        return false;
    }

    @Override
    public boolean turnRight(int degrees, int speed) {
        return false;
    }

    @Override
    public boolean stopMoving() {
        return false;
    }

    @Override
    public byte[] receive() {
        return new byte[0];
    }
}
