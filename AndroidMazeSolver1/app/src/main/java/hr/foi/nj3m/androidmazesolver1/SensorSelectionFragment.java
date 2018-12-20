package hr.foi.nj3m.androidmazesolver1;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class SensorSelectionFragment extends Fragment {

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_sensor_selection,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button button = (Button) getView().findViewById(R.id.btnNastavi);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox lijevo=(CheckBox) getView().findViewById(R.id.checkboxLijevo);
                CheckBox desno=(CheckBox) getView().findViewById(R.id.checkboxDesno);
                CheckBox ravno=(CheckBox) getView().findViewById(R.id.checkboxRavno);
                boolean lijevoChecked=lijevo.isChecked();
                boolean desnoChecked=desno.isChecked();
                boolean ravnoChecked=ravno.isChecked();
                if (!lijevoChecked && !desnoChecked && ! ravnoChecked){
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Nešto")
                            .setMessage("Poruka")
                            .setNeutralButton("Ok",null)
                            .show();
                }
                if (lijevoChecked){
                    //ToDo algoritam za lijevi senzor
                }
                if (desno.isChecked()){
                    //ToDo algoritam za desni senzor
                }
                if (desnoChecked){
                    //ToDo algoritam za prednji senzor
                }
                if (lijevoChecked && desnoChecked){
                    //ToDo algoritam za lijevi i desni senzor
                }
                if (lijevoChecked && ravnoChecked){
                    //ToDo algoritam za lijevi i prednji senzor
                }
                if (desnoChecked && ravnoChecked){
                    //ToDo algoritam za desni i prednji senzor
                }
                if (lijevoChecked && desnoChecked && ravnoChecked){
                    //ToDo algoritam za sve senzore
                }
            }
        });
    }

}
