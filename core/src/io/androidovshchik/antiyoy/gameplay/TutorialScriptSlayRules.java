package io.androidovshchik.antiyoy.gameplay;

import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.gameplay.loading.LoadingManager;
import yio.tro.antiyoy.gameplay.loading.LoadingParameters;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.stuff.LanguagesManager;

public class TutorialScriptSlayRules extends TutorialScript {
    public static final int STEP_ABOUT_UNIT_DEATH = 9;
    public static final int STEP_ATTACK_WITH_MAN = 3;
    public static final int STEP_ATTACK_WITH_SPEARMAN = 1;
    public static final int STEP_BUILD_SPEARMAN = 8;
    public static final int STEP_BUILD_TOWER = 5;
    public static final int STEP_GOOD_LUCK = 17;
    public static final int STEP_HOLD_TO_MARCH = 13;
    public static final int STEP_MERGE_UNITS = 16;
    public static final int STEP_PRESS_BUILD_TOWER_BUTTON = 4;
    public static final int STEP_PRESS_BUILD_UNIT_BUTTON_FIRST_TIME = 6;
    public static final int STEP_PRESS_BUILD_UNIT_BUTTON_SECOND_TIME = 7;
    public static final int STEP_PRESS_END_TURN = 11;
    public static final int STEP_PRESS_UNDO = 10;
    public static final int STEP_PRESS_UNDO_AGAIN = 14;
    public static final int STEP_SELECT_MAN = 2;
    public static final int STEP_SELECT_PROVINCE = 12;
    public static final int STEP_SELECT_SPEARMAN = 0;
    public static final int STEP_SELECT_UNIT_TO_MERGE = 15;
    int currentStep;
    LanguagesManager languagesManager;
    final String map = "10 6 0 0 0 0 106#10 7 0 3 0 0 106#10 8 1 0 0 0 0#11 4 4 0 0 0 9#11 5 1 3 0 0 9#11 6 1 0 0 0 9#12 2 0 3 0 0 104#12 3 4 3 0 0 9#12 4 4 0 0 0 9#12 5 1 0 0 0 9#13 2 0 2 0 0 104#13 3 4 0 0 0 9#13 4 1 0 1 0 9#13 5 3 3 0 0 6#14 2 4 0 1 0 9#14 3 2 0 0 0 5#14 4 2 0 2 0 5#14 5 3 0 0 0 6#14 9 4 0 0 0 2#15 2 2 0 0 0 5#15 3 2 0 0 0 5#15 4 2 0 0 0 5#15 5 1 0 0 0 12#15 6 4 0 1 0 2#15 7 4 3 0 0 2#15 8 4 4 0 0 2#16 2 2 0 1 0 5#16 3 2 3 0 0 5#16 4 1 0 2 0 12#16 5 1 0 0 0 12#16 6 1 0 0 0 12#16 7 4 0 0 0 2#16 8 4 0 0 0 2#16 9 4 0 0 0 2#17 2 2 0 0 0 5#17 3 4 0 0 0 3#17 4 1 0 0 0 12#17 5 1 3 0 0 12#17 6 4 0 1 0 2#17 7 2 0 0 0 10#18 2 4 3 0 0 3#18 4 1 0 0 0 12#18 5 2 0 0 0 10#18 6 2 0 0 0 10#18 7 2 3 0 0 10#18 8 4 0 0 0 17#18 11 0 0 0 0 238#19 4 3 0 0 0 15#19 5 2 0 2 0 10#19 6 2 0 0 0 10#19 7 4 3 0 0 17#19 8 0 0 2 0 238#19 10 0 0 0 0 238#19 11 0 0 0 0 238#20 4 3 3 0 0 15#20 5 3 0 1 0 15#20 6 0 0 0 0 238#20 7 0 0 1 1 238#20 8 0 0 1 1 238#20 9 0 2 0 0 238#20 10 0 0 0 0 238#20 11 0 0 0 0 238#21 4 3 0 0 0 15#21 5 0 0 2 1 238#21 6 0 0 0 0 238#21 7 0 0 0 0 238#21 8 0 0 0 0 238#21 9 0 0 0 0 238#22 2 2 0 1 0 0#22 3 2 3 0 0 0#22 4 0 0 0 0 238#22 5 0 0 0 0 238#22 6 0 3 0 0 238#22 7 0 0 0 0 238#22 8 0 0 0 0 238#23 4 0 0 0 0 238#23 5 0 0 0 0 238#23 6 0 0 0 0 238#24 4 0 0 0 0 238#24 5 0 0 0 0 238#25 3 0 0 0 0 238";
    long timeForNextStep;
    long timeToCheck;
    boolean waitingBeforeNextStep;

    public TutorialScriptSlayRules(GameController gameController) {
        super(gameController);
    }

    public void createTutorialGame() {
        LoadingParameters instance = LoadingParameters.getInstance();
        instance.mode = 0;
        instance.activeHexes = "10 6 0 0 0 0 106#10 7 0 3 0 0 106#10 8 1 0 0 0 0#11 4 4 0 0 0 9#11 5 1 3 0 0 9#11 6 1 0 0 0 9#12 2 0 3 0 0 104#12 3 4 3 0 0 9#12 4 4 0 0 0 9#12 5 1 0 0 0 9#13 2 0 2 0 0 104#13 3 4 0 0 0 9#13 4 1 0 1 0 9#13 5 3 3 0 0 6#14 2 4 0 1 0 9#14 3 2 0 0 0 5#14 4 2 0 2 0 5#14 5 3 0 0 0 6#14 9 4 0 0 0 2#15 2 2 0 0 0 5#15 3 2 0 0 0 5#15 4 2 0 0 0 5#15 5 1 0 0 0 12#15 6 4 0 1 0 2#15 7 4 3 0 0 2#15 8 4 4 0 0 2#16 2 2 0 1 0 5#16 3 2 3 0 0 5#16 4 1 0 2 0 12#16 5 1 0 0 0 12#16 6 1 0 0 0 12#16 7 4 0 0 0 2#16 8 4 0 0 0 2#16 9 4 0 0 0 2#17 2 2 0 0 0 5#17 3 4 0 0 0 3#17 4 1 0 0 0 12#17 5 1 3 0 0 12#17 6 4 0 1 0 2#17 7 2 0 0 0 10#18 2 4 3 0 0 3#18 4 1 0 0 0 12#18 5 2 0 0 0 10#18 6 2 0 0 0 10#18 7 2 3 0 0 10#18 8 4 0 0 0 17#18 11 0 0 0 0 238#19 4 3 0 0 0 15#19 5 2 0 2 0 10#19 6 2 0 0 0 10#19 7 4 3 0 0 17#19 8 0 0 2 0 238#19 10 0 0 0 0 238#19 11 0 0 0 0 238#20 4 3 3 0 0 15#20 5 3 0 1 0 15#20 6 0 0 0 0 238#20 7 0 0 1 1 238#20 8 0 0 1 1 238#20 9 0 2 0 0 238#20 10 0 0 0 0 238#20 11 0 0 0 0 238#21 4 3 0 0 0 15#21 5 0 0 2 1 238#21 6 0 0 0 0 238#21 7 0 0 0 0 238#21 8 0 0 0 0 238#21 9 0 0 0 0 238#22 2 2 0 1 0 0#22 3 2 3 0 0 0#22 4 0 0 0 0 238#22 5 0 0 0 0 238#22 6 0 3 0 0 238#22 7 0 0 0 0 238#22 8 0 0 0 0 238#23 4 0 0 0 0 238#23 5 0 0 0 0 238#23 6 0 0 0 0 238#24 4 0 0 0 0 238#24 5 0 0 0 0 238#25 3 0 0 0 0 238";
        instance.playersNumber = 1;
        instance.colorNumber = 5;
        instance.levelSize = 1;
        instance.difficulty = 0;
        instance.colorOffset = 0;
        instance.slayRules = true;
        LoadingManager.getInstance().startGame(instance);
        this.menuControllerYio = this.gameController.yioGdxGame.menuControllerYio;
        this.languagesManager = LanguagesManager.getInstance();
        this.currentStep = -1;
        this.waitingBeforeNextStep = true;
        allButtonsIgnoreTouches();
        allHexesIgnoreTouches();
        showTutorialTip("tip_capture_with_units");
        this.menuControllerYio.getButtonById(30).setReaction(Reaction.rbChooseGameModeMenu);
        enableLongTapToMoveInSettings();
    }

    private void enableLongTapToMoveInSettings() {
        Settings.longTapToMove = true;
    }

    private Hex getHex(int x, int y) {
        return this.gameController.fieldController.field[x][y];
    }

    private void pointToHex(int x, int y) {
        this.gameController.forefinger.setPointTo(getHex(x, y));
    }

    private void pointToMenu(double x, double y, double rotation) {
        this.gameController.forefinger.setPointTo(x, y, rotation);
    }

    private void showMessage(String key) {
        Scenes.sceneNotification.showNotification(this.languagesManager.getString(key), false);
    }

    private void showTutorialTip(String key) {
        Scenes.sceneTutorialTip.createTutorialTip(this.menuControllerYio.getArrayListFromString(this.languagesManager.getString(key)));
        this.tipIsCurrentlyShown = true;
    }

    private void stepSetup() {
        this.gameController.setIgnoreMarch(true);
        ButtonYio buttonYio;
        switch (this.currentStep) {
            case 1:
                setOnlyHexToRespond(19, 7);
                showMessage("tut_tap_on_hex");
                return;
            case 2:
                setOnlyHexToRespond(20, 7);
                showMessage("tut_select_unit");
                return;
            case 3:
                setOnlyHexToRespond(18, 8);
                showMessage("tut_tap_on_hex");
                return;
            case 4:
                buttonYio = setOnlyButtonToRespond(38, "tut_press_button");
                pointToMenu((double) buttonYio.x1, (double) buttonYio.y2, -2.356194490192345d);
                return;
            case 5:
                setOnlyHexToRespond(21, 5);
                showMessage("tut_build_tower");
                return;
            case 6:
                buttonYio = setOnlyButtonToRespond(39, "tut_press_button");
                pointToMenu((double) buttonYio.x2, (double) buttonYio.y2, 2.356194490192345d);
                return;
            case 7:
                buttonYio = setOnlyButtonToRespond(39, "tut_again");
                pointToMenu((double) buttonYio.x2, (double) buttonYio.y2, 2.356194490192345d);
                return;
            case 8:
                setOnlyHexToRespond(20, 5);
                showMessage("tut_build_spearman");
                return;
            case 9:
                return;
            case 10:
                buttonYio = setOnlyButtonToRespond(32, "tut_tap_to_undo");
                pointToMenu((double) buttonYio.x2, (double) buttonYio.y2, 2.356194490192345d);
                return;
            case 11:
                buttonYio = setOnlyButtonToRespond(31, "tut_tap_to_end_turn");
                pointToMenu((double) buttonYio.x1, (double) buttonYio.y2, -2.356194490192345d);
                return;
            case 12:
                setOnlyHexToRespond(22, 5);
                for (int i = 0; i < 6; i++) {
                    getHex(22, 5).getAdjacentHex(i).setIgnoreTouch(false);
                }
                showMessage("tut_tap_on_hex");
                return;
            case 13:
                setOnlyHexToRespond(22, 4);
                this.gameController.setIgnoreMarch(false);
                showMessage("tut_hold_hex");
                return;
            case 14:
                buttonYio = setOnlyButtonToRespond(32, "tut_tap_to_undo");
                pointToMenu((double) buttonYio.x2, (double) buttonYio.y2, 2.356194490192345d);
                return;
            case 15:
                setOnlyHexToRespond(20, 8);
                showMessage("tut_select_unit");
                return;
            case 16:
                setOnlyHexToRespond(18, 8);
                showMessage("tut_merge_units");
                return;
            case 17:
                resetIgnores();
                this.menuControllerYio.getButtonById(30).setReaction(Reaction.rbPauseMenu);
                return;
            default:
                setOnlyHexToRespond(21, 5);
                showMessage("tut_select_unit");
                return;
        }
    }

    private void checkToShowTip() {
        switch (this.currentStep) {
            case 1:
                showTutorialTip("tip_about_money");
                return;
            case 3:
                showTutorialTip("tip_build_towers");
                return;
            case 5:
                showTutorialTip("tip_about_defense");
                return;
            case 8:
                showTutorialTip("tip_about_taxes");
                return;
            case 9:
                showTutorialTip("tip_about_unit_death");
                return;
            case 10:
                showTutorialTip("tip_trees");
                return;
            case 11:
                showTutorialTip("tip_hold_to_march");
                return;
            case 14:
                showTutorialTip("tip_merging");
                return;
            case 16:
                showTutorialTip("tip_help");
                Scenes.sceneTutorialTip.addHelpButtonToTutorialTip();
                return;
            default:
                return;
        }
    }

    private boolean isStepComplete() {
        switch (this.currentStep) {
            case 1:
                if (getHex(19, 7).colorIndex != 0) {
                    return false;
                }
                return true;
            case 2:
                if (this.gameController.selectionController.selectedUnit == null) {
                    return false;
                }
                return true;
            case 3:
                if (getHex(18, 8).colorIndex != 0) {
                    return false;
                }
                return true;
            case 4:
                if (this.gameController.selectionController.tipFactor.get() <= 0.0f) {
                    return false;
                }
                return true;
            case 5:
                if (getHex(21, 5).objectInside != 4) {
                    return false;
                }
                return true;
            case 6:
                if (this.gameController.selectionController.tipFactor.get() <= 0.0f) {
                    return false;
                }
                return true;
            case 7:
                if (this.gameController.selectionController.getTipType() != 2) {
                    return false;
                }
                return true;
            case 8:
                if (getHex(20, 5).colorIndex != 0) {
                    return false;
                }
                return true;
            case 9:
                if (this.menuControllerYio.getButtonById(53).isCurrentlyTouched()) {
                    return true;
                }
                return false;
            case 10:
                if (getHex(20, 5).colorIndex == 0) {
                    return false;
                }
                return true;
            case 11:
                if (this.menuControllerYio.getButtonById(31).isCurrentlyTouched()) {
                    return true;
                }
                return false;
            case 12:
                if (this.gameController.selectionController.isSomethingSelected()) {
                    return true;
                }
                return false;
            case 13:
                if (getHex(19, 8).containsUnit()) {
                    return false;
                }
                return true;
            case 14:
                if (getHex(19, 8).containsUnit()) {
                    return true;
                }
                return false;
            case 15:
                if (this.gameController.selectionController.selectedUnit == null) {
                    return false;
                }
                return true;
            case 16:
                if (getHex(18, 8).unit.strength != 2) {
                    return false;
                }
                return true;
            case 17:
                return false;
            default:
                if (this.gameController.selectionController.selectedUnit != null) {
                    return true;
                }
                return false;
        }
    }

    private ButtonYio setOnlyButtonToRespond(int id, String message) {
        allButtonsIgnoreTouches();
        allHexesIgnoreTouches();
        ButtonYio buttonYio = this.menuControllerYio.getButtonById(id);
        buttonYio.setTouchable(true);
        showMessage(message);
        return buttonYio;
    }

    private void resetIgnores() {
        int i;
        this.gameController.setIgnoreMarch(false);
        for (i = 31; i <= 32; i++) {
            this.menuControllerYio.getButtonById(i).setTouchable(true);
        }
        for (i = 38; i <= 39; i++) {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(i);
            if (buttonYio != null) {
                buttonYio.setTouchable(true);
            }
        }
        for (i = 0; i < this.gameController.fieldController.fWidth; i++) {
            for (int j = 0; j < this.gameController.fieldController.fHeight; j++) {
                this.gameController.fieldController.field[i][j].setIgnoreTouch(false);
            }
        }
    }

    private void allButtonsIgnoreTouches() {
        int i;
        for (i = 31; i <= 32; i++) {
            this.menuControllerYio.getButtonById(i).setTouchable(false);
        }
        for (i = 38; i <= 39; i++) {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(i);
            if (buttonYio != null) {
                buttonYio.setTouchable(false);
            }
        }
    }

    private void setOnlyHexToRespond(int x, int y) {
        allButtonsIgnoreTouches();
        allHexesIgnoreTouches();
        getHex(x, y).setIgnoreTouch(false);
        pointToHex(x, y);
    }

    private void allHexesIgnoreTouches() {
        for (int i = 0; i < this.gameController.fieldController.fWidth; i++) {
            for (int j = 0; j < this.gameController.fieldController.fHeight; j++) {
                this.gameController.fieldController.field[i][j].setIgnoreTouch(true);
            }
        }
    }

    public void move() {
        if (this.waitingBeforeNextStep) {
            if (this.gameController.currentTime > this.timeForNextStep && !this.tipIsCurrentlyShown) {
                this.waitingBeforeNextStep = false;
                this.timeToCheck = this.gameController.currentTime + 200;
                this.currentStep++;
                stepSetup();
            }
        } else if (this.gameController.currentTime > this.timeToCheck) {
            this.timeToCheck = this.gameController.currentTime + 200;
            if (isStepComplete()) {
                allHexesIgnoreTouches();
                allButtonsIgnoreTouches();
                this.waitingBeforeNextStep = true;
                this.timeForNextStep = this.gameController.currentTime + 500;
                Scenes.sceneNotification.hideNotification();
                this.gameController.forefinger.hide();
                checkToShowTip();
            }
        }
    }
}
