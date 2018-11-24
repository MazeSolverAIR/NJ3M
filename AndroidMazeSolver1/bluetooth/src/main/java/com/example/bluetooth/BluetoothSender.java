package com.example.bluetooth;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class BluetoothSender implements IRobotMessenger {

    private static BluetoothSender InstanceOfSender;

    protected static BluetoothSender createBluetoothSender()
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothSender();

        return InstanceOfSender;
    }

    private  BluetoothSender()
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
