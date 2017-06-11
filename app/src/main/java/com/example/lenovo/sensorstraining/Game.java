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
    static final float DEFAULT_ADDED_TIME_ON_HIT = 0.5f;
    static final int DEFAULT_POINTS_PER_HIT = 10;
    static final int DEFAULT_TARGET_RANGE = 200;
    static final int DEFAULT_TARGET_RADIUS = 30;
    static final int DEFAULT_PLAYER_RADIUS = 12;

    private float totalTimeSeconds = DEFAULT_TOTAL_TIME_SECONDS;
    private float addedTimeOnHit = DEFAULT_ADDED_TIME_ON_HIT;
    private int pointsPerHit = DEFAULT_POINTS_PER_HIT;
    private int targetRange = DEFAULT_TARGET_RANGE;
    private int targetRadius = DEFAULT_TARGET_RADIUS;
    private int playerRadius = DEFAULT_PLAYER_RADIUS;

    public enum  Difficulty{
        EASY,
        MEDIUM,
        HARD,
        EXTREME
    }


    private long timeRemainingMilis;
    private long previousTimeMilis;

    private int points;
    private long elapsedTimeMilis;
    private boolean gameOver;
    private boolean hasStarted;
    private boolean isPaused;
    private Difficulty difficulty;

    private MyPoint target;
    private MyPoint player;
    
    private static MediaPlayer hitSoundPlayer;
    private MediaPlayer backgroundMusicPlayer;
    private Random random;
    private Context context;

    public Game(Context context) {
        this.context = context;
        random = new Random();
        target = new MyPoint();
        player = new MyPoint();
        hitSoundPlayer = MediaPlayer.create(context, R.raw.hit);
        //hitSoundPlayer = MediaPlayer.create(context, R.raw.gunshot);
        backgroundMusicPlayer = MediaPlayer.create(context, R.raw.background_loop);
        backgroundMusicPlayer.setLooping(true);
        backgroundMusicPlayer.start();
    }

    public void onStart(int difficulty){
        setDifficulty(difficulty);
        previousTimeMilis = SystemClock.uptimeMillis();
        timeRemainingMilis = (long) (1000 * totalTimeSeconds);
        setNewTarget();
        hasStarted = true;
        points = 0;
        elapsedTimeMilis = 0;
        gameOver = false;
        isPaused = false;
    }


    private void setDifficulty(int difficulty) {
        if(difficulty == Game.Difficulty.EASY.ordinal()){
            this.difficulty = Difficulty.EASY;
            setPlayerRadius(22);
            setAddedTimeOnHit(0.5f);
            setTargetRadius(40);
            setTargetRange(180);
            setTotalTimeSeconds(30);
        }
        if(difficulty == Game.Difficulty.MEDIUM.ordinal()){
            this.difficulty = Difficulty.MEDIUM;
            setPlayerRadius(12);
            setAddedTimeOnHit(0.5f);
            setTargetRadius(30);
            setTargetRange(210);
            setTotalTimeSeconds(30);
        }
        if(difficulty == Game.Difficulty.HARD.ordinal()){
            this.difficulty = Difficulty.HARD;
            setPlayerRadius(10);
            setAddedTimeOnHit(0.5f);
            setTargetRadius(25);
            setTargetRange(250);
            setTotalTimeSeconds(20);
        }
        if(difficulty == Difficulty.EXTREME.ordinal()){
            this.difficulty = Difficulty.EXTREME;
            setPlayerRadius(8);
            setAddedTimeOnHit(0.5f);
            setTargetRadius(15);
            setTargetRange(250);
            setTotalTimeSeconds(10);
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
        backgroundMusicPlayer.stop();
    }

    public void resume(){
        previousTimeMilis = SystemClock.uptimeMillis();
        isPaused = false;
/*
        if(backgroundMusicPlayer.isPlaying()){
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = MediaPlayer.create(context, R.raw.background_loop);
            backgroundMusicPlayer.setLooping(true);
        }
*/

        backgroundMusicPlayer.stop();
        backgroundMusicPlayer.release();
        backgroundMusicPlayer = MediaPlayer.create(context, R.raw.background_loop);
        backgroundMusicPlayer.setLooping(true);

        backgroundMusicPlayer.start();
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
