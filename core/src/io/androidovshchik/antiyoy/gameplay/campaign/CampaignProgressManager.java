package io.androidovshchik.antiyoy.gameplay.campaign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.Arrays;
import java.util.StringTokenizer;
import yio.tro.antiyoy.gameplay.DebugFlags;
import yio.tro.antiyoy.gameplay.rules.GameRules;

public class CampaignProgressManager {
    public static final int INDEX_OF_LAST_LEVEL = 105;
    public static final String PROGRESS_PREFS_GENERIC = "antiyoy.progress";
    public static final String PROGRESS_PREFS_SLAY = "antiyoy.progress.slay";
    private static CampaignProgressManager instance;
    public int currentLevelIndex;
    boolean[] progress = new boolean[(getIndexOfLastLevel() + 1)];

    public static void initialize() {
        instance = null;
    }

    public static CampaignProgressManager getInstance() {
        if (instance == null) {
            instance = new CampaignProgressManager();
            instance.loadProgress();
        }
        return instance;
    }

    private CampaignProgressManager() {
    }

    public boolean completedCampaignLevel(int winColor) {
        return GameRules.campaignMode && winColor == 0;
    }

    public static int getIndexOfLastLevel() {
        return 105;
    }

    public void markLevelAsCompleted(int index) {
        this.progress[index] = true;
        saveProgress();
    }

    public boolean isLevelLocked(int index) {
        if (DebugFlags.unlockLevels || index == 9 || index == 24 || index == 60 || isLevelComplete(index) || isLevelComplete(index - 1)) {
            return false;
        }
        return true;
    }

    public boolean isLevelComplete(int index) {
        if (index < 0) {
            return true;
        }
        return getProgress()[index];
    }

    private void saveProgress() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.progress.length; i++) {
            if (this.progress[i]) {
                builder.append(i).append(" ");
            }
        }
        System.out.println();
        System.out.println("CampaignProgressManager.saveProgress");
        System.out.println("progress = " + Arrays.toString(this.progress));
        System.out.println("builder.toString() = " + builder.toString());
        Preferences preferences = getPreferences();
        preferences.putString("completed_levels", builder.toString());
        preferences.flush();
    }

    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PROGRESS_PREFS_GENERIC);
    }

    private void clearProgress() {
        for (int i = 0; i < this.progress.length; i++) {
            this.progress[i] = false;
        }
    }

    public void resetProgress() {
        clearProgress();
        saveProgress();
    }

    public int getNextLevelIndex() {
        int nextLevelIndex = this.currentLevelIndex + 1;
        if (nextLevelIndex > getIndexOfLastLevel()) {
            return getIndexOfLastLevel();
        }
        return nextLevelIndex;
    }

    private void loadProgress() {
        clearProgress();
        Preferences preferences = getPreferences();
        StringTokenizer tokenizer = new StringTokenizer(preferences.getString("completed_levels", ""), " ");
        while (tokenizer.hasMoreTokens()) {
            int index = Integer.valueOf(tokenizer.nextToken()).intValue();
            if (index >= this.progress.length) {
                break;
            }
            this.progress[index] = true;
        }
        checkToImportOldProgress(preferences);
    }

    private void checkToImportOldProgress(Preferences preferences) {
        if (!preferences.getBoolean("imported_old_progress", false)) {
            importOldProgress();
            preferences.putBoolean("imported_old_progress", true);
        }
    }

    private void importOldProgress() {
        int progress = Gdx.app.getPreferences("main").getInteger("progress", 0);
        for (int i = 0; i < progress; i++) {
            markLevelAsCompleted(i);
        }
    }

    public boolean[] getProgress() {
        return this.progress;
    }

    public int getIndexOfRelevantLevel() {
        int index = 0;
        for (int i = 0; i < this.progress.length; i++) {
            if (this.progress[i]) {
                index = i;
            }
        }
        index++;
        if (index >= this.progress.length) {
            return index - 1;
        }
        return index;
    }

    public boolean isAtLeastOneLevelCompleted() {
        for (int i = 0; i < this.progress.length; i++) {
            if (isLevelComplete(i)) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentLevelIndex(int currentLevelIndex) {
        this.currentLevelIndex = currentLevelIndex;
    }

    public int getCurrentLevelIndex() {
        return this.currentLevelIndex;
    }

    public boolean isLastLevel() {
        return this.currentLevelIndex == getIndexOfLastLevel();
    }

    public String toString() {
        return "[Progress = " + Arrays.toString(this.progress) + "]";
    }
}
