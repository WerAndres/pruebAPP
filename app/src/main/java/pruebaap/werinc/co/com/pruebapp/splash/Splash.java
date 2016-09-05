package pruebaap.werinc.co.com.pruebapp.splash;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import pruebaap.werinc.co.com.pruebapp.R;
import pruebaap.werinc.co.com.pruebapp.principal.Categorias;

public class Splash extends Activity {

    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Intent mainIntent = new Intent().setClass(Splash.this, Categorias.class);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeCustomAnimation(Splash.this, R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                    ActivityCompat.startActivity(Splash.this, mainIntent, options.toBundle());
                }catch (Exception e){
                    System.err.println("E: "+e);
                    e.printStackTrace();
                }
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
