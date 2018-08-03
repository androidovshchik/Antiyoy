package io.androidovshchik.antiyoy.gameplay;

import java.util.Iterator;
import yio.tro.antiyoy.gameplay.loading.LoadingManager;
import yio.tro.antiyoy.gameplay.loading.LoadingParameters;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.stuff.LanguagesManager;

public class TutorialScriptGenericRules extends TutorialScript {
    public static final int STEP_ABOUT_FARM = 7;
    public static final int STEP_ABOUT_TOWERS = 13;
    public static final int STEP_ABOUT_TREES_ONE = 19;
    public static final int STEP_ABOUT_TREES_TWO = 20;
    public static final int STEP_BUILD_FARM = 6;
    public static final int STEP_BUILD_PEASANT = 9;
    public static final int STEP_BUILD_SPEARMAN = 15;
    public static final int STEP_BUILD_TOWER = 12;
    public static final int STEP_CAPTURE_PEASANT = 10;
    public static final int STEP_CHOOSE_FARM = 5;
    public static final int STEP_CHOOSE_PEASANT = 8;
    public static final int STEP_CHOOSE_SPEARMAN = 14;
    public static final int STEP_CHOOSE_TOWER = 11;
    public static final int STEP_CHOOSE_TO_MERGE = 22;
    public static final int STEP_END_TURN = 21;
    public static final int STEP_GOODBYE = 26;
    public static final int STEP_GREETINGS = 0;
    public static final int STEP_INCOME = 4;
    public static final int STEP_LONG_TAP = 24;
    public static final int STEP_MERGE_UNITS = 23;
    public static final int STEP_MOVE_UNIT = 3;
    public static final int STEP_SELECT_PROVINCE = 1;
    public static final int STEP_SELECT_UNIT = 2;
    public static final int STEP_SEVERAL_PROVINCES = 25;
    public static final int STEP_UNDO_SPEARMAN = 17;
    public static final int STEP_UNITS_CONSUME_MONEY = 16;
    public static final int STEP_WHY_UNITS_DIE = 18;
    int currentStep;
    LanguagesManager languagesManager;
    final String map = "11 7 7 0 0 0 10#11 8 7 0 0 0 10#12 2 1 0 0 0 6#12 3 1 3 0 0 6#12 6 7 0 0 0 10#13 2 1 0 1 0 6#13 3 1 0 0 0 6#13 5 7 0 0 0 10#14 2 1 0 0 0 6#14 3 7 0 0 0 10#14 4 7 0 0 0 10#14 5 7 0 0 0 10#15 2 7 0 0 0 10#15 3 7 0 0 0 10#15 4 7 0 0 0 10#15 5 7 0 0 0 10#15 10 7 0 0 0 10#15 11 7 0 0 0 10#16 3 7 0 0 0 10#16 4 7 0 0 0 10#16 5 7 0 0 0 10#16 6 7 0 0 0 10#16 9 7 0 0 0 10#16 10 7 0 0 0 10#16 11 0 0 2 1 58#17 2 7 2 0 0 10#17 3 7 0 0 0 10#17 4 7 0 0 0 10#17 5 7 0 0 0 10#17 6 7 0 0 0 10#17 7 7 0 0 0 10#17 8 7 0 0 0 10#17 9 7 0 0 0 10#17 10 0 0 0 0 58#17 11 0 0 0 0 58#18 2 7 0 0 0 10#18 4 7 0 0 0 10#18 5 7 0 0 0 10#18 6 7 0 0 0 10#18 7 7 0 0 0 10#18 8 7 0 0 0 10#18 9 0 0 1 1 58#18 10 0 0 0 0 58#18 11 0 0 0 0 58#19 6 7 0 0 0 10#19 7 7 0 0 0 10#19 8 7 1 0 0 10#19 9 0 0 0 0 58#19 10 0 0 0 0 58#19 11 0 3 0 0 58#20 5 7 2 0 0 10#20 6 7 0 0 0 10#20 7 7 0 0 0 10#20 8 7 1 0 0 10#20 9 7 0 0 0 10#20 11 0 2 0 0 58#21 2 7 0 0 0 10#21 4 7 2 0 0 10#21 5 7 2 0 0 10#21 6 7 0 0 0 10#21 7 7 2 0 0 10#21 8 7 2 0 0 10#22 2 7 0 0 0 10#22 3 7 0 0 0 10#22 7 7 0 0 0 10#22 8 7 0 0 0 10#22 9 7 0 0 0 10#23 2 7 0 0 0 10#23 7 7 0 0 0 10";
    long timeForNextStep;
    long timeToCheck;
    boolean waitingBeforeNextStep;

    public TutorialScriptGenericRules(GameController gameController) {
        super(gameController);
    }

    public void createTutorialGame() {
        LoadingParameters instance = LoadingParameters.getInstance();
        instance.mode = 0;
        instance.activeHexes = "11 7 7 0 0 0 10#11 8 7 0 0 0 10#12 2 1 0 0 0 6#12 3 1 3 0 0 6#12 6 7 0 0 0 10#13 2 1 0 1 0 6#13 3 1 0 0 0 6#13 5 7 0 0 0 10#14 2 1 0 0 0 6#14 3 7 0 0 0 10#14 4 7 0 0 0 10#14 5 7 0 0 0 10#15 2 7 0 0 0 10#15 3 7 0 0 0 10#15 4 7 0 0 0 10#15 5 7 0 0 0 10#15 10 7 0 0 0 10#15 11 7 0 0 0 10#16 3 7 0 0 0 10#16 4 7 0 0 0 10#16 5 7 0 0 0 10#16 6 7 0 0 0 10#16 9 7 0 0 0 10#16 10 7 0 0 0 10#16 11 0 0 2 1 58#17 2 7 2 0 0 10#17 3 7 0 0 0 10#17 4 7 0 0 0 10#17 5 7 0 0 0 10#17 6 7 0 0 0 10#17 7 7 0 0 0 10#17 8 7 0 0 0 10#17 9 7 0 0 0 10#17 10 0 0 0 0 58#17 11 0 0 0 0 58#18 2 7 0 0 0 10#18 4 7 0 0 0 10#18 5 7 0 0 0 10#18 6 7 0 0 0 10#18 7 7 0 0 0 10#18 8 7 0 0 0 10#18 9 0 0 1 1 58#18 10 0 0 0 0 58#18 11 0 0 0 0 58#19 6 7 0 0 0 10#19 7 7 0 0 0 10#19 8 7 1 0 0 10#19 9 0 0 0 0 58#19 10 0 0 0 0 58#19 11 0 3 0 0 58#20 5 7 2 0 0 10#20 6 7 0 0 0 10#20 7 7 0 0 0 10#20 8 7 1 0 0 10#20 9 7 0 0 0 10#20 11 0 2 0 0 58#21 2 7 0 0 0 10#21 4 7 2 0 0 10#21 5 7 2 0 0 10#21 6 7 0 0 0 10#21 7 7 2 0 0 10#21 8 7 2 0 0 10#22 2 7 0 0 0 10#22 3 7 0 0 0 10#22 7 7 0 0 0 10#22 8 7 0 0 0 10#22 9 7 0 0 0 10#23 2 7 0 0 0 10#23 7 7 0 0 0 10";
        instance.playersNumber = 1;
        instance.colorNumber = 5;
        instance.levelSize = 1;
        instance.difficulty = 0;
        instance.colorOffset = 0;
        instance.slayRules = false;
        LoadingManager.getInstance().startGame(instance);
        this.menuControllerYio = this.gameController.yioGdxGame.menuControllerYio;
        this.languagesManager = LanguagesManager.getInstance();
        this.currentStep = -1;
        this.waitingBeforeNextStep = true;
        ignoreAll();
        showTutorialTip("gen_greetings");
        this.menuControllerYio.getButtonById(30).setReaction(Reaction.rbChooseGameModeMenu);
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
        Scenes.sceneTutorialTip.createTutorialTipWithFixedHeight(this.menuControllerYio.getArrayListFromString(this.languagesManager.getString(key)), 6);
        this.tipIsCurrentlyShown = true;
    }

    private void stepSetup() {
        this.gameController.setIgnoreMarch(true);
        ButtonYio buttonYio;
        switch (this.currentStep) {
            case 1:
                setHexToRespondByColor(0);
                pointToHex(19, 11);
                return;
            case 2:
                setOnlyHexToRespond(18, 9);
                return;
            case 3:
                setOnlyHexToRespond(17, 9);
                return;
            case 4:
                ignoreAll();
                return;
            case 5:
                buttonYio = setOnlyButtonToRespond(38, "gen_press_button");
                pointToMenu((double) buttonYio.x1, (double) buttonYio.y2, -2.356194490192345d);
                return;
            case 6:
                setOnlyHexToRespond(18, 11);
                return;
            case 7:
                ignoreAll();
                return;
            case 8:
                buttonYio = setOnlyButtonToRespond(39, "gen_press_button");
                pointToMenu((double) buttonYio.x2, (double) buttonYio.y2, 2.356194490192345d);
                return;
            case 9:
                setOnlyHexToRespond(19, 9);
                return;
            case 10:
                ignoreAll();
                getHex(19, 9).setIgnoreTouch(false);
                getHex(20, 9).setIgnoreTouch(false);
                pointToHex(20, 9);
                showMessage("gen_capture_hex");
                return;
            case 11:
                buttonYio = setOnlyButtonToRespond(38, "gen_press_button_twice");
                pointToMenu((double) buttonYio.x1, (double) buttonYio.y2, -2.356194490192345d);
                return;
            case 12:
                setOnlyHexToRespond(18, 10);
                return;
            case 13:
                ignoreAll();
                return;
            case 14:
                buttonYio = setOnlyButtonToRespond(39, "gen_press_button_twice");
                pointToMenu((double) buttonYio.x2, (double) buttonYio.y2, 2.356194490192345d);
                return;
            case 15:
                setOnlyHexToRespond(19, 8);
                return;
            case 16:
                ignoreAll();
                return;
            case 17:
                buttonYio = setOnlyButtonToRespond(32, "tut_tap_to_undo");
                pointToMenu((double) buttonYio.x2, (double) buttonYio.y2, 2.356194490192345d);
                return;
            case 18:
                ignoreAll();
                return;
            case 19:
                ignoreAll();
                return;
            case 20:
                ignoreAll();
                return;
            case 21:
                buttonYio = setOnlyButtonToRespond(31, "tut_tap_to_end_turn");
                pointToMenu((double) buttonYio.x1, (double) buttonYio.y2, -2.356194490192345d);
                return;
            case 22:
                setOnlyHexToRespond(17, 9);
                return;
            case 23:
                setOnlyHexToRespond(20, 9);
                return;
            case 24:
                ignoreAll();
                return;
            case 25:
                ignoreAll();
                return;
            case 26:
                resetIgnores();
                this.menuControllerYio.getButtonById(30).setReaction(Reaction.rbPauseMenu);
                return;
            default:
                return;
        }
    }

    private void ignoreAll() {
        allButtonsIgnoreTouches();
        allHexesIgnoreTouches();
    }

    private void checkToShowTip() {
        switch (this.currentStep + 1) {
            case 1:
                showTutorialTip("gen_select_province");
                return;
            case 2:
                showTutorialTip("gen_select_unit");
                return;
            case 3:
                showTutorialTip("gen_move_unit");
                return;
            case 4:
                showTutorialTip("gen_income");
                return;
            case 5:
                showTutorialTip("gen_lets_build_farm");
                return;
            case 6:
                showTutorialTip("gen_build_farm");
                return;
            case 7:
                showTutorialTip("gen_about_farms");
                return;
            case 8:
                showTutorialTip("gen_lets_build_peasant");
                return;
            case 9:
                showTutorialTip("gen_build_peasant");
                return;
            case 10:
                showTutorialTip("gen_capture_peasant");
                return;
            case 11:
                showTutorialTip("gen_lets_build_tower");
                return;
            case 12:
                showTutorialTip("gen_build_tower");
                return;
            case 13:
                showTutorialTip("gen_about_towers");
                return;
            case 14:
                showTutorialTip("gen_lets_build_spearman");
                return;
            case 15:
                showTutorialTip("gen_build_spearman");
                return;
            case 16:
                showTutorialTip("gen_units_consume_money");
                return;
            case 17:
                showTutorialTip("gen_undo_spearman");
                return;
            case 18:
                showTutorialTip("gen_why_units_die");
                return;
            case 19:
                showTutorialTip("gen_about_trees_one");
                return;
            case 20:
                showTutorialTip("gen_about_trees_two");
                return;
            case 21:
                showTutorialTip("gen_end_turn");
                return;
            case 22:
                showTutorialTip("gen_choose_to_merge");
                return;
            case 23:
                showTutorialTip("gen_merge_units");
                return;
            case 24:
                showTutorialTip("gen_long_tap");
                return;
            case 25:
                showTutorialTip("gen_several_provinces");
                return;
            case 26:
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
                if (this.gameController.fieldController.selectedProvince == null) {
                    return false;
                }
                return true;
            case 2:
                if (this.gameController.selectionController.selectedUnit == null) {
                    return false;
                }
                return true;
            case 3:
                if (getHex(17, 9).containsUnit()) {
                    return true;
                }
                return false;
            case 5:
                if (this.gameController.selectionController.getTipType() != 5) {
                    return false;
                }
                return true;
            case 6:
                if (getHex(18, 11).objectInside != 6) {
                    return false;
                }
                return true;
            case 8:
                if (this.gameController.selectionController.getTipType() != 1) {
                    return false;
                }
                return true;
            case 9:
                if (getHex(19, 9).containsUnit()) {
                    return true;
                }
                return false;
            case 10:
                if (getHex(20, 9).containsUnit()) {
                    return true;
                }
                return false;
            case 11:
                if (this.gameController.selectionController.getTipType() != 0) {
                    return false;
                }
                return true;
            case 12:
                if (getHex(18, 10).objectInside != 4) {
                    return false;
                }
                return true;
            case 14:
                if (this.gameController.selectionController.getTipType() != 2) {
                    return false;
                }
                return true;
            case 15:
                if (getHex(19, 8).containsUnit()) {
                    return true;
                }
                return false;
            case 17:
                if (getHex(19, 8).containsUnit()) {
                    return false;
                }
                return true;
            case 21:
                if (this.menuControllerYio.getButtonById(31).isCurrentlyTouched()) {
                    return true;
                }
                return false;
            case 22:
                if (this.gameController.selectionController.selectedUnit == null) {
                    return false;
                }
                return true;
            case 23:
                if (getHex(20, 9).unit.strength != 2) {
                    return false;
                }
                return true;
            case 26:
                return false;
            default:
                return true;
        }
    }

    private ButtonYio setOnlyButtonToRespond(int id, String message) {
        ignoreAll();
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
        ignoreAll();
        getHex(x, y).setIgnoreTouch(false);
        pointToHex(x, y);
    }

    private void setHexToRespondByColor(int color) {
        ignoreAll();
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.colorIndex == color && !activeHex.containsUnit()) {
                activeHex.setIgnoreTouch(false);
            }
        }
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
