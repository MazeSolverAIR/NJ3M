package hr.foi.nj3m.androidmazesolver1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.foi.nj3m.core.controllers.algorithms.AlgoritamVirtualRobot;


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

        txtMaze = getView().findViewById(R.id.txtMazer);

        algoritamVirtualRobot = new AlgoritamVirtualRobot();

        //int[][] matrix= algoritamVirtualRobot.getMatrix();

        //matrix[6][14] = 2;


        /*algoritamVirtualRobot = new AlgoritamVirtualRobot();
        int[][] matrix;
        matrix = algoritamVirtualRobot.getMatrix();
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                txtMaze.setText(Integer.toString(matrix[i][j]));
            }
            txtMaze.setText("\n");
        }*/
    }

    public void printMatrix(int[][] matrix){ //TODO: Postaviti negdje drugdje
        txtMaze.setText(null);
        for(int i = 0; i < 13; i++) {
            for (int j = 0; j < 25; j++) {
                txtMaze.append(Integer.toString(matrix[i][j]));
            }
            txtMaze.append("\n");
        }
    }






}