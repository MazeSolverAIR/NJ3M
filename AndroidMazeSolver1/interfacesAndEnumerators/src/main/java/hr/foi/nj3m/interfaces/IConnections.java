package hr.foi.nj3m.interfaces;

public interface IConnections {

    boolean isAvailable();

    IRobotMessenger connect();
    boolean disconnect();
}
