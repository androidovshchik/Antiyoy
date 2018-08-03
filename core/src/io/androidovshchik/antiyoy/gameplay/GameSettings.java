package io.androidovshchik.antiyoy.gameplay;

import yio.tro.antiyoy.YioGdxGame;

public class GameSettings {
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_HARD = 2;
    public static final int DIFFICULTY_NORMAL = 1;
    public static final int SPEED_FAST = 2;
    public static final int SPEED_NORMAL = 1;
    public static final int SPEED_SLOW = 0;
    private int difficulty;
    private int speed;
    private final YioGdxGame yioGdxGame;

    public GameSettings(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
    }

    public void switchSpeed() {
        this.speed++;
        if (this.speed > 2) {
            this.speed = 0;
        }
    }

    public void switchDifficulty() {
        this.difficulty++;
        if (this.difficulty > 2) {
            this.difficulty = 0;
        }
    }
}
