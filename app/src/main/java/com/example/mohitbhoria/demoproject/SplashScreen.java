package com.example.mohitbhoria.demoproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Thread()
        {
            @Override
            public void  run()
            {
                try
                {
                    Thread.sleep(3000);
                }
                catch (Exception e)
                {}
                Intent i=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }.start();

    }

}
