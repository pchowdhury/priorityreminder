package com.phoenix2k.priorityreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pushpan on 06/02/17.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                timerObj.cancel();
                proceedToSignIn();
                finish();
            }
        };
        timerObj.schedule(timerTaskObj, 1000, 1000);
    }

    private void proceedToSignIn() {
        startActivity(new Intent(this, SignInActivity.class));
    }


}
