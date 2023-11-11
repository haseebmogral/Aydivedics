package com.orders.aydivedics;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.orders.aydivedics.ui.login.LoginActivity;


public class splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utility.activity = splash.this;

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences sp1= getSharedPreferences ("Login", MODE_PRIVATE);

                String unm=sp1.getString("username", null);
                if (unm != null){
                    Toast.makeText(splash.this, "welcome "+unm, Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(splash.this, userButtonActivity.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(splash.this, LoginActivity.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }



            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}
