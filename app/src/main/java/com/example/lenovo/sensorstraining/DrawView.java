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
    Paint generalPaint = new Paint();
    Paint targetPaint = new Paint();
    private int stopX;
    private int stopY;
    private int centerX;
    private int centerY;
    private int targetX;
    private int targetY;
    private int targetRadius;

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

    public DrawView(Context context) {
        super(context);
        targetPaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(5f);
        targetPaint.setColor(Color.parseColor("#CD5C5C"));
        //targetPaint.setStyle(Paint.Style.FILL);
        //targetPaint.setColor(Color.YELLOW);
        targetPaint.setStrokeWidth(60f);
        /*centerX = getWidth() / 2;
        centerY = getHeight() / 2;*/
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
        //canvas.drawCircle(centerX + targetX, centerY + targetY, targetRadius, targetPaint);
        canvas.drawCircle(centerX + targetX, centerY + targetY, targetRadius, targetPaint);
        //canvas.drawCircle(centerX, centerY, 50, targetPaint);
        canvas.drawLine(centerX, centerY, centerX + stopX, centerY + stopY, linePaint);
        //canvas.drawLine(centerX + targetX, centerY + targetY, centerX + targetX + 100, centerY + targetY + 100, targetPaint);
    }

    private int lineLength(){
        return (int) Math.sqrt( (stopX) * (stopX) + (stopY) * (stopY) );
    }

    private void setPaintStyles(){
        if(lineLength() < 40){
            linePaint.setStrokeWidth(4f);
        }
        else {
            linePaint.setStrokeWidth(lineLength() / 10);
        }
        targetPaint.setColor(Color.YELLOW);
    }


}
