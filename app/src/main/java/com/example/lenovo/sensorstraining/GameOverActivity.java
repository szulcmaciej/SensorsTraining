package com.example.lenovo.sensorstraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.lenovo.sensorstraining.databinding.ActivityGameOverBinding;

public class GameOverActivity extends AppCompatActivity {

    private int score;
    private Game.Difficulty difficulty;

    ActivityGameOverBinding mBinding;
    SharedPreferences sharedPrefs;
    Context context;

    public static void start(Context context, int score, Game.Difficulty difficulty){
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", difficulty);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_over);
        sharedPrefs = getSharedPreferences("game_data", Context.MODE_PRIVATE);
        context = this;
        setFields();
        setHighscoreLabelVisibleAndSaveHighscoreIfNewHighscore();
        setTexts();
        setButtonListeners();
    }

    private void setButtonListeners() {
        mBinding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.start(context);
            }
        });
    }

    private void setFields() {
        score = getIntent().getIntExtra("score", 0);
        difficulty = (Game.Difficulty) getIntent().getSerializableExtra("difficulty");
    }

    private void setHighscoreLabelVisibleAndSaveHighscoreIfNewHighscore() {
        if(isNewHighscore(score, difficulty)){
            String highscoreName = "highscore" + difficulty.name();
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt(highscoreName, score);
            editor.apply();

            mBinding.newHighscoreLabel.setVisibility(View.VISIBLE);
        }
    }

    private void setTexts() {
        String score = Integer.toString(getIntent().getIntExtra("score", 0));
        String highscoreName = "highscore" + difficulty.name();
        String highscore = Integer.toString(sharedPrefs.getInt(highscoreName, 0));
        mBinding.scoreTextView.setText(score);
        mBinding.highscoreTextView.setText(highscore);
    }

    private boolean isNewHighscore(int score, Game.Difficulty difficulty){
        String oldHighscoreName = "highscore" + difficulty.name();
        int oldHighscore = sharedPrefs.getInt(oldHighscoreName, 0);
        return score > oldHighscore;
    }


}
