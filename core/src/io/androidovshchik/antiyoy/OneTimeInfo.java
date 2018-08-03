package io.androidovshchik.antiyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class OneTimeInfo {
    public static final String PREFS_ONE_TIME = "antiyoy.one_time_info";
    private static OneTimeInfo instance = null;
    public boolean bleentoroRelease;

    public static void initialize() {
        instance = null;
    }

    public static OneTimeInfo getInstance() {
        if (instance == null) {
            instance = new OneTimeInfo();
            instance.load();
        }
        return instance;
    }

    void load() {
        this.bleentoroRelease = Gdx.app.getPreferences(PREFS_ONE_TIME).getBoolean("bleentoro_release", true);
    }

    public void save() {
        Preferences preferences = Gdx.app.getPreferences(PREFS_ONE_TIME);
        preferences.putBoolean("bleentoro_release", this.bleentoroRelease);
        preferences.flush();
    }
}
