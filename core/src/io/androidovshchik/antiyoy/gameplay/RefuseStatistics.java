package io.androidovshchik.antiyoy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class RefuseStatistics {
    public static final String PREFS = "antiyoy.refuse_stats";
    private static RefuseStatistics instance;
    public int acceptedEarlyGameEnd;
    public int refusedEarlyGameEnd;

    public static void initialize() {
        instance = null;
    }

    public static RefuseStatistics getInstance() {
        if (instance == null) {
            instance = new RefuseStatistics();
            instance.load();
        }
        return instance;
    }

    public void onEarlyGameEndAccept() {
        this.acceptedEarlyGameEnd++;
        save();
    }

    public void onEarlyGameEndRefuse() {
        this.refusedEarlyGameEnd++;
        save();
    }

    private void save() {
        Preferences preferences = getPreferences();
        preferences.putInteger("accept_early_game_end", this.acceptedEarlyGameEnd);
        preferences.putInteger("refuse_early_game_end", this.refusedEarlyGameEnd);
        preferences.flush();
    }

    private void load() {
        Preferences preferences = getPreferences();
        this.acceptedEarlyGameEnd = preferences.getInteger("accept_early_game_end");
        this.refusedEarlyGameEnd = preferences.getInteger("refuse_early_game_end");
    }

    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }
}
