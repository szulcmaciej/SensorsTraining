package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lenovo.sensorstraining.databinding.ActivityGameBinding;

import java.text.DecimalFormat;

public class GameActivity extends AppCompatActivity implements SensorEventListener{

    private ActivityGameBinding mBinding;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        //setContentView(R.layout.activity_game);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        game = new Game();

        mBinding.drawView.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float acceleration0 = event.values[0] / 9.81f;
        float acceleration1 = event.values[1] / 9.81f;
        //float acceleration2 = event.values[2] / 9.81f;

        int stopX = (int) (-acceleration0 * 200) +  mBinding.drawView.getWidth() / 2;
        int stopY = (int) (acceleration1 * 200) + mBinding.drawView.getHeight() / 2;

        gameLoop(stopX, stopY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void gameLoop(int sensorX, int sensorY){
        if(!game.isGameOver()){
            game.update(sensorX, sensorY);
            updateUI();
        }
        else {
            //TODO rób coś jak game over
            //unregister listener?
        }
    }

    private void updateUI(){
        //TODO
        mBinding.score.setText(R.string.score + game.getPoints());
        mBinding.timeRemaining.setText(new DecimalFormat("#.#").format(game.getTimeRemainingMilis() / 1000f));

    }
}
