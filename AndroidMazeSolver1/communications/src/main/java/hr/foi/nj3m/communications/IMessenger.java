package hr.foi.nj3m.communications;

public interface IMessenger {

    void sendCommand(String command);

    void receive();
}
