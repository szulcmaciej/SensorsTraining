package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.SystemClock;

import java.util.Random;

/**
 * Created by Lenovo on 2017-05-22.
 */

public class Game {
    static final float DEFAULT_TOTAL_TIME_SECONDS = 40;
    static final float DEFAULT_ADDED_TIME_ON_HIT = 0.2f;
    static final int DEFAULT_POINTS_PER_HIT = 10;
    static final int DEFAULT_TARGET_RANGE = 200;
    static final int DEFAULT_TARGET_RADIUS = 30;

    private float totalTimeSeconds = DEFAULT_TOTAL_TIME_SECONDS;
    private float addedTimeOnHit = DEFAULT_ADDED_TIME_ON_HIT;
    private int pointsPerHit = DEFAULT_POINTS_PER_HIT;
    private int targetRange = DEFAULT_TARGET_RANGE;
    private int targetRadius = DEFAULT_TARGET_RADIUS;



    private long timeRemainingMilis;
    private long previousTimeMilis;

    private int points;
    boolean gameOver;
    boolean hasStarted;
    boolean isPaused;

    MyPoint target;
    MyPoint player;
    private static MediaPlayer hitSoundPlayer;

    Random random;
    Context context;

    public Game(Context context) {
        this.context = context;
        random = new Random();
        target = new MyPoint();
        player = new MyPoint();
        hitSoundPlayer = MediaPlayer.create(context, R.raw.gunshot);
    }


    public void onStart(){
        previousTimeMilis = SystemClock.uptimeMillis();
        timeRemainingMilis = (long) (1000 * totalTimeSeconds);
        setNewTarget();
        hasStarted = true;
        points = 0;
        gameOver = false;
        isPaused = false;
    }

    //wywoływane przy onSensorChanged()
    public void update(int inputX, int inputY){
        /*if(!hasStarted){
            onStart();
        }*/
        if (!isPaused) {
            long deltaTime = SystemClock.uptimeMillis() - previousTimeMilis;
            previousTimeMilis = SystemClock.uptimeMillis();
            timeRemainingMilis -= deltaTime;
            if(timeRemainingMilis <= 0){
                gameOver();
            }
            else {
                player.set(inputX, inputY);
                if(target.distance(player) < targetRadius){
                    onHit();
                }
            }
        }
    }

    private void setNewTarget(){
        int newX = random.nextInt(targetRange * 2) - targetRange;
        int newY = random.nextInt(targetRange * 2) - targetRange;
        target.set(newX, newY);

        if(target.distance(0, 0) < targetRadius){
            setNewTarget();
        }
    }

    private void gameOver(){
        gameOver = true;
        hasStarted = false;
    }

    private void onHit(){
        points += pointsPerHit;
        timeRemainingMilis += addedTimeOnHit;
        setNewTarget();
        playHitSound();
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
    }

    public void resume(){
        previousTimeMilis = SystemClock.uptimeMillis();
        isPaused = false;
    }

    private void playHitSound(){
        if(hitSoundPlayer.isPlaying()){
            hitSoundPlayer.stop();
            hitSoundPlayer.release();
            hitSoundPlayer = MediaPlayer.create(context, R.raw.gunshot);
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
}
