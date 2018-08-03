package io.androidovshchik.antiyoy.menu.save_slot_selector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.GameSaver;
import io.androidovshchik.antiyoy.gameplay.editor.LevelEditor;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.Yio;

public class SaveSystem {
    public static final String AUTOSAVE_KEY = "autosave";
    public static final String EDITOR_SLOT_PREFS = "antiyoy.editor_slot_prefs";
    public static final String SAVE_SLOT_PREFS = "antiyoy.slot_prefs";
    GameController gameController;
    public GameSaver gameSaver;

    public SaveSystem(GameController gameController) {
        this.gameController = gameController;
        this.gameSaver = new GameSaver(gameController);
        checkToImportOldSaves();
    }

    private void checkToImportOldSaves() {
        Preferences preferences = getPreferences(SAVE_SLOT_PREFS);
        if (!preferences.getBoolean("imported_old_saves", false)) {
            preferences.putBoolean("imported_old_saves", true);
            preferences.flush();
            performImportOldSaves();
        }
    }

    private void performImportOldSaves() {
        tryToImportOldSave("save_slot4");
        tryToImportOldSave("save_slot3");
        tryToImportOldSave("save_slot2");
        tryToImportOldSave("save_slot1");
        tryToImportOldSave("save_slot0");
        tryToImportOldSave("save");
    }

    private void tryToImportOldSave(String key) {
        Preferences preferences = getPreferences(key);
        if (preferences.getString("save_active_hexes", "").length() >= 3) {
            addKey(key, SAVE_SLOT_PREFS);
            SaveSlotInfo saveSlotInfo = new SaveSlotInfo();
            saveSlotInfo.name = getNameString(preferences);
            if (saveSlotInfo.name.length() < 3) {
                saveSlotInfo.name = LevelEditor.SLOT_NAME;
            }
            saveSlotInfo.description = getDescriptionString(preferences);
            if (saveSlotInfo.description.length() < 3) {
                saveSlotInfo.description = "old save";
            }
            saveSlotInfo.key = key;
            editSlot(key, saveSlotInfo, SAVE_SLOT_PREFS);
        }
    }

    public static String getNameString(Preferences slotPrefs) {
        String multiMode = "";
        if (slotPrefs.getInteger("save_player_number") > 1) {
            multiMode = " [" + slotPrefs.getInteger("save_player_number") + "x]";
        }
        if (slotPrefs.getBoolean("save_campaign_mode")) {
            return LanguagesManager.getInstance().getString("choose_game_mode_campaign") + " " + slotPrefs.getInteger("save_current_level");
        }
        return LanguagesManager.getInstance().getString("choose_game_mode_skirmish") + multiMode;
    }

    public static String getDescriptionString(Preferences slotPrefs) {
        return slotPrefs.getString("date");
    }

    public ArrayList<String> getKeys(String prefs) {
        StringTokenizer tokenizer = new StringTokenizer(getKeysString(getPreferences(prefs)), " ");
        ArrayList<String> result = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }
        return result;
    }

    private String getKeysString(Preferences preferences) {
        return preferences.getString("keys", getDefaultKeys());
    }

    private String getDefaultKeys() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            stringBuilder.append("def_slot_").append(i).append(" ");
        }
        return stringBuilder.toString();
    }

    public SaveSlotInfo getSlotInfo(String key, String prefsName) {
        Preferences preferences = getPreferences(prefsName);
        SaveSlotInfo info = new SaveSlotInfo();
        info.name = preferences.getString(key + ":name");
        info.description = preferences.getString(key + ":desc");
        info.key = key;
        return info;
    }

    public String getKeyForNewSlot(String prefs) {
        String key;
        do {
            key = prefs + "_" + YioGdxGame.random.nextInt(10000);
        } while (containsKey(key, prefs));
        return key;
    }

    public boolean containsKey(String key, String prefs) {
        Iterator it = getKeys(prefs).iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(key)) {
                return true;
            }
        }
        return false;
    }

    public void addKey(String newKey, String prefs) {
        if (!containsKey(newKey, prefs)) {
            Preferences preferences = getPreferences(prefs);
            preferences.putString("keys", newKey + " " + getKeysString(preferences));
            preferences.flush();
        }
    }

    public void deleteSlot(SaveSlotInfo saveSlotInfo, String prefs) {
        Preferences preferences = getPreferences(prefs);
        StringBuilder newKeys = new StringBuilder();
        Iterator it = getKeys(prefs).iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (!key.equals(saveSlotInfo.key)) {
                newKeys.append(key).append(" ");
            }
        }
        preferences.putString("keys", newKeys.toString());
        preferences.flush();
    }

    public void editSlot(String key, SaveSlotInfo saveSlotInfo, String prefs) {
        Preferences preferences = getPreferences(prefs);
        preferences.putString(key + ":name", saveSlotInfo.name);
        preferences.putString(key + ":desc", saveSlotInfo.getDescription());
        preferences.flush();
    }

    private Preferences getPreferences(String prefs) {
        return Gdx.app.getPreferences(prefs);
    }

    public void performAutosave() {
        String autosaveKey = AUTOSAVE_KEY;
        if (!containsKey(autosaveKey, SAVE_SLOT_PREFS)) {
            addKey(autosaveKey, SAVE_SLOT_PREFS);
        }
        SaveSlotInfo saveSlotInfo = new SaveSlotInfo();
        saveSlotInfo.name = LanguagesManager.getInstance().getString(AUTOSAVE_KEY);
        saveSlotInfo.description = Yio.getDate();
        editSlot(autosaveKey, saveSlotInfo, SAVE_SLOT_PREFS);
        saveGame(autosaveKey);
        moveAutosaveKeyToFirstPlace();
    }

    public void moveAutosaveKeyToFirstPlace() {
        if (containsKey(AUTOSAVE_KEY, SAVE_SLOT_PREFS)) {
            ArrayList<String> keys = getKeys(SAVE_SLOT_PREFS);
            keys.remove(AUTOSAVE_KEY);
            keys.add(0, AUTOSAVE_KEY);
            StringBuilder newKeys = new StringBuilder();
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                newKeys.append((String) it.next()).append(" ");
            }
            Preferences preferences = getPreferences(SAVE_SLOT_PREFS);
            preferences.putString("keys", newKeys.toString());
            preferences.flush();
        }
    }

    public void saveGame(String prefsName) {
        this.gameSaver.saveGame(prefsName);
    }

    public void loadGame(String prefsName) {
        this.gameSaver.loadGame(prefsName);
    }

    public void loadTopSlot() {
        ArrayList<String> keys = getKeys(SAVE_SLOT_PREFS);
        if (keys.size() != 0) {
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!key.equals(AUTOSAVE_KEY)) {
                    loadGame(key);
                    return;
                }
            }
        }
    }
}
