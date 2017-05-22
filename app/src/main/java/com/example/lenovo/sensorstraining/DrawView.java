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

    public DrawView(Context context) {
        super(context);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(5f);
        targetPaint.setColor(Color.YELLOW);
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
        setLineWidth();
        canvas.drawLine(centerX, centerY, stopX, stopY, linePaint);
        canvas.drawCircle(targetX, targetY, targetRadius, targetPaint);
    }

    private int lineLength(){
        return (int) Math.sqrt( (stopX-centerX) * (stopX-centerX) + (stopY-centerY) * (stopY-centerY) );
    }

    private void setLineWidth(){
        if(lineLength() < 40){
            linePaint.setStrokeWidth(4f);
        }
        else {
            linePaint.setStrokeWidth(lineLength() / 10);
        }
    }
}
