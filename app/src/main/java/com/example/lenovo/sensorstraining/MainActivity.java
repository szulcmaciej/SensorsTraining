package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

//TODO czas nie może być ujemny, pauza na onPause?
//TODO w menu mają być 2 dodatkowe ikonki (jedna widoczna zawsze, druga opcjonalnie, na lewo od trzech kropek)
//TODO dodać dźwięki przy hitach

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    DrawView drawView;
    SensorManager sensorManager;
    Sensor accelerometer;
    LinearLayout linearLayout;
    private TextView axis0;
    private TextView axis1;
    private TextView axis2;
    private TextView currentGTV;
    private TextView maxGTV;
    private float maxG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        maxG = 0f;



        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        //setContentView(drawView);

        setContentView(R.layout.activity_main);
        findViewsAndSetListeners();
        linearLayout.addView(drawView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float acceleration0 = event.values[0] / 9.81f;
        float acceleration1 = event.values[1] / 9.81f;
        float acceleration2 = event.values[2] / 9.81f;

        int stopX = (int) (-acceleration0 * 200) + drawView.getWidth() / 2;
        int stopY = (int) (acceleration1 * 200) + drawView.getHeight() / 2;

        float currentG = (float) Math.sqrt( acceleration0*acceleration0 + acceleration1*acceleration1 + acceleration2*acceleration2 );
        if(currentG > maxG){
            maxG = currentG;
        }

        setTexts(acceleration0, acceleration1, acceleration2, currentG, maxG);
        draw(stopX, stopY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void draw(int stopX, int stopY){
        drawView.setStopX(stopX);
        drawView.setStopY(stopY);
        drawView.invalidate();
    }

    private void findViewsAndSetListeners(){
        axis0 = (TextView) findViewById(R.id.axis0);
        axis1 = (TextView) findViewById(R.id.axis1);
        axis2 = (TextView) findViewById(R.id.axis2);
        currentGTV = (TextView) findViewById(R.id.currentG);
        maxGTV = (TextView) findViewById(R.id.maxG);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutMain);
    }

    private void setTexts(float a0, float a1, float a2, float currentG, float maxG){
        axis0.setText("X: " + new DecimalFormat("#.##").format(a0));
        axis1.setText("Y: " + new DecimalFormat("#.##").format(a1));
        axis2.setText("Z: " + new DecimalFormat("#.##").format(a2));
        String currentGString = "Current G: " + (new DecimalFormat("#.##").format(currentG));
        String maxGString = "Max G: " + (new DecimalFormat("#.##").format(maxG));
        currentGTV.setText(currentGString);
        maxGTV.setText(maxGString);
    }
}
