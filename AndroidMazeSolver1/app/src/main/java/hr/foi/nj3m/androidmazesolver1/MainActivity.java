package hr.foi.nj3m.androidmazesolver1;


import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import hr.foi.nj3m.interfaces.IWireless;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static BluetoothAdapter mBluetoothAdapter;
    public Button mButton;
    IWireless iWireless;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new ConnectionTypeSelectionFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();

    }
}
