package asa.org.bd.ammsma.splashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int SPLASH_TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginAmmsActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_TIME_OUT);

    }
}
