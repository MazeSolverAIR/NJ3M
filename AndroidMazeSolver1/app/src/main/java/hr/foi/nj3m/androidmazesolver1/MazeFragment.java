package hr.foi.nj3m.androidmazesolver1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.core.controllers.algorithms.AlgoritamVirtualRobot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.virtualmbot.VirtualMBot;
import hr.foi.nj3m.virtualmbot.VirtualMaze;
import hr.foi.nj3m.virtualwifi.VirtualWiFi;

import static java.lang.Thread.sleep;


public class MazeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    AlgoritamVirtualRobot algoritamVirtualRobot;
    TextView txtMaze;
    int trenutnoX = 14;
    int trenutnoY = 6;


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
        VirtualWiFi virtualWiFi = new VirtualWiFi(container);
        AlgoritamVirtualRobot algoritamVirtualRobot = new AlgoritamVirtualRobot();

        btnPocni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                virtualMBot.sendSensorInfo();
                virtualWiFi.receive();
                String strToSend = algoritamVirtualRobot.FindPath(virtualWiFi.recvdMessage);

                virtualWiFi.sendCommand(strToSend);

                virtualMBot.receieveMsg();

                virtualMBot.IzvrsiRadnju();

                System.out.println(virtualMBot.recvdMessage);
                printMatrix(matrix);
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