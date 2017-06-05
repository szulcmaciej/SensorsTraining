package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.adapters.RadioGroupBindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.lenovo.sensorstraining.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    public enum  Difficulty{
        EASY,
        MEDIUM,
        HARD;
    }

    ActivitySettingsBinding mBinding;
    Game game;

    public static void start(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setContentView(R.layout.activity_settings);
        game = GameSingleton.getInstance(this);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.easyRadioButton:
                if (checked)
                    setDifficulty(Difficulty.EASY);
                    break;
            case R.id.mediumRadioButton:
                if (checked)
                    setDifficulty(Difficulty.MEDIUM);
                    break;
            case R.id.hardRadioButton:
                if (checked)
                    setDifficulty(Difficulty.HARD);
                    break;
        }
    }

    private void setDifficulty(Difficulty difficulty){
        if(difficulty == Difficulty.EASY){
            game.setPlayerRadius(22);
            game.setAddedTimeOnHit(1f);
            game.setTargetRadius(40);
            game.setTargetRange(150);
            game.setTotalTimeSeconds(40);
        }
        if(difficulty == Difficulty.MEDIUM){
            game.setPlayerRadius(12);
            game.setAddedTimeOnHit(0.5f);
            game.setTargetRadius(30);
            game.setTargetRange(200);
            game.setTotalTimeSeconds(30);
        }
        if(difficulty == Difficulty.HARD){
            game.setPlayerRadius(8);
            game.setAddedTimeOnHit(0.2f);
            game.setTargetRadius(18);
            game.setTargetRange(250);
            game.setTotalTimeSeconds(20);
        }
    }

}