package com.szulcmaciej.wobble.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szulcmaciej.wobble.sensorstraining.R;

public class HowToPlayActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent intent = new Intent(context, HowToPlayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
    }
}
