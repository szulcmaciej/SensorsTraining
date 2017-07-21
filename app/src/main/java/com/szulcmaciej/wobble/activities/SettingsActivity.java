package com.szulcmaciej.wobble.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.szulcmaciej.wobble.game_utility.Game;
import com.szulcmaciej.wobble.sensorstraining.R;
import com.szulcmaciej.wobble.sensorstraining.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS_NAME = "game_data";
    public static final String SHARED_PREFS_DIFFICULTY = "difficulty";
    public static final String SHARED_PREFS_SOUND = "sound";
    ActivitySettingsBinding mBinding;
    //Game game;
    SharedPreferences sharedPrefs;

    public static void start(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        //game = GameSingleton.getInstance(this);
        sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        setControlsFromSharedPrefs();
        setListeners();
    }

    private void setListeners() {
        mBinding.difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId) {
                    case R.id.easyRadioButton:
                        saveDifficultyToSharedPrefs(Game.Difficulty.EASY);
                        break;
                    case R.id.mediumRadioButton:
                        saveDifficultyToSharedPrefs(Game.Difficulty.MEDIUM);
                        break;
                    case R.id.hardRadioButton:
                        saveDifficultyToSharedPrefs(Game.Difficulty.HARD);
                        break;
                    case R.id.extremeRadioButton:
                        saveDifficultyToSharedPrefs(Game.Difficulty.EXTREME);
                        break;
                }
            }
        });

        mBinding.soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSoundEnabledToSharedPrefs(isChecked);
                if (isChecked) {
                    mBinding.soundSwitch.setText(R.string.on);
                } else {
                    mBinding.soundSwitch.setText(R.string.off);
                }
            }
        });
    }
    private void setControlsFromSharedPrefs(){
        setRadioGroupFromSharedPrefs();
        setSoundSwitchFromSharedPrefs();
    }

    private void saveDifficultyToSharedPrefs(Game.Difficulty difficulty){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(SHARED_PREFS_DIFFICULTY, difficulty.ordinal());
        editor.apply();
    }
    private void saveSoundEnabledToSharedPrefs(boolean soundEnabled){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(SHARED_PREFS_SOUND, soundEnabled);
        editor.apply();
    }

    private void setRadioGroupFromSharedPrefs(){
        int selectedRadioButtonId = -1;
        int difficulty = sharedPrefs.getInt(SHARED_PREFS_DIFFICULTY, Game.Difficulty.EASY.ordinal());

        if(difficulty == Game.Difficulty.EASY.ordinal()){
            selectedRadioButtonId = R.id.easyRadioButton;
        }
        if(difficulty == Game.Difficulty.MEDIUM.ordinal()){
            selectedRadioButtonId = R.id.mediumRadioButton;
        }
        if(difficulty == Game.Difficulty.HARD.ordinal()){
            selectedRadioButtonId = R.id.hardRadioButton;
        }
        if(difficulty == Game.Difficulty.EXTREME.ordinal()){
            selectedRadioButtonId = R.id.extremeRadioButton;
        }

        mBinding.difficultyRadioGroup.check(selectedRadioButtonId);
    }
    private void setSoundSwitchFromSharedPrefs(){
        boolean soundEnabled = sharedPrefs.getBoolean(SHARED_PREFS_SOUND, true);
        mBinding.soundSwitch.setChecked(soundEnabled);
        if (soundEnabled) {
            mBinding.soundSwitch.setText(R.string.on);
        } else {
            mBinding.soundSwitch.setText(R.string.off);
        }
    }

}