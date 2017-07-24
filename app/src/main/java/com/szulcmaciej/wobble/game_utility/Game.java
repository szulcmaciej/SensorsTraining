package com.szulcmaciej.wobble.game_utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.os.Vibrator;

import com.szulcmaciej.wobble.sensorstraining.R;

import java.util.Random;

/**
 * Created by Lenovo on 2017-05-22.
 */

public class Game {
    public static final int PLAYER_RADIUS_EASY = 22;
    public static final float ADDED_TIME_ON_HIT_EASY = 0.3f;
    public static final int TARGET_RADIUS_EASY = 40;
    public static final int TARGET_RANGE_EASY = 180;
    public static final int TOTAL_TIME_SECONDS_EASY = 10;
    public static final int PLAYER_RADIUS_MEDIUM = 12;
    public static final float ADDED_TIME_ON_HIT_MEDIUM = 0.3f;
    public static final int TARGET_RADIUS_MEDIUM = 30;
    public static final int TARGET_RANGE_MEDIUM = 210;
    public static final int TOTAL_TIME_SECONDS_MEDIUM = 10;
    public static final int PLAYER_RADIUS_HARD = 10;
    public static final float ADDED_TIME_ON_HIT_HARD = 0.3f;
    public static final int TARGET_RADIUS_HARD = 25;
    public static final int TARGET_RANGE_HARD = 250;
    public static final int TOTAL_TIME_SECONDS_HARD = 10;
    public static final int PLAYER_RADIUS_EXTREME = 8;
    public static final float ADDED_TIME_ON_HIT_EXTREME = 0.3f;
    public static final int TARGET_RADIUS_EXTREME = 15;
    public static final int TARGET_RANGE_EXTREME = 250;
    public static final int TOTAL_TIME_SECONDS_EXTREME = 10;

    private static final int POINTS_PER_HIT_DEFAULT = 10;
    private static final long VIBRATOR_TIME_MILIS = 20;

    private float totalTimeSeconds;
    private float addedTimeOnHit;
    private int targetRange;
    private int targetRadius;
    private int playerRadius;

    private int pointsPerHit = POINTS_PER_HIT_DEFAULT;

    public enum  Difficulty{
        EASY,
        MEDIUM,
        HARD,
        EXTREME;
    }


    private long timeRemainingMilis;
    private long previousTimeMilis;

    private int points;
    private long elapsedTimeMilis;
    private boolean gameOver;
    private boolean hasStarted;
    private boolean isPaused;
    private Difficulty difficulty;
    private boolean soundEnabled;
    private boolean vibrationEnabled;

    private MyPoint target;
    private MyPoint player;
    
    private static MediaPlayer hitSoundPlayer;
    private MediaPlayer backgroundMusicPlayer;
    private Vibrator vibrator;
    private Random random;
    private Context context;

    public Game(Context context) {
        this.context = context;
        random = new Random();
        target = new MyPoint();
        player = new MyPoint();
        backgroundMusicPlayer = MediaPlayer.create(context, R.raw.background_loop);
        hitSoundPlayer = MediaPlayer.create(context, R.raw.hit);
    }

    public void onStart(int difficulty, boolean soundEnabled, boolean vibrationEnabled){
        setDifficulty(difficulty);
        this.soundEnabled = soundEnabled;
        this.vibrationEnabled = vibrationEnabled;
        previousTimeMilis = SystemClock.uptimeMillis();
        timeRemainingMilis = (long) (1000 * totalTimeSeconds);
        setNewTarget();
        hasStarted = true;
        points = 0;
        elapsedTimeMilis = 0;
        gameOver = false;
        isPaused = false;

        if (soundEnabled) {
            //hitSoundPlayer = MediaPlayer.create(context, R.raw.hit);
            //hitSoundPlayer = MediaPlayer.create(context, R.raw.gunshot);

            if(backgroundMusicPlayer.isPlaying()){
                backgroundMusicPlayer.stop();
                backgroundMusicPlayer.release();
                backgroundMusicPlayer = MediaPlayer.create(context, R.raw.background_loop);
            }

            backgroundMusicPlayer.setLooping(true);
            backgroundMusicPlayer.start();
        }

        if(vibrationEnabled){
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }


    private void setDifficulty(int difficulty) {
        if(difficulty == Game.Difficulty.EASY.ordinal()){
            this.difficulty = Difficulty.EASY;
            setPlayerRadius(PLAYER_RADIUS_EASY);
            setAddedTimeOnHit(ADDED_TIME_ON_HIT_EASY);
            setTargetRadius(TARGET_RADIUS_EASY);
            setTargetRange(TARGET_RANGE_EASY);
            setTotalTimeSeconds(TOTAL_TIME_SECONDS_EASY);
        }
        if(difficulty == Game.Difficulty.MEDIUM.ordinal()){
            this.difficulty = Difficulty.MEDIUM;
            setPlayerRadius(PLAYER_RADIUS_MEDIUM);
            setAddedTimeOnHit(ADDED_TIME_ON_HIT_MEDIUM);
            setTargetRadius(TARGET_RADIUS_MEDIUM);
            setTargetRange(TARGET_RANGE_MEDIUM);
            setTotalTimeSeconds(TOTAL_TIME_SECONDS_MEDIUM);
        }
        if(difficulty == Game.Difficulty.HARD.ordinal()){
            this.difficulty = Difficulty.HARD;
            setPlayerRadius(PLAYER_RADIUS_HARD);
            setAddedTimeOnHit(ADDED_TIME_ON_HIT_HARD);
            setTargetRadius(TARGET_RADIUS_HARD);
            setTargetRange(TARGET_RANGE_HARD);
            setTotalTimeSeconds(TOTAL_TIME_SECONDS_HARD);
        }
        if(difficulty == Difficulty.EXTREME.ordinal()){
            this.difficulty = Difficulty.EXTREME;
            setPlayerRadius(PLAYER_RADIUS_EXTREME);
            setAddedTimeOnHit(ADDED_TIME_ON_HIT_EXTREME);
            setTargetRadius(TARGET_RADIUS_EXTREME);
            setTargetRange(TARGET_RANGE_EXTREME);
            setTotalTimeSeconds(TOTAL_TIME_SECONDS_EXTREME);
        }
    }

    //wywoływane przy onSensorChanged()

    public void update(int inputX, int inputY){
        if (!isPaused && !gameOver) {
            long deltaTime = SystemClock.uptimeMillis() - previousTimeMilis;
            previousTimeMilis = SystemClock.uptimeMillis();
            timeRemainingMilis -= deltaTime;
            elapsedTimeMilis += deltaTime;
            if(timeRemainingMilis <= 0){
                gameOver();
            }
            else {
                player.set(inputX, inputY);
                if(target.distance(player) < targetRadius + playerRadius){
                    onHit();
                }
            }
        }
    }

    private void setNewTarget(){
        int newX = random.nextInt(targetRange * 2) - targetRange;
        int newY = random.nextInt(targetRange * 2) - targetRange;
        target.set(newX, newY);
        //target.set(targetRange, targetRange);

        if(target.distance(player) < targetRadius + playerRadius){
            setNewTarget();
        }

    }

    private void gameOver(){
        gameOver = true;
        hasStarted = false;
    }

    private void onHit(){
        points += pointsPerHit;
        timeRemainingMilis += ((long) (addedTimeOnHit * 1000)) ;
        setNewTarget();
        if (soundEnabled) {
            playHitSound();
        }

        if (vibrationEnabled) {
            vibrator.vibrate(VIBRATOR_TIME_MILIS);
        }
    }

    public int getTargetRadius() {
        return targetRadius;
    }

    public long getTimeRemainingMilis() {
        return timeRemainingMilis;
    }

    public int getPoints() {
        return points;
    }

    public MyPoint getTarget() {
        return target;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void pause(){
        isPaused = true;
        if (soundEnabled) {
            backgroundMusicPlayer.stop();
        }
    }

    public void resume(){
        previousTimeMilis = SystemClock.uptimeMillis();
        isPaused = false;

        if (soundEnabled) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = MediaPlayer.create(context, R.raw.background_loop);
            backgroundMusicPlayer.setLooping(true);

            backgroundMusicPlayer.start();
        }
    }

    private void playHitSound(){
        if(hitSoundPlayer.isPlaying()){
            hitSoundPlayer.stop();
            hitSoundPlayer.release();
            hitSoundPlayer = MediaPlayer.create(context, R.raw.hit);
            //hitSoundPlayer = MediaPlayer.create(context, R.raw.gunshot);
        }
        hitSoundPlayer.start();
    }

    public float getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(float totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public float getAddedTimeOnHit() {
        return addedTimeOnHit;
    }

    public void setAddedTimeOnHit(float addedTimeOnHit) {
        this.addedTimeOnHit = addedTimeOnHit;
    }

    public int getPointsPerHit() {
        return pointsPerHit;
    }

    public void setPointsPerHit(int pointsPerHit) {
        this.pointsPerHit = pointsPerHit;
    }

    public int getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(int targetRange) {
        this.targetRange = targetRange;
    }

    public void setTargetRadius(int targetRadius) {
        this.targetRadius = targetRadius;
    }

    public int getPlayerRadius() {
        return playerRadius;
    }

    public MyPoint getPlayer() {
        return player;
    }

    public void setPlayerRadius(int playerRadius) {
        this.playerRadius = playerRadius;

    }

    public boolean isPaused() {
        return isPaused;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
