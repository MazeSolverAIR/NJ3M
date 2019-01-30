package hr.foi.nj3m.androidmazesolver1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import hr.foi.nj3m.core.controllers.Factory;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.enumeratorControllers.CommandsToMBotController;
import hr.foi.nj3m.core.controllers.threads.SendReceive;

public class ConnectedDialogFragment extends Fragment {

    static boolean stopAll = true;
    Switch onOffSwitch;
    SendReceive sendReceive;
    boolean start = true;
    MBotPathFinder pathFinder;

    /**
     * Primatelj poruka koje mu prosljeđuje dretva za primanje poruka od robota. Ovdje se odlučuje što će se uraditi na osnovi poruka koje smo primili.
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 2)
                Toast.makeText(getContext(), msg.arg1, Toast.LENGTH_LONG).show();

            else if (msg.what == 1) {
                byte[] readBuffer = (byte[]) msg.obj;
                String message = new String(readBuffer, 0, msg.arg1);

                if (!message.startsWith("V") && !stopAll) {
                    String msgToSend = CommandsToMBotController.getStringFromComandEnum(pathFinder.FindPath(message));
                    sendReceive.write(msgToSend);
                }
                else if(!message.startsWith("V") && stopAll)
                    sendReceive.write("STP");
            }
            return true;
        }
    });

    /**
     * Metoda koja se poziva za instanciranje View UI - a.
     *
     * @param inflater           objekt koji se koristi za inflateanje bilo kojeg Viewa u fragment
     * @param container          View objekta koji je roditelj Viewa koji fragment instancira
     * @param savedInstanceState Ako nije null tada se fragment rekonstruira iz prošlog spremljenog stanja
     * @return vraća View za fragment View, inače null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connected_dialog, container, false);
    }

    /**
     * Metoda koja se poziva kad je fragment vidljiv korisniku. Obavezno se na početku
     * metode poziva bazna metoda.
     */
    @Override
    public void onStart() {
        super.onStart();

        onOffSwitch = (Switch) getView().findViewById(R.id.swStartStop);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connect();
            }
        }).start();

        onOffSwitch.setOnClickListener(v -> {
            if (onOffSwitch.isChecked())
            {
                if(pathFinder == null)
                    pathFinder = MBotPathFinder.createInstance(2);

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

    /**
     * Metoda čija je svrha kreiranje instance sendReceive klase i njezino pokretanje.
     */
    private void Connect() {
        sendReceive = Factory.createSendRecieve(handler);
        sendReceive.start();
    }
}
