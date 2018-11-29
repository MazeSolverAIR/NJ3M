package com.example.bluetooth;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
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
    public boolean sendCommand(String naredba) {
        return false;
    }

    @Override
    public byte[] receive() {
        return new byte[0];
    }
}
