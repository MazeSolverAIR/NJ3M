package hr.foi.nj3m.interfaces;

import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.interfaces.connections.IConnection;
import hr.foi.nj3m.interfaces.connections.IDevice;
import hr.foi.nj3m.interfaces.connections.IDiscover;
import hr.foi.nj3m.interfaces.connections.ISocket;
import hr.foi.nj3m.interfaces.connections.IWireless;

public interface IRobotConnector extends IConnection, IDevice, IDiscover, IWireless {
}
