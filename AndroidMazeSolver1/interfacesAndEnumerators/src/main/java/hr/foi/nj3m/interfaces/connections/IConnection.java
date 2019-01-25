package hr.foi.nj3m.interfaces.connections;

import hr.foi.nj3m.interfaces.communications.IMessenger;

public interface IConnection {
    IMessenger connect(int position);
}
