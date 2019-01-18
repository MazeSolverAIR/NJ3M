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
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import static hr.foi.nj3m.androidmazesolver1.ListOfDevicesFragment.EXTRA_ADDRESS;
import static hr.foi.nj3m.core.controllers.algorithms.MBotInfoProcesser.ProcessInfo;

import hr.foi.nj3m.androidmazesolver1.Threads.SendReceive;
import hr.foi.nj3m.core.controllers.algorithms.CommandsGenerator;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.checkACK.ACKChecker;
import hr.foi.nj3m.core.controllers.components.LineFollower;
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

    MBotPathFinder pathFinder;

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
                if(pathFinder==null)
                    pathFinder = MBotPathFinder.createInstance(2);
                sendReceive.write(pathFinder.FindPath());
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
    long timeElapsedRecv = 0;
    long timeElapsedLoop = 0;

    boolean errorAtSum = false;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(pathFinder==null)
                pathFinder = MBotPathFinder.createInstance(2);

            if(msg.what == 1){
                byte[] readBuffer = (byte[]) msg.obj;
                String message = new String(readBuffer, 0, msg.arg1);

                boolean mbotMsg = false;

                Log.d("pmio", message);

                if(message.startsWith("0")) //1 ak je desni vani , 2 ak je lijevi, a 3 ak su obadva 0 kad su na crti
                {
                    LineFollower.right = true;
                    LineFollower.left = true;
                    mbotMsg = true;
                }
                if(message.startsWith("1"))
                {
                    LineFollower.right = false;
                    LineFollower.left = true;
                    mbotMsg = true;
                }
                if(message.startsWith("2"))
                {
                    LineFollower.right = true;
                    LineFollower.left = false;
                    mbotMsg = true;
                }
                if(message.startsWith("3"))
                {
                    LineFollower.right = false;
                    LineFollower.left = false;
                    mbotMsg = true;
                }

                if(mbotMsg)
                {
                    String substrFront = "";
                    try
                    {
                        substrFront = message.substring(message.lastIndexOf("c")+1, message.lastIndexOf("m"));
                    }
                    catch (Exception ex) {
                    }

                    Log.d("Primam", substrFront);

                    MBotPathFinder.FrontSensor.setCurrentValue(substrFront);
                    sendReceive.write(pathFinder.FindPath());
                }

                //timeElapsedLoop = SystemClock.elapsedRealtime();

                /*if(message.startsWith("MBot:"))
                {

                    Log.d("Primio",message);
                    MSMessageFromACK messageAck = new MSMessageFromACK();
                    messageAck.setMessage(message);
                    listOfRecvMessages.add(messageAck);

                    timeElapsedRecv = SystemClock.elapsedRealtime();*/

                    /*if(messageAck.returnFinalMessage().equals("Over") && !errorAtSum)
                    {
                        switch(ProcessInfo(listOfRecvMessages))
                        {
                            case SendMessagesAgain:
                                sendReceive.writeAgain();
                                break;
                            case DemandMessagesAgain:
                                sendReceive.write(CommandsGenerator.SendMeAgain());
                                break;
                            case OK:
                                sendReceive.write(pathFinder.FindPath());
                                break;
                        }
                        listOfRecvMessages.clear();
                    }
                    else if(!ACKChecker.checkSum(messageAck))
                        errorAtSum = true;
                }

                if(errorAtSum && Math.abs(timeElapsedLoop - timeElapsedRecv) > 120)
                {
                    sendReceive.write(CommandsGenerator.SendMeAgain());
                    errorAtSum = false;
                    listOfRecvMessages.clear();
                }*/
            }
            return true;
        }
    });
}
