package com.example.lenovo.sensorstraining;

import android.graphics.Point;
import android.os.SystemClock;
import android.provider.Settings;

import java.util.Random;

/**
 * Created by Lenovo on 2017-05-22.
 */

public class Game {
    static final float TOTAL_TIME_SECONDS = 40;
    static final float ADDED_TIME_ON_HIT = 0.2f;
    static final int POINTS_PER_HIT = 10;
    static final int TARGET_RANGE = 200;
    static final int TARGET_RADIUS = 10;


    private long timeRemainingMilis;
    private long previousTimeMilis;

    private int points;
    boolean gameOver;
    boolean hasStarted;

    MyPoint target;

    Random random;

    public Game() {
        random = new Random();
        target = new MyPoint();
        timeRemainingMilis = (long) (1000 * TOTAL_TIME_SECONDS);
        points = 0;
        gameOver = false;
        hasStarted = false;
    }

    private void onStart(){
        previousTimeMilis = SystemClock.uptimeMillis();
        setNewTarget();
        hasStarted = true;
    }

    //wywoływane przy onSensorChanged()
    public void update(int inputX, int inputY){
        if(!hasStarted){
            onStart();
        }
        long deltaTime = SystemClock.uptimeMillis() - previousTimeMilis;
        previousTimeMilis = SystemClock.uptimeMillis();
        timeRemainingMilis -= deltaTime;
        if(timeRemainingMilis <= 0){
            gameOver();
        }
        else {
            if(target.distance(inputX, inputY) < TARGET_RADIUS){
                onHit();
            }
        }
    }

    private void setNewTarget(){
        int newX = random.nextInt(TARGET_RANGE * 2) - TARGET_RANGE;
        int newY = random.nextInt(TARGET_RANGE * 2) - TARGET_RANGE;
        target.set(newX, newY);

        if(target.distance(0, 0) < TARGET_RADIUS){
            setNewTarget();
        }
    }

    private void gameOver(){
        gameOver = true;
    }

    private void onHit(){
        points += POINTS_PER_HIT;
        timeRemainingMilis += ADDED_TIME_ON_HIT;
        setNewTarget();
    }

    public static int getTargetRadius() {
        return TARGET_RADIUS;
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
}
