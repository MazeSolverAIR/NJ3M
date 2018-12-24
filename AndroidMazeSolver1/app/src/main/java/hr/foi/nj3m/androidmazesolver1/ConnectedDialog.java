package hr.foi.nj3m.androidmazesolver1;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hr.foi.nj3m.androidmazesolver1.Threads.ClientThread;
import hr.foi.nj3m.androidmazesolver1.Threads.SendReceive;

import static java.lang.Thread.sleep;

public class ConnectedDialog extends AppCompatActivity {

    Button btnSendControl;

    BluetoothAdapter mBluetoothAdapter = null;
    SendReceive sendReceive;
    ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_dialog);

        final String deviceAddress = getIntent().getStringExtra(ListOfDevices.EXTRA_ADDRESS);

        btnSendControl = (Button) findViewById(R.id.btnSendControl);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Connect(deviceAddress);

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

                ListOfDevices.iRobotMessenger.sendCommand("RotateLeft", bluetoothSocket);

                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ListOfDevices.iRobotMessenger.sendCommand("RunMotors", bluetoothSocket);

                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ListOfDevices.iRobotMessenger.sendCommand("Over", bluetoothSocket);

                for (CommandsToMBot naredba:listaNaredbi)
                {
                    String stringNaredba = CommandsToMBotController.getStringFromComandEnum(naredba);
                    ListOfDevices.iRobotMessenger.sendCommand(stringNaredba, bluetoothSocket);
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
                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
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
        clientThread = new ClientThread(mBluetoothAdapter.getRemoteDevice(deviceAddress), handler);
        try {
            sendReceive = clientThread.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
