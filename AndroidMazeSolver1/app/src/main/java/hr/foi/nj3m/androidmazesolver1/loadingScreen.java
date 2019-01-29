package hr.foi.nj3m.androidmazesolver1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class loadingScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2500;

    Animation fromBottom;
    Animation fromTop;
    Animation fromLeft;
    Animation fromRight;
    ImageView logo;
    ImageView tekstMazeSolver;
    ImageView tekstMakeBlock;

    /**
     * Metoda koja se poziva prilikom pokretanja aktivnosti.
     *
     * @param savedInstanceState Ako je true, rekonstruira aktivnost
     *                           iz prethodno spremljenog stanja.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        logo = (ImageView) findViewById(R.id.logoNMJ);
        tekstMakeBlock = (ImageView) findViewById(R.id.txtMazeBlock);
        tekstMazeSolver = (ImageView) findViewById(R.id.txtMazeSolver);

        fromTop = AnimationUtils.loadAnimation(this, R.anim.from_top);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        fromLeft = AnimationUtils.loadAnimation(this, R.anim.enter_left_to_right);
        fromRight = AnimationUtils.loadAnimation(this, R.anim.enter_right_to_left);

        logo.setAnimation(fromTop);
        tekstMazeSolver.setAnimation(fromLeft);
        tekstMakeBlock.setAnimation(fromRight);

        //OtvaranjeGlavneForme
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(loadingScreen.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    public void otvoriGlavniProzor() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
