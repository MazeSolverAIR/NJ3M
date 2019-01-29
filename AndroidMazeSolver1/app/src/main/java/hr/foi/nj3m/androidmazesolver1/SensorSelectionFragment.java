package hr.foi.nj3m.androidmazesolver1;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import hr.foi.nj3m.core.controllers.Factory;
import hr.foi.nj3m.core.controllers.components.UltrasonicSensor;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.sensors.IUltraSonic;

public class SensorSelectionFragment extends Fragment {

    private IUltraSonic mLeft;
    private IUltraSonic mRight;
    private IUltraSonic mFront;
    private List<IUltraSonic> mListOfSensors = new ArrayList<>();
    private Button mButton;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor_selection, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mButton = getView().findViewById(R.id.btnNastavi);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox left = (CheckBox) getView().findViewById(R.id.checkboxLijevo);
                CheckBox right = (CheckBox) getView().findViewById(R.id.checkboxDesno);
                CheckBox front = (CheckBox) getView().findViewById(R.id.checkboxRavno);
                boolean leftChecked = left.isChecked();
                boolean rightChecked = right.isChecked();
                boolean frontChecked = front.isChecked();
                if (!leftChecked && !rightChecked && !frontChecked) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Označeni senzori!")
                            .setMessage("Niste odabrali senzore!")
                            .setNeutralButton("Ok", null)
                            .show();
                }
                if (leftChecked) {
                    mLeft = Factory.CreateUltrasonicSensor(Sides.Left);
                    mListOfSensors.add(mLeft);
                }
                if (rightChecked) {
                    mRight = Factory.CreateUltrasonicSensor(Sides.Right);
                    mListOfSensors.add(mRight);
                }
                if (frontChecked) {
                    mFront = Factory.CreateUltrasonicSensor(Sides.Front);
                    mListOfSensors.add(mFront);
                }
            }
        });
    }
}
