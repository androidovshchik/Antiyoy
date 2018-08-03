package io.androidovshchik.antiyoy.gameplay.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import io.androidovshchik.antiyoy.gameplay.DetectorProvince;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.GameSaver;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Unit;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class LevelEditor {
    public static final String EDITOR_PREFS = "editor";
    public static final int MODE_DELETE = 3;
    public static final int MODE_MOVE = 0;
    public static final int MODE_SET_HEX = 1;
    public static final int MODE_SET_OBJECT = 2;
    public static final String SLOT_NAME = "slot";
    public static final int TEMPORARY_SLOT_NUMBER = 12345;
    private int currentSlotNumber;
    DetectorProvince detectorProvince;
    private final EditorAutomationManager editorAutomationManager;
    private boolean filteredByOnlyLand = false;
    public GameController gameController;
    private GameSaver gameSaver;
    private int inputColor;
    private int inputMode;
    private int inputObject;
    private long lastTimeTouched;
    private boolean randomColor;
    private int scrX;
    private int scrY;
    public boolean showMoney;
    private ArrayList<Hex> tempList = new ArrayList();

    public LevelEditor(GameController gameController) {
        this.gameController = gameController;
        this.gameSaver = new GameSaver(gameController);
        this.detectorProvince = new DetectorProvince();
        this.editorAutomationManager = new EditorAutomationManager(this);
        this.showMoney = false;
    }

    private void focusedHexActions(Hex focusedHex) {
        if (focusedHex != null) {
            if (this.randomColor) {
                this.inputColor = this.gameController.random.nextInt(7);
            }
            switch (this.inputMode) {
                case 0:
                    inputModeMoveActions(focusedHex);
                    return;
                case 1:
                    inputModeHexActions(focusedHex);
                    return;
                case 2:
                    inputModeSetObjectActions(focusedHex);
                    return;
                case 3:
                    inputModeDeleteActions(focusedHex);
                    return;
                default:
                    return;
            }
        }
    }

    private void inputModeDeleteActions(Hex focusedHex) {
        if (focusedHex.active) {
            deactivateHex(focusedHex);
        }
    }

    private void inputModeSetObjectActions(Hex focusedHex) {
        if (focusedHex.active) {
            int unitStrength = 0;
            if (focusedHex.containsUnit()) {
                unitStrength = focusedHex.unit.strength;
            }
            int lastObject = focusedHex.objectInside;
            this.gameController.cleanOutHex(focusedHex);
            if (this.inputObject == 0) {
                focusedHex.setObjectInside(0);
                this.gameController.addAnimHex(focusedHex);
            } else if (this.inputObject < 5) {
                addSolidObject(focusedHex, lastObject);
                checkToTurnIntoFarm(focusedHex);
                this.gameController.addAnimHex(focusedHex);
            } else {
                tryToAddUnitToFocusedHex(focusedHex, unitStrength);
            }
        }
    }

    private void addSolidObject(Hex focusedHex, int lastObject) {
        if (!canAddObjectToHex(focusedHex)) {
            return;
        }
        if (lastObject == 4 && this.inputObject == 4) {
            this.gameController.addSolidObject(focusedHex, 7);
        } else {
            this.gameController.addSolidObject(focusedHex, this.inputObject);
        }
    }

    private boolean canAddObjectToHex(Hex hex) {
        if (!hex.isNeutral()) {
            return true;
        }
        switch (this.inputObject) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 7:
                return true;
            default:
                return false;
        }
    }

    private void checkToTurnIntoFarm(Hex srcHex) {
        if (this.inputObject == 3) {
            ArrayList<Hex> province = detectProvince(srcHex);
            if (province != null) {
                Iterator it = province.iterator();
                while (it.hasNext()) {
                    Hex hex = (Hex) it.next();
                    if (hex.objectInside == 3 && hex != srcHex) {
                        srcHex.objectInside = 6;
                        return;
                    }
                }
            }
        }
    }

    private ArrayList<Hex> detectProvince(Hex start) {
        ArrayList<Hex> province = new ArrayList();
        this.tempList.clear();
        this.tempList.add(start);
        province.add(start);
        while (this.tempList.size() > 0) {
            Hex hex = (Hex) this.tempList.get(0);
            this.tempList.remove(0);
            for (int i = 0; i < 6; i++) {
                Hex adjacentHex = hex.getAdjacentHex(i);
                if (adjacentHex.active && adjacentHex.sameColor(hex) && !province.contains(adjacentHex)) {
                    this.tempList.add(adjacentHex);
                    province.add(adjacentHex);
                }
            }
        }
        return province;
    }

    private boolean canAddUnitToHex(Hex hex) {
        if (hex.isNeutral()) {
            return false;
        }
        return true;
    }

    private void tryToAddUnitToFocusedHex(Hex focusedHex, int unitStrength) {
        if (canAddUnitToHex(focusedHex)) {
            int str = unitStrength + (this.inputObject - 4);
            while (str > 4) {
                str -= 4;
            }
            this.gameController.addUnit(focusedHex, str);
            focusedHex.unit.stopJumping();
        }
    }

    private void inputModeMoveActions(Hex focusedHex) {
    }

    private int countUpColorNumber() {
        int cn = 0;
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.colorIndex > cn) {
                cn = activeHex.colorIndex;
            }
        }
        cn++;
        if (cn > FieldController.NEUTRAL_LANDS_INDEX) {
            return FieldController.NEUTRAL_LANDS_INDEX;
        }
        return cn;
    }

    public String getFullLevelString() {
        GameRules.colorNumber = countUpColorNumber();
        this.gameController.fieldController.detectProvinces();
        return this.gameController.fieldController.getFullLevelString();
    }

    public void saveSlot() {
        String fullLevel = getFullLevelString();
        Preferences prefs = Gdx.app.getPreferences(EDITOR_PREFS);
        prefs.putString(SLOT_NAME + this.currentSlotNumber, fullLevel);
        prefs.putInteger("chosen_color" + this.currentSlotNumber, GameRules.editorChosenColor);
        prefs.putBoolean("editor_fog" + this.currentSlotNumber, GameRules.editorFog);
        prefs.putBoolean("editor_diplomacy" + this.currentSlotNumber, GameRules.editorDiplomacy);
        prefs.flush();
    }

    public void loadSlot() {
        Preferences prefs = Gdx.app.getPreferences(EDITOR_PREFS);
        String fullLevel = prefs.getString(SLOT_NAME + this.currentSlotNumber, "");
        LoadingParameters instance = LoadingParameters.getInstance();
        if (fullLevel.length() < 3) {
            instance.mode = 7;
            instance.levelSize = 4;
            instance.playersNumber = 1;
            instance.colorNumber = 5;
            instance.colorOffset = 0;
            instance.difficulty = 1;
        } else {
            instance.mode = 4;
            instance.applyFullLevel(fullLevel);
            instance.colorOffset = 0;
        }
        LoadingManager.getInstance().startGame(instance);
        GameRules.editorChosenColor = prefs.getInteger("chosen_color" + this.currentSlotNumber);
        GameRules.editorFog = prefs.getBoolean("editor_fog" + this.currentSlotNumber, false);
        GameRules.editorDiplomacy = prefs.getBoolean("editor_diplomacy" + this.currentSlotNumber, false);
        defaultValues();
    }

    private void defaultValues() {
        this.inputMode = 0;
        this.showMoney = false;
    }

    public void onAllPanelsHide() {
        this.showMoney = false;
    }

    private boolean isValidLevelString(String fullLevel) {
        if (fullLevel != null && fullLevel.contains("/") && fullLevel.contains("#") && fullLevel.length() >= 10) {
            return true;
        }
        return false;
    }

    public void importLevel() {
        String fromClipboard = "";
        fromClipboard = Gdx.app.getClipboard().getContents();
        if (isValidLevelString(fromClipboard)) {
            LoadingParameters instance = LoadingParameters.getInstance();
            instance.mode = 4;
            instance.applyFullLevel(fromClipboard);
            instance.colorOffset = 0;
            LoadingManager.getInstance().startGame(instance);
        }
    }

    public void exportLevel() {
        String fullLevel = Gdx.app.getPreferences(EDITOR_PREFS).getString(SLOT_NAME + this.currentSlotNumber, "");
        System.out.println("fullLevel = " + fullLevel);
        Gdx.app.getClipboard().setContents(fullLevel);
        Scenes.sceneNotification.showNotification("exported");
    }

    public void clearLevel() {
        for (int i = 0; i < this.gameController.fieldController.fWidth; i++) {
            for (int j = 0; j < this.gameController.fieldController.fHeight; j++) {
                deactivateHex(this.gameController.fieldController.field[i][j]);
            }
        }
    }

    private void deactivateHex(Hex hex) {
        if (hex.active) {
            this.gameController.cleanOutHex(hex);
            hex.active = false;
            ListIterator activeIterator = this.gameController.fieldController.activeHexes.listIterator();
            while (activeIterator.hasNext()) {
                if (((Hex) activeIterator.next()) == hex) {
                    activeIterator.remove();
                    break;
                }
            }
            this.gameController.addAnimHex(hex);
        }
    }

    public void playLevel() {
        Preferences prefs = Gdx.app.getPreferences(EDITOR_PREFS);
        String fullLevel = prefs.getString(SLOT_NAME + this.currentSlotNumber, "");
        if (fullLevel.length() >= 10) {
            LoadingParameters instance = LoadingParameters.getInstance();
            instance.mode = 5;
            instance.applyFullLevel(fullLevel);
            GameRules.editorChosenColor = prefs.getInteger("chosen_color" + this.currentSlotNumber);
            GameRules.editorFog = prefs.getBoolean("editor_fog" + this.currentSlotNumber, false);
            instance.fogOfWar = GameRules.editorFog;
            GameRules.editorDiplomacy = prefs.getBoolean("editor_diplomacy" + this.currentSlotNumber, false);
            instance.diplomacy = GameRules.editorDiplomacy;
            instance.colorOffset = this.gameController.getColorOffsetBySliderIndex(GameRules.editorChosenColor, 7);
            LoadingManager.getInstance().startGame(instance);
        }
    }

    public void onEndCreation() {
        Iterator it = this.gameController.unitList.iterator();
        while (it.hasNext()) {
            ((Unit) it.next()).stopJumping();
        }
        this.gameController.fieldController.provinces.clear();
    }

    private void activateHex(Hex hex, int color) {
        if (!hex.active) {
            hex.active = true;
            hex.setColorIndex(color);
            this.gameController.fieldController.activeHexes.listIterator().add(hex);
            this.gameController.addAnimHex(hex);
        }
    }

    private void inputModeHexActions(Hex focusedHex) {
        if (focusedHex.active) {
            int objectInside = focusedHex.objectInside;
            this.gameController.fieldController.setHexColor(focusedHex, this.inputColor);
            focusedHex.setObjectInside(objectInside);
        } else if (!this.filteredByOnlyLand) {
            activateHex(focusedHex, this.inputColor);
        }
    }

    private boolean updateFocusedHex() {
        Hex lastFocHex = this.gameController.fieldController.focusedHex;
        this.gameController.selectionController.updateFocusedHex(this.scrX, this.scrY);
        if (this.gameController.fieldController.focusedHex == lastFocHex) {
            return false;
        }
        return true;
    }

    public boolean isSomethingMoving() {
        if (this.inputMode != 1) {
            return false;
        }
        if (this.gameController.currentTime < this.lastTimeTouched + 50) {
            return true;
        }
        return false;
    }

    public boolean touchDown(int x, int y) {
        this.scrX = x;
        this.scrY = y;
        this.lastTimeTouched = this.gameController.currentTime;
        updateFocusedHex();
        if (this.inputMode == 2 || this.inputMode == 1 || this.inputMode == 3) {
            focusedHexActions(this.gameController.fieldController.focusedHex);
        }
        return isTouchCaptured();
    }

    public boolean touchUp(int x, int y) {
        this.scrX = x;
        this.scrY = y;
        this.lastTimeTouched = this.gameController.currentTime;
        return isTouchCaptured();
    }

    public boolean touchDrag(int x, int y) {
        this.scrX = x;
        this.scrY = y;
        this.lastTimeTouched = this.gameController.currentTime;
        if (!updateFocusedHex()) {
            return isTouchCaptured();
        }
        if (this.inputMode == 1 || this.inputMode == 3) {
            focusedHexActions(this.gameController.fieldController.focusedHex);
        }
        return isTouchCaptured();
    }

    private boolean isTouchCaptured() {
        return this.inputMode != 0;
    }

    public void placeObject(Hex hex, int type) {
        this.gameController.cleanOutHex(hex);
        this.gameController.addSolidObject(hex, type);
    }

    public void expandProvinces() {
        this.editorAutomationManager.expandProvinces();
    }

    public void expandTrees() {
        this.editorAutomationManager.expandTrees();
    }

    public void placeCapitalsOrFarms() {
        this.editorAutomationManager.placeCapitalsOrFarms();
    }

    public void placeRandomTowers() {
        this.editorAutomationManager.placeRandomTowers();
    }

    public void cutExcessStuff() {
        this.editorAutomationManager.cutExcessStuff();
    }

    private LanguagesManager getLangManager() {
        return LanguagesManager.getInstance();
    }

    public void randomize() {
        this.gameSaver.detectRules();
        GameRules.setColorNumber(countUpColorNumber());
        this.gameController.fieldController.clearField();
        this.gameController.fieldController.createFieldMatrix();
        if (GameRules.slayRules) {
            this.gameController.mapGeneratorSlay.generateMap(this.gameController.random, this.gameController.fieldController.field);
        } else {
            this.gameController.mapGeneratorGeneric.generateMap(this.gameController.random, this.gameController.fieldController.field);
        }
        this.gameController.yioGdxGame.gameView.updateCacheLevelTextures();
        resetInputMode();
    }

    public void resetInputMode() {
        setInputMode(0);
    }

    public void setInputMode(int inputMode) {
        this.inputMode = inputMode;
    }

    public void setInputColor(int inputColor) {
        this.inputColor = inputColor;
        setRandomColor(false);
        if (inputColor >= 7 && inputColor != FieldController.NEUTRAL_LANDS_INDEX) {
            setRandomColor(true);
        }
    }

    public void switchFilterOnlyLand() {
        setFilteredByOnlyLand(!this.filteredByOnlyLand);
    }

    private void updateTextOnFilterOnlyLandButton(ButtonYio filterButton) {
        if (this.filteredByOnlyLand) {
            filterButton.setTextLine(getLangManager().getString("filter_only_land"));
        } else {
            filterButton.setTextLine(getLangManager().getString("filter_no"));
        }
    }

    public void launchEditCampaignLevelMode() {
        if (!GameRules.inEditorMode) {
            System.out.println("opened campaign level in editor: " + CampaignProgressManager.getInstance().currentLevelIndex);
            GameRules.inEditorMode = true;
            this.currentSlotNumber = TEMPORARY_SLOT_NUMBER;
            saveSlot();
            Scenes.sceneEditorActions.create();
        }
    }

    public void updateFilterOnlyLandButton() {
        MenuControllerYio menuControllerYio = this.gameController.yioGdxGame.menuControllerYio;
        ButtonYio filterButton = menuControllerYio.getButtonById(12353);
        if (filterButton != null) {
            updateTextOnFilterOnlyLandButton(filterButton);
            menuControllerYio.buttonRenderer.renderButton(filterButton);
        }
    }

    public void setInputObject(int inputObject) {
        this.inputObject = inputObject;
    }

    private void setRandomColor(boolean randomColor) {
        this.randomColor = randomColor;
    }

    public void setCurrentSlotNumber(int currentSlotNumber) {
        this.currentSlotNumber = currentSlotNumber;
    }

    public int getCurrentSlotNumber() {
        return this.currentSlotNumber;
    }

    public boolean isFilteredByOnlyLand() {
        return this.filteredByOnlyLand;
    }

    public void setFilteredByOnlyLand(boolean filteredByOnlyLand) {
        this.filteredByOnlyLand = filteredByOnlyLand;
        updateFilterOnlyLandButton();
    }
}
