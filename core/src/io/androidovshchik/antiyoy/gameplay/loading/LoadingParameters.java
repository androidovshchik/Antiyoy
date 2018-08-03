package io.androidovshchik.antiyoy.gameplay.loading;

import com.badlogic.gdx.Preferences;
import java.util.StringTokenizer;
import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.diplomacy.DiplomacyInfoCondensed;
import yio.tro.antiyoy.gameplay.replays.Replay;
import yio.tro.antiyoy.gameplay.rules.GameRules;

public class LoadingParameters {
    private static LoadingParameters instance = null;
    public String activeHexes;
    public int campaignLevelIndex;
    public int colorNumber;
    public int colorOffset;
    public int difficulty;
    public boolean diplomacy;
    public DiplomacyInfoCondensed diplomacyInfo;
    public boolean fogOfWar;
    public int levelSize;
    public int mode;
    public int playersNumber;
    public Replay replay;
    public boolean slayRules;
    public int turn;
    public String ulKey;
    public boolean userLevelMode;

    void defaultValues() {
        this.mode = -1;
        this.levelSize = -1;
        this.playersNumber = -1;
        this.colorNumber = -1;
        this.difficulty = -1;
        this.colorOffset = -1;
        this.slayRules = false;
        this.campaignLevelIndex = YioGdxGame.random.nextInt();
        this.activeHexes = "";
        this.turn = -1;
        this.replay = null;
        this.diplomacyInfo = null;
        this.fogOfWar = false;
        this.diplomacy = false;
        this.ulKey = null;
    }

    public void copyFrom(LoadingParameters src) {
        this.mode = src.mode;
        this.levelSize = src.levelSize;
        this.playersNumber = src.playersNumber;
        this.colorNumber = src.colorNumber;
        this.difficulty = src.difficulty;
        this.colorOffset = src.colorOffset;
        this.slayRules = src.slayRules;
        this.campaignLevelIndex = src.campaignLevelIndex;
        this.activeHexes = src.activeHexes;
        this.turn = src.turn;
        this.replay = src.replay;
        this.fogOfWar = src.fogOfWar;
        this.diplomacy = src.diplomacy;
        this.diplomacyInfo = src.diplomacyInfo;
        this.userLevelMode = src.userLevelMode;
        this.ulKey = src.ulKey;
    }

    public static void initialize() {
        instance = null;
    }

    public static LoadingParameters getInstance() {
        if (instance == null) {
            instance = new LoadingParameters();
        }
        instance.defaultValues();
        return instance;
    }

    public void showInConsole() {
        System.out.println();
        System.out.println("Parameters:");
        System.out.println("loadMode = " + this.mode);
        System.out.println("levelSize = " + this.levelSize);
        System.out.println("playersNumber = " + this.playersNumber);
        System.out.println("colorNumber = " + this.colorNumber);
        System.out.println("difficulty = " + this.difficulty);
        System.out.println("colorOffset = " + this.colorOffset);
        System.out.println("slayRules = " + this.slayRules);
        System.out.println("campaignLevelIndex = " + this.campaignLevelIndex);
        System.out.println("activeHexes = " + this.activeHexes);
        System.out.println("turn = " + this.turn);
        System.out.println("replay = " + this.replay);
        System.out.println("fogOfWar = " + this.fogOfWar);
        System.out.println("diplomacy = " + this.diplomacy);
        System.out.println("userLevelMode = " + this.userLevelMode);
        System.out.println("ulKey = " + this.ulKey);
        System.out.println();
    }

    public void applyFullLevel(String fullLevel) {
        int delimiterChar = fullLevel.indexOf("/");
        int[] basicInfoValues = new int[4];
        if (delimiterChar >= 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(fullLevel.substring(0, delimiterChar), " ");
            int i = 0;
            while (stringTokenizer.hasMoreTokens()) {
                basicInfoValues[i] = Integer.valueOf(stringTokenizer.nextToken()).intValue();
                i++;
            }
            this.activeHexes = fullLevel.substring(delimiterChar + 1, fullLevel.length());
            this.playersNumber = basicInfoValues[2];
            this.colorNumber = basicInfoValues[3];
            this.levelSize = basicInfoValues[1];
            this.difficulty = basicInfoValues[0];
        }
    }

    public void applyPrefs(Preferences prefs) {
        this.turn = prefs.getInteger("save_turn");
        this.playersNumber = prefs.getInteger("save_player_number");
        this.colorNumber = prefs.getInteger("save_color_number");
        if (this.colorNumber > FieldController.NEUTRAL_LANDS_INDEX) {
            this.colorNumber = FieldController.NEUTRAL_LANDS_INDEX;
        }
        this.levelSize = prefs.getInteger("save_level_size");
        this.difficulty = prefs.getInteger("save_difficulty");
        GameRules.campaignMode = prefs.getBoolean("save_campaign_mode");
        this.campaignLevelIndex = prefs.getInteger("save_current_level");
        this.colorOffset = prefs.getInteger("save_color_offset", 0);
        this.slayRules = prefs.getBoolean("slay_rules", true);
        this.fogOfWar = prefs.getBoolean("fog_of_war", false);
        this.diplomacy = prefs.getBoolean("diplomacy", false);
        this.userLevelMode = prefs.getBoolean("user_level_mode", false);
        this.ulKey = prefs.getString("ul_key", null);
    }
}
