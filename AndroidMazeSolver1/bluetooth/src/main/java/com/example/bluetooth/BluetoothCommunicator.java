package com.example.bluetooth;

import hr.foi.nj3m.interfaces.IRobotMessenger;

public class BluetoothCommunicator implements IRobotMessenger {

    private static BluetoothCommunicator InstanceOfSender;

    protected static BluetoothCommunicator createBluetoothSender()
    {
        if(InstanceOfSender == null)
            InstanceOfSender = new BluetoothCommunicator();

        return InstanceOfSender;
    }

    private BluetoothCommunicator()
    {

    }

    @Override
    public boolean sendCommand(String command) {
        return false;
    }

    @Override
    public byte[] receive() {
        return new byte[0];
    }
}
