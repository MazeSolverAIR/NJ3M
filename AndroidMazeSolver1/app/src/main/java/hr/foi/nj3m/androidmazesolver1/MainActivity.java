package hr.foi.nj3m.androidmazesolver1;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnEnableDisableBT = (Button) findViewById(R.id.btnEnableDisableBT);
        Button btnDiscover = (Button) findViewById(R.id.btnDiscover);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Bluetooth bluetooth = new Bluetooth(this,mBluetoothAdapter);

        btnEnableDisableBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.enableDisableBluetooth();
            }
        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.discover();
                TextView deviceName = (TextView) findViewById(R.id.tvDeviceName);
                TextView deviceAddress = (TextView) findViewById(R.id.tvDeviceAddress);
            }
        });
    }
}
