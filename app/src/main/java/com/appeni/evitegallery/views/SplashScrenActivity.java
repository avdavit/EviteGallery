package com.appeni.evitegallery.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashScrenActivity extends Activity {
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
