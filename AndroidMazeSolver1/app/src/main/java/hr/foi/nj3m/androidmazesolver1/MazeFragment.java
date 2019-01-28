package hr.foi.nj3m.androidmazesolver1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.AlgoritamVirtualRobot;
import hr.foi.nj3m.core.controllers.interfaceControllers.ConnectionController;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.communications.IMessenger;
import hr.foi.nj3m.virtualmbot.VirtualMBot;
import hr.foi.nj3m.virtualmbot.VirtualMaze;


public class MazeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    AlgoritamVirtualRobot algoritamVirtualRobot;
    TextView txtMaze;
    static VirtualMsgContainer vContainer = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maze, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        final Button btnPocni = getView().findViewById(R.id.btnPocni);
        final Button btnRefresh = getView().findViewById(R.id.btnRefresh);
        txtMaze = getView().findViewById(R.id.txtMazer);

        algoritamVirtualRobot = new AlgoritamVirtualRobot();
        int[][] matrix = VirtualMaze.getMatrix();

        VirtualMsgContainer container = new VirtualMsgContainer();
        VirtualMBot virtualMBot = new VirtualMBot(container, 14, 6, Sides.Left);
        IMessenger messenger = ConnectionController.getiMessenger();
        AlgoritamVirtualRobot algoritamVirtualRobot = new AlgoritamVirtualRobot();

        btnPocni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!virtualMBot.exitFound)
                {
                    virtualMBot.sendSensorInfo();
                    messenger.receive(container);
                    String strToSend = algoritamVirtualRobot.FindPath(messenger.getRcvdMsg());

                    messenger.send(strToSend);

                    virtualMBot.receieveMsg();

                    virtualMBot.IzvrsiRadnju();

                    System.out.println(virtualMBot.recvdMessage);
                    printMatrix(matrix);
                }
            }
        });

    }

    public void printMatrix(int[][] matrix) {
        txtMaze.setText(null);
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 25; j++) {
                txtMaze.append(Integer.toString(matrix[i][j]));
            }
            txtMaze.append("\n");
        }
    }
}