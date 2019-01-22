package hr.foi.nj3m.virtualmbot;

import hr.foi.nj3m.communications.IVirtualMessenger;
import hr.foi.nj3m.interfaces.IVirtualMBot;


public class VirtualMBot implements IVirtualMBot, IVirtualMessenger
{

    @Override
    public String receieveMsg(String msg) {
        return null;
    }

    @Override
    public boolean sendMsg(String msg) {
        return false;
    }

    @Override
    public void moveForward() {

    }

    @Override
    public void moveRight() {

    }

    @Override
    public void moveLeft() {

    }

    @Override
    public void moveBackwards() {

    }
}
