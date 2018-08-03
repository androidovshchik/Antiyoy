package io.androidovshchik.antiyoy.gameplay.replays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.stuff.Yio;

public class RepSlot {
    public boolean campaignMode;
    public String date = Yio.getDate();
    GameController gameController;
    public String key;
    public int levelIndex = -1;
    public int numberOfHumans;
    public Replay replay;

    public RepSlot(GameController gameController, String key) {
        this.gameController = gameController;
        this.key = key;
        this.replay = new Replay(gameController);
    }

    public void save() {
        Preferences preferences = Gdx.app.getPreferences(this.key);
        preferences.putBoolean("campaign", this.campaignMode);
        preferences.putInteger("players", this.numberOfHumans);
        preferences.putString("date", this.date);
        preferences.putInteger("level_index", this.levelIndex);
        this.replay.setTempSlayRules(GameRules.slayRules);
        this.replay.setTempColorOffset(this.gameController.colorIndexViewOffset);
        preferences.flush();
        this.replay.saveToPreferences(this.key);
    }

    public void load() {
        Preferences preferences = Gdx.app.getPreferences(this.key);
        this.campaignMode = preferences.getBoolean("campaign");
        this.numberOfHumans = preferences.getInteger("players");
        this.date = preferences.getString("date", "-");
        this.levelIndex = preferences.getInteger("level_index", -1);
    }
}
