package hr.foi.nj3m.androidmazesolver1;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class loadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        //OtvaranjeGlavneForme

        new CountDownTimer(5000, 1000) {
            public void onFinish() {
                otvoriGlavniProzor();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();


    }

    public void otvoriGlavniProzor(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
