package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        setPaintStyles();
        canvas.drawCircle(centerX + targetX, centerY + targetY, targetRadius, targetPaint);
        canvas.drawLine(centerX, centerY, centerX + stopX, centerY + stopY, linePaint);
        canvas.drawCircle(centerX + stopX, centerY + stopY, playerRadius, playerPaint);
    }

    private int lineLength(){
        return (int) Math.sqrt( (stopX) * (stopX) + (stopY) * (stopY) );
    }

    private void setPaintStyles(){
        if(lineLength() < 100){
            linePaint.setStrokeWidth(5f);
        }
        else {
            linePaint.setStrokeWidth(lineLength() / 20);
        }
        targetPaint.setColor(Color.YELLOW);
        playerPaint.setColor(Color.RED);
    }

}
