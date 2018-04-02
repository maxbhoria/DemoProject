package com.example.mohitbhoria.demoproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    TextView userName,email,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userName= (TextView) findViewById(R.id.textUserName);
        email= (TextView) findViewById(R.id.textEmail);
        mobile= (TextView) findViewById(R.id.textPhone);
        SharedPreferences sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defa="Default";
        String getUserName=sharedpreferences.getString("user",defa);
        String getPhone=sharedpreferences.getString("mobile",defa);
        String getEmail=sharedpreferences.getString("email",defa);
        userName.setText(getUserName);
        mobile.setText(getPhone);
        email.setText(getEmail);
    }
}
