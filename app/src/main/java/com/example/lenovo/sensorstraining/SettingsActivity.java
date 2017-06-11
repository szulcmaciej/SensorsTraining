package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.adapters.RadioGroupBindingAdapter;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lenovo.sensorstraining.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding mBinding;
    Game game;
    SharedPreferences sharedPrefs;

    public static void start(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        game = GameSingleton.getInstance(this);
        sharedPrefs = getSharedPreferences("game_data", Context.MODE_PRIVATE);
        setRadioGroupFromSharedPrefs();
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
    }

    private void saveDifficultyToSharedPrefs(Game.Difficulty difficulty){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("difficulty", difficulty.ordinal());
        editor.apply();
    }

    private void setRadioGroupFromSharedPrefs(){
        int selectedRadioButtonId = -1;
        int difficulty = sharedPrefs.getInt("difficulty", Game.Difficulty.EASY.ordinal());

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

}