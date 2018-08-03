package io.androidovshchik.antiyoy.gameplay.user_levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Iterator;

public class UserLevelProgressManager {
    private static String ULP_PREFS = "antiyoy.user_levels_progress";
    private static UserLevelProgressManager instance = null;
    ArrayList<String> keys = new ArrayList();

    public static void initialize() {
        instance = null;
    }

    public static UserLevelProgressManager getInstance() {
        if (instance == null) {
            instance = new UserLevelProgressManager();
        }
        return instance;
    }

    public UserLevelProgressManager() {
        load();
    }

    private void load() {
        String[] split = Gdx.app.getPreferences(ULP_PREFS).getString("keys").split(" ");
        for (Object add : split) {
            this.keys.add((String) add);
        }
    }

    public boolean isLevelCompleted(String key) {
        return containsKey(key);
    }

    public ArrayList<String> getKeys() {
        return this.keys;
    }

    public void onLevelCompleted(String key) {
        if (!containsKey(key)) {
            this.keys.add(key);
            save();
        }
    }

    public int getNumberOfCompletedLevels() {
        return this.keys.size();
    }

    boolean containsKey(String key) {
        Iterator it = this.keys.iterator();
        while (it.hasNext()) {
            String s = (String) it.next();
            if (s != null && s.equals(key)) {
                return true;
            }
        }
        return false;
    }

    private void save() {
        Preferences preferences = Gdx.app.getPreferences(ULP_PREFS);
        StringBuilder builder = new StringBuilder();
        Iterator it = this.keys.iterator();
        while (it.hasNext()) {
            builder.append((String) it.next()).append(" ");
        }
        preferences.putString("keys", builder.toString());
        preferences.flush();
    }
}
