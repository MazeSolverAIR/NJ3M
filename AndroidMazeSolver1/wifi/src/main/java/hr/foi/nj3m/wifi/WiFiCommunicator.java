package hr.foi.nj3m.wifi;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class WiFiCommunicator implements IRobotMessenger {


    private static WiFiCommunicator InstanceOfSender;

    protected static WiFiCommunicator createWiFiSender()
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new WiFiCommunicator();

        return InstanceOfSender;
    }

    private WiFiCommunicator()
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
