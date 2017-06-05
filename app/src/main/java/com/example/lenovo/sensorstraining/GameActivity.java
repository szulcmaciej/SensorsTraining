package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.lenovo.sensorstraining.databinding.ActivityGameBinding;

import java.text.DecimalFormat;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements SensorEventListener{

    private ActivityGameBinding mBinding;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Game game;


    public static void start(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        game = GameSingleton.getInstance(getApplicationContext());
        game.onStart();
        mBinding.drawView.setBackgroundColor(Color.GRAY);
        resumeGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        }
*/
        //game.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (!game.isPaused) {
            menuInflater.inflate(R.menu.game_menu_playing, menu);
        } else {
            menuInflater.inflate(R.menu.game_menu_paused, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.restart :
                game.onStart();
                resumeGame();
                return true;
            case R.id.pause :
                pauseGame();
                return true;
            case R.id.play :
                resumeGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float acceleration0 = event.values[0] / 9.81f;
        float acceleration1 = event.values[1] / 9.81f;
        //float acceleration2 = event.values[2] / 9.81f;

        int stopX = (int) (-acceleration0 * 200);
        int stopY = (int) (acceleration1 * 200);

        gameLoop(stopX, stopY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void gameLoop(int sensorX, int sensorY){
        if(!game.isGameOver()){
            game.update(sensorX, sensorY);
            //updateUI(sensorX, sensorY);
            updateUI(game.getPlayer().x, game.getPlayer().y);
        }
        else {
            //TODO rób coś jak game over
            sensorManager.unregisterListener(this);
        }
    }

    private void pauseGame(){
        game.pause();
        sensorManager.unregisterListener(this);
        invalidateOptionsMenu();
    }

    private void resumeGame(){
        game.resume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        }
        invalidateOptionsMenu();
    }

    private void updateUI(int sensorX, int sensorY){
        mBinding.score.setText(String.format(Locale.getDefault(), "%s%d", getResources().getString(R.string.score), game.getPoints()));
        mBinding.timeRemaining.setText(new DecimalFormat("#.#").format(game.getTimeRemainingMilis() / 1000f));
        draw(sensorX, sensorY);
    }

    private void draw(int sensorX, int sensorY){
        mBinding.drawView.setStopX(sensorX);
        mBinding.drawView.setStopY(sensorY);
        mBinding.drawView.setTargetX(game.getTarget().x);
        mBinding.drawView.setTargetY(game.getTarget().y);
        mBinding.drawView.setTargetRadius(game.getTargetRadius());
        mBinding.drawView.setPlayerRadius(game.getPlayerRadius());
        mBinding.drawView.invalidate();
    }
}
