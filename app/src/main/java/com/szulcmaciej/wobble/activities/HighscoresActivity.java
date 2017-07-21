package com.szulcmaciej.wobble.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szulcmaciej.wobble.game_utility.Game;
import com.szulcmaciej.wobble.sensorstraining.R;
import com.szulcmaciej.wobble.sensorstraining.databinding.ActivityHighscoresBinding;

public class HighscoresActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_NAME = "game_data";
    ActivityHighscoresBinding mBinding;
    SharedPreferences sharedPrefs;

    public static void start(Context context){
        Intent intent = new Intent(context, HighscoresActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_highscores);
        sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        setTexts();
    }

    private void setTexts(){
        String[] highscorePrefsNames = {
                "highscore" + Game.Difficulty.EASY.name(),
                "highscore" + Game.Difficulty.MEDIUM.name(),
                "highscore" + Game.Difficulty.HARD.name(),
                "highscore" + Game.Difficulty.EXTREME.name()};

        int easyHighscore = sharedPrefs.getInt(highscorePrefsNames[0], 0);
        int mediumHighscore = sharedPrefs.getInt(highscorePrefsNames[1], 0);
        int hardHighscore = sharedPrefs.getInt(highscorePrefsNames[2], 0);
        int extremeHighscore = sharedPrefs.getInt(highscorePrefsNames[3], 0);

        String easyHighscoreText = Integer.toString(easyHighscore);
        String mediumHighscoreText = Integer.toString(mediumHighscore);
        String hardHighscoreText = Integer.toString(hardHighscore);
        String extremeHighscoreText = Integer.toString(extremeHighscore);

        mBinding.easyValueTextView.setText(easyHighscoreText);
        mBinding.mediumValueTextView.setText(mediumHighscoreText);
        mBinding.hardValueTextView.setText(hardHighscoreText);
        mBinding.extremeValueTextView.setText(extremeHighscoreText);
    }
}
