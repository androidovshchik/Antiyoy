package io.androidovshchik.antiyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import io.androidovshchik.antiyoy.gameplay.name_generator.CityNameGenerator;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class CustomLanguageLoader {
    private static boolean autoDetect = false;
    private static String langName = null;
    public static final String prefs = "antiyoy.language";

    public static void loadLanguage() {
        Preferences preferences = Gdx.app.getPreferences(prefs);
        autoDetect = preferences.getBoolean("auto", true);
        if (!autoDetect) {
            langName = preferences.getString("lang_name");
            LanguagesManager.getInstance().setLanguage(langName);
        }
    }

    public static void setAndSaveLanguage(String langName) {
        Preferences preferences = Gdx.app.getPreferences(prefs);
        preferences.putBoolean("auto", false);
        preferences.putString("lang_name", langName);
        preferences.flush();
        Fonts.initFonts();
        CityNameGenerator.getInstance().load();
    }
}
