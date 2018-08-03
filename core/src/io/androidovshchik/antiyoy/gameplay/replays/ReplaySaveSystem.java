package io.androidovshchik.antiyoy.gameplay.replays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.campaign.CampaignProgressManager;
import yio.tro.antiyoy.gameplay.rules.GameRules;

public class ReplaySaveSystem {
    public static final String REPLAYS_PREFS = "antiyoy.replays";
    private static ReplaySaveSystem instance;
    GameController gameController = null;
    private ArrayList<String> keys = new ArrayList();
    private Preferences prefs;

    public static void resetInstance() {
        instance = null;
    }

    public static ReplaySaveSystem getInstance() {
        if (instance == null) {
            instance = new ReplaySaveSystem();
        }
        return instance;
    }

    private void loadKeys() {
        updatePrefs();
        StringTokenizer tokenizer = new StringTokenizer(this.prefs.getString("keys"), " ");
        this.keys.clear();
        while (tokenizer.hasMoreTokens()) {
            this.keys.add(tokenizer.nextToken());
        }
    }

    private void updatePrefs() {
        this.prefs = Gdx.app.getPreferences(REPLAYS_PREFS);
    }

    private int getMaxKey() {
        int max = 0;
        Iterator it = this.keys.iterator();
        while (it.hasNext()) {
            int value = Integer.valueOf((String) it.next()).intValue();
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private String getKeyForNewSlot() {
        return "" + (getMaxKey() + 1);
    }

    private void saveKeys() {
        updatePrefs();
        StringBuilder builder = new StringBuilder();
        Iterator it = this.keys.iterator();
        while (it.hasNext()) {
            builder.append((String) it.next()).append(" ");
        }
        this.prefs.putString("keys", builder.toString());
        this.prefs.flush();
    }

    public void clearKeys() {
        loadKeys();
        this.keys.clear();
        saveKeys();
    }

    public void saveReplay(Replay replay) {
        RepSlot repSlot = new RepSlot(this.gameController, addKey());
        repSlot.campaignMode = GameRules.campaignMode;
        repSlot.levelIndex = CampaignProgressManager.getInstance().getCurrentLevelIndex();
        repSlot.numberOfHumans = replay.realNumberOfHumans;
        repSlot.replay = replay;
        repSlot.save();
    }

    public void removeReplay(String key) {
        this.keys.remove(key);
        saveKeys();
    }

    public RepSlot getSlotByKey(String key) {
        RepSlot repSlot = new RepSlot(this.gameController, key);
        repSlot.load();
        return repSlot;
    }

    private String addKey() {
        loadKeys();
        String keyForNewSlot = getKeyForNewSlot();
        this.keys.add(0, keyForNewSlot);
        saveKeys();
        return keyForNewSlot;
    }

    public ArrayList<String> getKeys() {
        loadKeys();
        return this.keys;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
