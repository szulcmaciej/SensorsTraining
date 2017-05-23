package com.example.lenovo.sensorstraining;

import android.content.Context;

/**
 * Created by Lenovo on 2017-05-23.
 */

public class GameSingleton {
    private static Game game;

    public static Game getInstance(Context context){
        if (game == null) {
            game = new Game(context);
        }

        return game;
    }
}
