package hr.foi.nj3m.communications;

import java.util.ArrayList;

public interface IConnections {

    boolean isAvailable();
    void addDevices(ArrayList devices);
    void clearList();
    ArrayList<String> getDeviceArray();
    String getDeviceAddress(int position);
    String getDeviceName(int position);
    boolean deviceExists(String deviceName);
    IRobotMessenger connect(int position);
    boolean disconnect();
}
