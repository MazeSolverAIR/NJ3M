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

import java.util.ArrayList;
import java.util.List;

import static hr.foi.nj3m.androidmazesolver1.ListOfDevicesFragment.EXTRA_ADDRESS;
import static hr.foi.nj3m.core.controllers.algorithms.MBotInfoProcesser.ProcessInfo;
import static java.lang.Thread.sleep;

import hr.foi.nj3m.androidmazesolver1.Threads.SendReceive;
import hr.foi.nj3m.core.controllers.algorithms.CommandsGenerator;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.checkACK.ACKChecker;
import hr.foi.nj3m.core.controllers.interfaceControllers.MSMessageFromACK;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public class ConnectedDialogFragment extends Fragment {

    Button btnSendControl;
    SendReceive sendReceive;
    String deviceAddress;
    SharedPreferences sharedPreferences;

    boolean start = true;

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

                MBotPathFinder pathFinder = MBotPathFinder.createInstance();
                sendReceive.write(pathFinder.TestMethod());
                /*if(start)
                {
                    sendReceive.write(CommandsGenerator.StartMBot());

                    start = false;
                }
                else
                {
                    sendReceive.write(CommandsGenerator.StopMBot());

                    start = true;
                }*/
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


    List<MSMessageFromACK> listOfRecvMessages = new ArrayList<>();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                byte[] readBuffer = (byte[]) msg.obj;
                String message = new String(readBuffer, 0, msg.arg1);

                if(message.startsWith("MBot:"))
                {
                    MSMessageFromACK messageAck = new MSMessageFromACK();
                    messageAck.setMessage(message);
                    listOfRecvMessages.add(messageAck);

                    Log.d("Primio", messageAck.returnFinalMessage());

                    if(messageAck.returnFinalMessage().equals("Over"))
                    {
                        if(ProcessInfo(listOfRecvMessages) == -2)
                            sendReceive.write(CommandsGenerator.SendMeAgain());

                        else if(ProcessInfo(listOfRecvMessages) == -1)
                            sendReceive.writeAgain();

                        listOfRecvMessages.clear();
                    }
                    else if(!ACKChecker.checkSum(messageAck))
                    {
                        sendReceive.write(CommandsGenerator.SendMeAgain());
                        listOfRecvMessages.clear();
                    }
                }
            }
            return true;
        }
    });
}
