package io.androidovshchik.antiyoy.gameplay.replays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.GameSaver;
import yio.tro.antiyoy.gameplay.replays.actions.RepAction;
import yio.tro.antiyoy.gameplay.replays.actions.RepActionFactory;
import yio.tro.antiyoy.gameplay.rules.GameRules;

public class Replay {
    public ArrayList<RepAction> actions = new ArrayList();
    public ArrayList<RepAction> buffer = new ArrayList();
    int currentStepIndex = 0;
    RepActionFactory factory = new RepActionFactory();
    private final GameController gameController;
    boolean go = true;
    public String initialLevelString;
    public int realNumberOfHumans = 0;
    String stringActions = null;
    public int tempColorOffset = 0;
    boolean tempGo = true;
    public boolean tempSlayRules = false;

    public Replay(GameController gameController) {
        this.gameController = gameController;
    }

    public void recreateInitialSituation() {
        GameSaver gameSaver = this.gameController.gameSaver;
        gameSaver.setActiveHexesString(this.initialLevelString.substring(this.initialLevelString.indexOf("/") + 1));
        this.gameController.fieldController.clearField();
        this.gameController.fieldController.cleanOutAllHexesInField();
        gameSaver.createHexStrings();
        gameSaver.recreateMap();
    }

    public void updateInitialLevelString() {
        if (Settings.replaysEnabled) {
            this.initialLevelString = this.gameController.fieldController.getFullLevelString();
        }
    }

    void addAction(RepAction repAction) {
        repAction.initType();
        this.buffer.add(repAction);
        if (repAction.type == 6) {
            applyBuffer();
        }
    }

    public void prepare() {
        this.currentStepIndex = 0;
        this.go = true;
        onTacticalPause();
        if (GameRules.replayMode) {
            this.gameController.speedManager.setSpeed(0);
        } else {
            this.realNumberOfHumans = this.gameController.playersNumber;
        }
    }

    public void performStep() {
        while (this.go && this.actions.size() != 0 && this.currentStepIndex >= 0 && this.currentStepIndex < this.actions.size()) {
            RepAction repAction = (RepAction) this.actions.get(this.currentStepIndex);
            repAction.perform(this.gameController);
            increaseStepIndex();
            if (repAction.type == 6) {
                return;
            }
        }
    }

    public void onTacticalPause() {
        if (this.go) {
            this.tempGo = this.go;
            this.go = false;
        }
    }

    public void onResumeNormalSpeed() {
        this.go = this.tempGo;
    }

    private void increaseStepIndex() {
        this.currentStepIndex++;
        if (this.currentStepIndex >= this.actions.size()) {
            this.go = false;
        }
    }

    private void applyBuffer() {
        Iterator it = this.buffer.iterator();
        while (it.hasNext()) {
            this.actions.add((RepAction) it.next());
        }
        this.buffer.clear();
    }

    public void saveToPreferences(String prefsKey) {
        Preferences preferences = Gdx.app.getPreferences(prefsKey);
        preferences.putString("initial", this.initialLevelString);
        preferences.putBoolean("slay_rules", this.tempSlayRules);
        preferences.putInteger("color_offset", this.tempColorOffset);
        preferences.putInteger("real_human_number", this.realNumberOfHumans);
        preferences.putString("actions", convertActionsToString());
        preferences.flush();
    }

    private String convertActionsToString() {
        StringBuilder builder = new StringBuilder();
        Iterator it = this.actions.iterator();
        while (it.hasNext()) {
            RepAction action = (RepAction) it.next();
            builder.append(action.type).append("-");
            builder.append(action.saveInfo()).append("#");
        }
        return builder.toString();
    }

    public void loadFromPreferences(String prefsKey) {
        Preferences preferences = Gdx.app.getPreferences(prefsKey);
        this.initialLevelString = preferences.getString("initial");
        this.tempSlayRules = preferences.getBoolean("slay_rules");
        this.tempColorOffset = preferences.getInteger("color_offset", 0);
        this.realNumberOfHumans = preferences.getInteger("real_human_number", 0);
        this.stringActions = preferences.getString("actions");
    }

    public void updateActionsFromString(FieldController fieldController) {
        if (this.stringActions != null) {
            StringTokenizer tokenizer = new StringTokenizer(this.stringActions, "#");
            this.actions.clear();
            this.buffer.clear();
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                int indexOfMinus = token.indexOf("-");
                RepAction action = this.factory.createAction(Integer.valueOf(token.substring(0, indexOfMinus)).intValue());
                action.loadInfo(fieldController, token.substring(indexOfMinus + 1));
                addAction(action);
            }
            applyBuffer();
        }
    }

    public void recreateBufferFromSnapshot(ArrayList<RepAction> src) {
        this.buffer.clear();
        Iterator it = src.iterator();
        while (it.hasNext()) {
            this.buffer.add((RepAction) it.next());
        }
    }

    public void setTempSlayRules(boolean tempSlayRules) {
        this.tempSlayRules = tempSlayRules;
    }

    public void setTempColorOffset(int tempColorOffset) {
        this.tempColorOffset = tempColorOffset;
    }

    public void setRealNumberOfHumans(int realNumberOfHumans) {
        this.realNumberOfHumans = realNumberOfHumans;
    }

    public void showInConsole() {
        Iterator it;
        System.out.println();
        if (this.actions.size() < 50) {
            System.out.println("Replay actions:");
            it = this.actions.iterator();
            while (it.hasNext()) {
                System.out.println("- " + ((RepAction) it.next()));
            }
        } else {
            System.out.println("Replay actions size = " + this.actions.size());
        }
        System.out.println("Replay buffer:");
        it = this.buffer.iterator();
        while (it.hasNext()) {
            System.out.println("- " + ((RepAction) it.next()));
        }
    }
}
