package com.example.lenovo.sensorstraining.game_utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Lenovo on 2017-04-12.
 */

public class DrawView extends View {



    Paint linePaint = new Paint();
    Paint playerPaint = new Paint();
    Paint targetPaint = new Paint();
    private int stopX;
    private int stopY;
    private int centerX;
    private int centerY;
    private int targetX;
    private int targetY;
    private int targetRadius;

    private int playerRadius;

    Context context;

    public void setStopX(int stopX) {
        this.stopX = stopX;
    }

    public void setStopY(int stopY) {
        this.stopY = stopY;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public void setTargetRadius(int targetRadius) {
        this.targetRadius = targetRadius;
    }

    public void setPlayerRadius(int playerRadius) {
        this.playerRadius = playerRadius;
    }

    public DrawView(Context context) {
        super(context);
        this.context = context;
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        setPaintStyles();
        changePixelsToDP();
        canvas.drawLine(centerX, centerY, centerX + stopX, centerY + stopY, linePaint);
        canvas.drawCircle(centerX + targetX, centerY + targetY, targetRadius, targetPaint);
        canvas.drawCircle(centerX + stopX, centerY + stopY, playerRadius, playerPaint);
    }

    private int lineLength(){
        return (int) Math.sqrt( (stopX) * (stopX) + (stopY) * (stopY) );
    }

    private void setPaintStyles(){
        float strokeWidth;
        if(lineLength() < 100){
            strokeWidth = convertPixelsToDp(5f, context) * 3;
        }
        else {

            strokeWidth = convertPixelsToDp(lineLength() / 20, context) * 3;
        }
        linePaint.setStrokeWidth(strokeWidth);
        targetPaint.setColor(Color.YELLOW);
        playerPaint.setColor(Color.RED);
    }

    private void changePixelsToDP() {
        stopX = (int) (convertPixelsToDp(stopX, context) * 3);
        stopY = (int) (convertPixelsToDp(stopY, context) * 3);
        targetX = (int) (convertPixelsToDp(targetX, context) * 3);
        targetY = (int) (convertPixelsToDp(targetY, context) * 3);
        targetRadius = (int) (convertPixelsToDp(targetRadius , context) * 3);
        playerRadius = (int) (convertPixelsToDp(playerRadius, context) * 3);
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
