package com.example.kgreen.wallpaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Intent myIntent = new Intent(Home.this, WallpaperScreen.class);
                        Home.this.startActivity(myIntent);

                    }
                },
                3000
        );


    }
    @Override
    public void onBackPressed() {
    }


}

