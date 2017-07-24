package com.szulcmaciej.wobble.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.szulcmaciej.wobble.sensorstraining.R;
import com.szulcmaciej.wobble.sensorstraining.databinding.ActivityMainBinding;


//TODO (?) share button w GameOverActivity
//TODO nowe logo (?)

//DONE about activity
//DONE (not really) zmienić styl (np pozostały czas i punkty jakąś fajną czcionką?, pomyśleć nad kolorami)
//DONE porządek w layoutach (stringi, style)
//DONE dodać poziom trudności extreme i hard zrobić łatwiejszy
//DONE w menu mają być 2 dodatkowe ikonki (jedna widoczna zawsze, druga opcjonalnie, na lewo od trzech kropek)
//DONE jakiś popup albo aktywność Game Over + dźwięk
//DONE dodać highscores (w sharedprefsach 1 highscore dla każdego poziomu trudności))
//DONE highscores activity
//DONE game over activity + bundle z wynikiem
//DONE wartości trudności do stałych
//DONE dźwięki! dużo dźwięków! jeszcze więcej dźwięków!
//DONE dźwięki przy game over, new highscore, jakaś zapętlona muzyczka w tle, dźwięk przy początku gry
//DONE dodać logo apki
//DONE ikonka i nazwa
//NOTTODO może zmienić metodę setNewTarget() na efektywniejszą?
//NOTTODO jakiś efekt (animacja?) przy hicie/pojawianiu się nowego celu
//DONE menu ma mieć link do ustawień, stronę o autorze, highscores?
//DONE aktywność z ustawieniami
//DONE w GameActivity dodać menu z przyciskami : pauza lub play, restart
//DONE czas nie może być ujemny, pauza na onPause?
//DONE dodać dźwięki przy hitach
//DONE przerobić czarną krechę na cieńszą, ale z czymś na końcu (czerwonym kółkiem?)
//DONE ekran ma się nie wygaszać w czasie gry
//DONE naprawić restart, bo nie działa po skończeniu czasu (może jakoś połączyć z game over?)

//DONE przenieść ustawianie trudności na przycisk albo do main activity
//DONE how to play (przycisk w main activity + osobna aktywność?)
//DONE dodać do ustawień wyłączanie dźwięku
//DONE do wszytkich aktywności dodać back button w action bar
//DONE muzyka nie przestaje grać przy restarcie

//TODO balansowanie rozgrywki (0.3s za hit?)
//DONE zrobić żeby czas zawsze wyświetlał się z 1 miejscem dziesiętnym

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setButtonListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about :
                AboutActivity.start(context);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setButtonListeners() {
        mBinding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.start(context);
            }
        });

        mBinding.highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighscoresActivity.start(context);
            }
        });

        mBinding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.start(context);
            }
        });

        mBinding.howToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HowToPlayActivity.start(context);
            }
        });
    }

}