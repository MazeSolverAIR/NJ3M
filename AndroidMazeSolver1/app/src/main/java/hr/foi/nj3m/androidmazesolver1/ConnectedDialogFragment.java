package hr.foi.nj3m.androidmazesolver1;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static hr.foi.nj3m.androidmazesolver1.ListOfDevicesFragment.EXTRA_ADDRESS;
import static java.lang.Thread.sleep;

import hr.foi.nj3m.androidmazesolver1.Threads.SendReceive;

import static java.lang.Thread.sleep;

public class ConnectedDialogFragment extends Fragment {
    BluetoothAdapter mBluetoothAdapter = null;
    Button btnSendControl;
    SendReceive sendReceive;
    String deviceAddress;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_connected_dialog,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Bundle bundle= this.getArguments();

        //final String deviceAddress = getActivity().getIntent().getStringExtra(ListOfDevicesFragment.EXTRA_ADDRESS);

        btnSendControl = (Button) getView().findViewById(R.id.btnSendControl);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                deviceAddress = (String) bundle.getSerializable(EXTRA_ADDRESS);
                Connect(deviceAddress);
            }
        }).start();
        //Connect(deviceAddress);

        // NE KORISTIMO VEĆ OVU TIPKU
        /*btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerThread serverThread = new ServerThread(handler, getApplicationContext());
                try {
                    sendReceive = serverThread.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        btnSendControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                MBotPathFinder finder = MBotPathFinder.createInstance();

                List<CommandsToMBot> listaNaredbi = finder.TestMethod();

                ListOfDevicesFragment.iRobotMessenger.sendCommand("RotateLeft", bluetoothSocket);

                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ListOfDevicesFragment.iRobotMessenger.sendCommand("RunMotors", bluetoothSocket);

                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ListOfDevicesFragment.iRobotMessenger.sendCommand("Over", bluetoothSocket);

                for (CommandsToMBot naredba:listaNaredbi)
                {
                    String stringNaredba = CommandsToMBotController.getStringFromComandEnum(naredba);
                    ListOfDevicesFragment.iRobotMessenger.sendCommand(stringNaredba, bluetoothSocket);
                    Log.d("Saljem", stringNaredba);
                    try {
                        sleep(50);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

                String string = "RotateLeft";
                sendReceive.write("RotateLeft");
                try {
                    sleep(35);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendReceive.write("RunMotors");
                try {
                    sleep(35);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendReceive.write("Over");

                Log.d("Poslana poruka: ", string);
                Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_LONG).show();
            }
        });

        // NI OVU TIPKU VEĆ NE KORISTIMO
        /*btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientThread = new ClientThread(mBluetoothAdapter.getRemoteDevice(deviceAddress), handler);
                try {
                    sendReceive = clientThread.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    private void Connect(String deviceAddress){
        sendReceive = new SendReceive(mBluetoothAdapter.getRemoteDevice(deviceAddress), handler);
        try {
            sendReceive.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendReceive.start();

        // TODO: 24.12.2018. Ovo je instanciranje klase ClientThread koju vjerojatno više nećemo koristiti; provjeriti!
        /*clientThread = new ClientThread(mBluetoothAdapter.getRemoteDevice(deviceAddress), handler);
        try {
            sendReceive = clientThread.call();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                byte[] readBuffer = (byte[]) msg.obj;
                String message = new String(readBuffer, 0, msg.arg1);
                String workingMessage = "";
                try {
                    workingMessage = message.substring(0, message.lastIndexOf(';'));
                }catch (Exception e){

                }
                Log.d("Primio sam", message);
                if(workingMessage.contains("KreceWrite")) {
                    Log.d("Tocna poruka", workingMessage);
                    try {

                        sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //sendReceive.write("RotateLeft");
                }
            }
            return true;
        }
    });
}
