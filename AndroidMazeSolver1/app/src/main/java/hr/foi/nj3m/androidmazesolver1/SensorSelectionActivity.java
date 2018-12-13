package hr.foi.nj3m.androidmazesolver1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SensorSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_selection);

        Button button = (Button) findViewById(R.id.btnNastavi);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox lijevo=(CheckBox) findViewById(R.id.checkboxLijevo);
                CheckBox desno=(CheckBox) findViewById(R.id.checkboxDesno);
                CheckBox ravno=(CheckBox) findViewById(R.id.checkboxRavno);
                boolean lijevoChecked=lijevo.isChecked();
                boolean desnoChecked=desno.isChecked();
                boolean ravnoChecked=ravno.isChecked();
                if (!lijevoChecked && !desnoChecked && ! ravnoChecked){
                    Toast.makeText(SensorSelectionActivity.this,"Nisu odabrani senzori!",Toast.LENGTH_LONG).show();
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
