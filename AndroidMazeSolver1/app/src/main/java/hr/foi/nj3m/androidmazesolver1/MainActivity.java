package hr.foi.nj3m.androidmazesolver1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton tipkaBluetooth = (ImageButton) findViewById(R.id.btnBluetooth);
        tipkaBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListOfDevices();
            }
        });

    }

    public void openListOfDevices(){
        Intent intent = new Intent(this,ListOfDevices.class);
        startActivity(intent);
    }
}
