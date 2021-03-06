package com.szulcmaciej.wobble.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.szulcmaciej.wobble.game_utility.Game;
import com.szulcmaciej.wobble.game_utility.GameSingleton;
import com.szulcmaciej.wobble.sensorstraining.R;
import com.szulcmaciej.wobble.sensorstraining.databinding.ActivityGameBinding;

import java.text.DecimalFormat;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements SensorEventListener{

    public static final String SHARED_PREFS_NAME = "game_data";
    public static final String SHARED_PREFS_DIFFICULTY = "difficulty";
    public static final String SHARED_PREFS_SOUND = "sound";
    private static final String SHARED_PREFS_VIBRATION = "vibration";

    private ActivityGameBinding mBinding;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Game game;
    private SharedPreferences sharedPrefs;


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
        sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        game = GameSingleton.getInstance(getApplicationContext());
        startGame();
        mBinding.drawView.setBackgroundColor(getResources().getColor(R.color.darkGray));
        resumeGame();
    }

    private void startGame() {
        int difficulty = sharedPrefs.getInt(SHARED_PREFS_DIFFICULTY, Game.Difficulty.EASY.ordinal());
        boolean soundEnabled = sharedPrefs.getBoolean(SHARED_PREFS_SOUND, true);
        boolean vibrationEnabled = sharedPrefs.getBoolean(SHARED_PREFS_VIBRATION, true);
        game.onStart(difficulty, soundEnabled, vibrationEnabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (!game.isPaused()) {
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
                startGame();
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
            sensorManager.unregisterListener(this);
            GameOverActivity.start(this, game.getPoints(), game.getDifficulty());
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
        mBinding.score.setText(String.format(Locale.getDefault(), "%s %d", getResources().getString(R.string.score), game.getPoints()));
        mBinding.timeRemaining.setText(new DecimalFormat("#0.0").format(game.getTimeRemainingMilis() / 1000f));
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
