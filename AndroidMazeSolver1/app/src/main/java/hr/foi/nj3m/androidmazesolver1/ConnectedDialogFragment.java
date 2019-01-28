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
import android.widget.Switch;
import android.widget.Toast;

import static hr.foi.nj3m.androidmazesolver1.ListOfDevicesFragment.EXTRA_ADDRESS;

import hr.foi.nj3m.core.controllers.algorithms.CommandsGenerator;
import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.core.controllers.threads.SendReceive;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.interfaces.IRobotConnector;
import hr.foi.nj3m.interfaces.communications.IMessenger;

public class ConnectedDialogFragment extends Fragment {

    Switch onOffSwitch;
    SendReceive sendReceive;
    String deviceAddress;
    SharedPreferences sharedPreferences;
    IRobotConnector iRobotConnector;

    boolean start = true;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_connected_dialog,container,false);
    }

    MBotPathFinder pathFinder;

    boolean stopAll = true;
    @Override
    public void onStart() {
        super.onStart();
        final Bundle bundle= this.getArguments();

        sharedPreferences = getContext().getSharedPreferences("MazeSolver1", Context.MODE_PRIVATE);
        onOffSwitch = (Switch) getView().findViewById(R.id.swStartStop);
        iRobotConnector = ConnectionController.getInstanceOfConnection();

        new Thread(new Runnable() {
            @Override
            public void run() {
                deviceAddress = (String) bundle.getSerializable(EXTRA_ADDRESS);
                Connect(deviceAddress);
            }
        }).start();

        onOffSwitch.setOnClickListener(v -> {
            if(onOffSwitch.isChecked())
            {
                stopAll = false;
                sendReceive.write("RUN");
            }

            else
            {
                stopAll = true;
                sendReceive.write("STP");
            }
        });
    }

    private void Connect(String deviceAddress){
        //iRobotConnector.initializeSocket(deviceAddress, handler);
        sendReceive = new SendReceive(handler);
        sendReceive.start();
    }


    static int brojac = 1;
    boolean okRecvd = false;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            /*if(pathFinder==null)
                pathFinder = MBotPathFinder.createInstance(2);*/

            if (msg.what == 2)
                Toast.makeText(getContext(), msg.arg1, Toast.LENGTH_LONG).show();

            else if (msg.what == 1){
                byte[] readBuffer = (byte[]) msg.obj;
                String message = new String(readBuffer, 0, msg.arg1);

                if(!message.startsWith("V") && !stopAll) {
                    Double frontSensorDist = 0.0;
                    try{
                        String msgWotking = message.substring(0, message.indexOf("b"));
                        frontSensorDist = Double.parseDouble(msgWotking);

                    }
                    catch (Exception ex)
                    {
                    }
                    Log.d("Primio", message);

                    if(message.contains("OK"))
                    {
                        sendReceive.write("SM");
                        okRecvd = true;
                    }
                    else if (frontSensorDist <= 22)
                    {
                        if(okRecvd)
                            brojac++;
                        if(brojac == 2)
                            sendReceive.write("FR");
                        else
                            sendReceive.write("SL");

                        okRecvd = false;
                    }
                    else if(frontSensorDist > 22)
                    {
                        sendReceive.write("RM");
                        brojac = 1;
                        okRecvd = false;
                    }



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
