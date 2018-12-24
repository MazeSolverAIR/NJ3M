package hr.foi.nj3m.androidmazesolver1;


import android.bluetooth.BluetoothAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import android.widget.ImageButton;
import android.widget.Toast;
import static android.os.Environment.DIRECTORY_DCIM;
import java.io.File;

import hr.foi.nj3m.core.controllers.interfaceControllers.WirelessController;
import hr.foi.nj3m.interfaces.IWireless;

public class MainActivity extends AppCompatActivity {

    public  Button mButton;

    private static final String TAG = "MainActivity";
    public static BluetoothAdapter mBluetoothAdapter;
    IWireless iWireless;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final hr.foi.nj3m.androidmazesolver1.Bluetooth bluetooth = new hr.foi.nj3m.androidmazesolver1.Bluetooth(this, mBluetoothAdapter, mBroadcastReceiver);

        final ImageButton tipkaBluetooth = (ImageButton) findViewById(R.id.btnBluetooth);

        iWireless = WirelessController.createInstance(this);

        tipkaBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bluetooth.enableDisableBluetooth();
                if(mBluetoothAdapter.isEnabled()){
                    openListOfDevices();
                }
                else
                    iWireless.enableDisable(mBroadcastReceiver);
            }
        });

        File mFileTemp = new File(getExternalFilesDir(DIRECTORY_DCIM)+File.separator+"MazeSolverPictures","Maze.png");
        mFileTemp.getParentFile().mkdirs();
        galleryAddPic();

        mButton=findViewById(R.id.button2);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment fragment= new SensorSelectionFragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    public void openListOfDevices(){
        Fragment fragment= new ListOfDevices();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "Bluetooth isključen", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context, "Bluetooth uključen", Toast.LENGTH_LONG).show();
                        openListOfDevices();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(getExternalFilesDir(DIRECTORY_DCIM)+File.separator+"MazeSolverPictures");
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
