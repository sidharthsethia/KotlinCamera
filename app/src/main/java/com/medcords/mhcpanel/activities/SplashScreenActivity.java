package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.medcords.mhcpanel.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new PrefetchData().execute();
            }
        }, SPLASH_TIME_OUT);


    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
            if(sharedPreferences.getBoolean("userLoggedIn",false)){
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                SplashScreenActivity.this.overridePendingTransition(0, 0);
            }else {
                Intent i = new Intent(SplashScreenActivity.this, SplashActivity.class);
                startActivity(i);
            }
            // close this activity
            finish();
        }

    }
}
