package io.androidovshchik.antiyoy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyInfoCondensed;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.stuff.Yio;

public class GameSaver {
    String activeHexesString;
    private GameController gameController;
    private ArrayList<String> hexStrings;
    private Preferences prefs;
    String tokenSeparator = "#";

    public GameSaver(GameController gameController) {
        this.gameController = gameController;
    }

    private void saveBasicInfo() {
        this.prefs.putInteger("save_turn", this.gameController.turn);
        this.prefs.putInteger("save_color_number", GameRules.colorNumber);
        this.prefs.putInteger("save_level_size", this.gameController.fieldController.levelSize);
        this.prefs.putInteger("save_player_number", this.gameController.playersNumber);
        this.prefs.putBoolean("save_campaign_mode", GameRules.campaignMode);
        this.prefs.putInteger("save_current_level", CampaignProgressManager.getInstance().currentLevelIndex);
        this.prefs.putInteger("save_difficulty", GameRules.difficulty);
        this.prefs.putInteger("save_color_offset", this.gameController.colorIndexViewOffset);
        this.prefs.putBoolean("slay_rules", GameRules.slayRules);
        this.prefs.putString("date", Yio.getDate());
        this.prefs.putBoolean("fog_of_war", GameRules.fogOfWarEnabled);
        this.prefs.putBoolean("diplomacy", GameRules.diplomacyEnabled);
        this.prefs.putBoolean("user_level_mode", GameRules.userLevelMode);
        if (GameRules.ulKey != null) {
            this.prefs.putString("ul_key", GameRules.ulKey);
        }
    }

    private void saveStatistics() {
        MatchStatistics matchStatistics = this.gameController.matchStatistics;
        this.prefs.putInteger("save_stat_turns_made", matchStatistics.turnsMade);
        this.prefs.putInteger("save_stat_units_died", matchStatistics.unitsDied);
        this.prefs.putInteger("save_stat_units_produced", matchStatistics.unitsProduced);
        this.prefs.putInteger("save_stat_money_spent", matchStatistics.moneySpent);
        this.prefs.putInteger("save_stat_time_count", matchStatistics.timeCount);
    }

    private String getHexString(Hex hex) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("" + hex.index1);
        stringBuffer.append(" " + hex.index2);
        stringBuffer.append(" " + hex.colorIndex);
        stringBuffer.append(" " + hex.objectInside);
        if (hex.containsUnit()) {
            stringBuffer.append(" " + hex.unit.strength);
            if (hex.unit.isReadyToMove()) {
                stringBuffer.append(" 1");
            } else {
                stringBuffer.append(" 0");
            }
        } else {
            stringBuffer.append(" 0");
            stringBuffer.append(" 0");
        }
        Province province = this.gameController.getProvinceByHex(hex);
        if (province != null) {
            stringBuffer.append(" " + province.money);
        } else {
            stringBuffer.append(" 10");
        }
        return stringBuffer.toString();
    }

    public String getActiveHexesString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            stringBuffer.append(getHexString((Hex) it.next()));
            stringBuffer.append(this.tokenSeparator);
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        return stringBuffer.toString();
    }

    public void saveGameToSlot(int slotIndex) {
    }

    void saveGame() {
        saveGame("save");
    }

    public void saveGame(String prefsName) {
        this.prefs = Gdx.app.getPreferences(prefsName);
        saveBasicInfo();
        saveStatistics();
        saveDiplomacy();
        saveInitialLevelString();
        this.prefs.putString("save_active_hexes", getActiveHexesString());
        this.prefs.flush();
    }

    private void saveInitialLevelString() {
        this.prefs.putString("initial_level", this.gameController.fieldController.initialLevelString);
    }

    private void saveDiplomacy() {
        if (GameRules.diplomacyEnabled) {
            DiplomacyManager diplomacyManager = this.gameController.fieldController.diplomacyManager;
            DiplomacyInfoCondensed instance = DiplomacyInfoCondensed.getInstance();
            instance.update(diplomacyManager);
            this.prefs.putString("diplomacy_info", instance.getFull());
        }
    }

    public void setActiveHexesString(String activeHexesString) {
        this.activeHexesString = activeHexesString;
    }

    private String getHexStringBySnapshot(int[] snapshot) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < snapshot.length; i++) {
            stringBuffer.append("" + snapshot[i]);
            if (i != snapshot.length - 1) {
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();
    }

    private int[] getHexSnapshotByString(String hexString) {
        int[] snapshot = new int[7];
        StringTokenizer stringTokenizer = new StringTokenizer(hexString, " ");
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            snapshot[i] = Integer.valueOf(stringTokenizer.nextToken()).intValue();
            i++;
        }
        return snapshot;
    }

    private void activateHexByString(String hexString) {
        int[] snapshot = getHexSnapshotByString(hexString);
        int index1 = snapshot[0];
        Hex hex = this.gameController.fieldController.field[index1][snapshot[1]];
        hex.active = true;
        hex.setColorIndex(snapshot[2]);
        ListIterator activeIterator = this.gameController.fieldController.activeHexes.listIterator();
        int objectInside = snapshot[3];
        if (objectInside > 0) {
            this.gameController.addSolidObject(hex, objectInside);
        }
        int unitStrength = snapshot[4];
        if (unitStrength > 0) {
            this.gameController.addUnit(hex, unitStrength);
            if (snapshot[5] == 1) {
                hex.unit.setReadyToMove(true);
                hex.unit.startJumping();
            } else {
                hex.unit.setReadyToMove(false);
                hex.unit.stopJumping();
            }
        }
        hex.moveZoneNumber = snapshot[6];
        activeIterator.add(hex);
    }

    public void createHexStrings() {
        StringTokenizer tokenizer = new StringTokenizer(this.activeHexesString, this.tokenSeparator);
        this.hexStrings = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            this.hexStrings.add(tokenizer.nextToken());
        }
    }

    public void recreateMap() {
        Iterator it = this.hexStrings.iterator();
        while (it.hasNext()) {
            activateHexByString((String) it.next());
        }
        this.gameController.fieldController.detectProvinces();
        it = this.gameController.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            province.money = ((Hex) province.hexList.get(0)).moveZoneNumber;
            province.updateName();
        }
    }

    private void loadStatistics() {
        MatchStatistics matchStatistics = this.gameController.matchStatistics;
        matchStatistics.turnsMade = this.prefs.getInteger("save_stat_turns_made", matchStatistics.turnsMade);
        matchStatistics.unitsDied = this.prefs.getInteger("save_stat_units_died", matchStatistics.unitsDied);
        matchStatistics.unitsProduced = this.prefs.getInteger("save_stat_units_produced", matchStatistics.unitsProduced);
        matchStatistics.moneySpent = this.prefs.getInteger("save_stat_money_spent", matchStatistics.moneySpent);
        matchStatistics.timeCount = this.prefs.getInteger("save_stat_time_count", matchStatistics.timeCount);
    }

    public void beginRecreation() {
        this.gameController.fieldController.createFieldMatrix();
        createHexStrings();
        recreateMap();
    }

    public void loadGame(String prefsName) {
        this.prefs = Gdx.app.getPreferences(prefsName);
        this.activeHexesString = this.prefs.getString("save_active_hexes", "");
        if (this.activeHexesString.length() >= 3) {
            LoadingParameters instance = LoadingParameters.getInstance();
            instance.mode = 3;
            instance.applyPrefs(this.prefs);
            instance.activeHexes = this.activeHexesString;
            LoadingManager.getInstance().startGame(instance);
            loadStatistics();
            loadDiplomacy();
            loadInitialLevelString();
        }
    }

    private void loadInitialLevelString() {
        this.gameController.fieldController.initialLevelString = this.prefs.getString("initial_level", null);
    }

    private void loadDiplomacy() {
        if (GameRules.diplomacyEnabled) {
            DiplomacyManager diplomacyManager = this.gameController.fieldController.diplomacyManager;
            DiplomacyInfoCondensed instance = DiplomacyInfoCondensed.getInstance();
            instance.setFull(this.prefs.getString("diplomacy_info", "-"));
            instance.apply(diplomacyManager);
        }
    }

    public void detectRules() {
        GameRules.setSlayRules(true);
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            if (doesHexRequireGenericRules((Hex) it.next())) {
                GameRules.setSlayRules(false);
                System.out.println("detected generic rules");
                return;
            }
        }
    }

    private boolean doesHexRequireGenericRules(Hex activeHex) {
        if (activeHex.colorIndex == FieldController.NEUTRAL_LANDS_INDEX || activeHex.objectInside == 6 || activeHex.objectInside == 7) {
            return true;
        }
        return false;
    }
}
