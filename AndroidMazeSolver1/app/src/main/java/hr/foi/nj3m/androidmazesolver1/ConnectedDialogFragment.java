package hr.foi.nj3m.androidmazesolver1;

import android.content.Context;
import android.content.SharedPreferences;
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

    Button btnSendControl;
    SendReceive sendReceive;
    String deviceAddress;
    SharedPreferences sharedPreferences;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_connected_dialog,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Bundle bundle= this.getArguments();

        sharedPreferences = getContext().getSharedPreferences("MazeSolver1", Context.MODE_PRIVATE);
        btnSendControl = (Button) getView().findViewById(R.id.btnSendControl);

        new Thread(new Runnable() {
            @Override
            public void run() {
                deviceAddress = (String) bundle.getSerializable(EXTRA_ADDRESS);
                Connect(deviceAddress);
            }
        }).start();

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

                //Log.d("Poslana poruka: ", string);
                //Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Connect(String deviceAddress){
        switch (sharedPreferences.getString("TypeOfConnection", "DEFAULT")){
            case "bluetooth":
                sendReceive = new SendReceive(deviceAddress, handler);
                sendReceive.start();
                break;
            case "wifi":
                // TODO: 2.1.2019. Ako proradi WiFi modul na robotu, potrebno je ovo testirati. 
                sendReceive = new SendReceive("adresa", handler);
                sendReceive.start();
                break;
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
