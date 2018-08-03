package io.androidovshchik.antiyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.game_view.GameView;
import io.androidovshchik.antiyoy.menu.CheckButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.save_slot_selector.SaveSystem;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class Settings {
    public static boolean askToEndTurn = false;
    public static boolean autosave;
    public static boolean fastConstruction;
    static Settings instance = null;
    public static boolean leftHandMode;
    public static boolean longTapToMove;
    public static boolean musicEnabled;
    public static boolean replaysEnabled;
    public static float sensitivity;
    public static int skinIndex;
    public static boolean soundEnabled = true;
    public static boolean waterTexture;
    private GameController gameController;
    private GameView gameView;
    private MenuControllerYio menuControllerYio;
    YioGdxGame yioGdxGame;

    public static void initialize() {
        instance = null;
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public void setYioGdxGame(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        this.menuControllerYio = yioGdxGame.menuControllerYio;
        this.gameView = yioGdxGame.gameView;
        this.gameController = yioGdxGame.gameController;
    }

    public void loadSettings() {
        boolean z;
        Preferences prefs = Gdx.app.getPreferences("settings");
        if (prefs.getInteger("sound", 0) == 0) {
            soundEnabled = false;
        } else {
            soundEnabled = true;
        }
        this.menuControllerYio.getCheckButtonById(5).setChecked(soundEnabled);
        skinIndex = prefs.getInteger("skin", 0);
        this.gameView.loadSkin(skinIndex);
        int AS = prefs.getInteger(SaveSystem.AUTOSAVE_KEY, 0);
        autosave = false;
        if (AS == 1) {
            autosave = true;
        }
        this.menuControllerYio.getCheckButtonById(1).setChecked(autosave);
        sensitivity = (float) prefs.getInteger("sensitivity", 6);
        sensitivity = Math.max(0.1f, sensitivity / 6.0f);
        if (prefs.getInteger("ask_to_end_turn", 0) == 1) {
            z = true;
        } else {
            z = false;
        }
        askToEndTurn = z;
        this.menuControllerYio.getCheckButtonById(3).setChecked(askToEndTurn);
        int cityNames = prefs.getInteger("city_names", 0);
        this.gameController.setCityNamesEnabled(cityNames);
        CheckButtonYio checkButtonById = this.menuControllerYio.getCheckButtonById(4);
        if (cityNames == 1) {
            z = true;
        } else {
            z = false;
        }
        checkButtonById.setChecked(z);
        longTapToMove = prefs.getBoolean("long_tap_to_move", true);
        applyCheckButtonIfNotNull(this.menuControllerYio.getCheckButtonById(7), longTapToMove);
        waterTexture = prefs.getBoolean("water_texture", false);
        applyCheckButtonIfNotNull(this.menuControllerYio.getCheckButtonById(10), waterTexture);
        this.gameView.loadBackgroundTexture();
        replaysEnabled = prefs.getBoolean("replays_enabled", true);
        applyCheckButtonIfNotNull(this.menuControllerYio.getCheckButtonById(8), replaysEnabled);
        fastConstruction = prefs.getBoolean("fast_construction", false);
        applyCheckButtonIfNotNull(this.menuControllerYio.getCheckButtonById(9), fastConstruction);
        musicEnabled = prefs.getBoolean("music", false);
        applyCheckButtonIfNotNull(this.menuControllerYio.getCheckButtonById(2), musicEnabled);
        leftHandMode = prefs.getBoolean("left_hand_mode", false);
        applyCheckButtonIfNotNull(this.menuControllerYio.getCheckButtonById(12), leftHandMode);
        MusicManager.getInstance().onMusicStatusChanged();
    }

    public boolean saveSettings() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.putInteger("sound", boolToInteger(this.menuControllerYio.getCheckButtonById(5).isChecked()));
        prefs.putInteger(SaveSystem.AUTOSAVE_KEY, boolToInteger(this.menuControllerYio.getCheckButtonById(1).isChecked()));
        prefs.putInteger("ask_to_end_turn", boolToInteger(this.menuControllerYio.getCheckButtonById(3).isChecked()));
        prefs.putInteger("city_names", boolToInteger(this.menuControllerYio.getCheckButtonById(4).isChecked()));
        prefs.putBoolean("music", this.menuControllerYio.getCheckButtonById(2).isChecked());
        prefs.flush();
        MusicManager.getInstance().onMusicStatusChanged();
        return false;
    }

    public void saveMoreSettings() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        saveSkin(prefs);
        prefs.putInteger("sensitivity", Scenes.sceneMoreSettingsMenu.sensitivitySlider.getCurrentRunnerIndex());
        saveWaterTexture(prefs);
        prefs.putBoolean("long_tap_to_move", this.menuControllerYio.getCheckButtonById(7).isChecked());
        prefs.putBoolean("replays_enabled", this.menuControllerYio.getCheckButtonById(8).isChecked());
        prefs.putBoolean("fast_construction", this.menuControllerYio.getCheckButtonById(9).isChecked());
        prefs.putBoolean("left_hand_mode", this.menuControllerYio.getCheckButtonById(12).isChecked());
        MusicManager.getInstance().onMusicStatusChanged();
        prefs.flush();
    }

    private void applyCheckButtonIfNotNull(CheckButtonYio checkButtonYio, boolean value) {
        if (checkButtonYio != null) {
            checkButtonYio.setChecked(value);
        }
    }

    private void saveWaterTexture(Preferences prefs) {
        CheckButtonYio chkWaterTexture = this.menuControllerYio.getCheckButtonById(10);
        if (chkWaterTexture != null) {
            prefs.putBoolean("water_texture", chkWaterTexture.isChecked());
        }
    }

    private boolean saveSkin(Preferences prefs) {
        int lastIndex = skinIndex;
        skinIndex = Scenes.sceneMoreSettingsMenu.skinSlider.getCurrentRunnerIndex();
        if (skinIndex == lastIndex) {
            return false;
        }
        prefs.putInteger("skin", skinIndex);
        Scenes.sceneSelectionOverlay.onSkinChanged();
        if (lastIndex == skinIndex) {
            return false;
        }
        if (lastIndex == 3 || skinIndex == 3) {
            return true;
        }
        return false;
    }

    public static boolean isShroomArtsEnabled() {
        return skinIndex == 3;
    }

    private int boolToInteger(boolean b) {
        if (b) {
            return 1;
        }
        return 0;
    }
}
