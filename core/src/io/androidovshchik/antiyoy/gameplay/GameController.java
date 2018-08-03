package io.androidovshchik.antiyoy.gameplay;

import com.badlogic.gdx.net.HttpStatus;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.ai.AiFactory;
import io.androidovshchik.antiyoy.ai.ArtificialIntelligence;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.gameplay.editor.LevelEditor;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.replays.ReplayManager;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.gameplay.rules.Ruleset;
import io.androidovshchik.antiyoy.gameplay.rules.RulesetGeneric;
import io.androidovshchik.antiyoy.gameplay.rules.RulesetSlay;
import io.androidovshchik.antiyoy.gameplay.user_levels.UserLevelProgressManager;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.Yio;

public class GameController {
    public final AiFactory aiFactory;
    private ArrayList<ArtificialIntelligence> aiList;
    public boolean backgroundVisible;
    public String balanceString;
    public float boundHeight;
    public float boundWidth;
    public CameraController cameraController;
    public boolean checkToMarch;
    private boolean cityNamesEnabled;
    ClickDetector clickDetector;
    public int colorIndexViewOffset;
    public String currentPriceString;
    public long currentTime;
    public int currentTouchCount;
    private final DebugActionsManager debugActionsManager;
    public final FieldController fieldController;
    public Forefinger forefinger;
    public GameSaver gameSaver;
    public boolean ignoreMarch;
    LoadingParameters initialParameters;
    public Unit jumperUnit;
    public boolean letsUpdateCacheByAnim;
    private LevelEditor levelEditor;
    public MapGenerator mapGeneratorGeneric;
    public MapGenerator mapGeneratorSlay;
    public int marchDelay;
    public MatchStatistics matchStatistics;
    public int playersNumber;
    public Random predictableRandom = new Random(0);
    public float priceStringWidth;
    private boolean proposedSurrender;
    public Random random = new Random();
    boolean readyToEndTurn;
    public ReplayManager replayManager;
    public Ruleset ruleset;
    public final SelectionController selectionController;
    public SnapshotManager snapshotManager;
    public SpeedManager speedManager;
    private long timeToUpdateCache;
    public PointYio touchPoint;
    public int turn;
    public TutorialScript tutorialScript;
    public ArrayList<Unit> unitList;
    public boolean updateWholeCache;
    public YioGdxGame yioGdxGame;

    public GameController(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        CampaignProgressManager.getInstance();
        this.selectionController = new SelectionController(this);
        this.marchDelay = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        this.cameraController = new CameraController(this);
        this.unitList = new ArrayList();
        this.mapGeneratorSlay = new MapGenerator(this);
        this.mapGeneratorGeneric = new MapGeneratorGeneric(this);
        this.aiList = new ArrayList();
        this.initialParameters = new LoadingParameters();
        this.touchPoint = new PointYio();
        this.snapshotManager = new SnapshotManager(this);
        this.fieldController = new FieldController(this);
        this.jumperUnit = new Unit(this, this.fieldController.emptyHex, 0);
        this.speedManager = new SpeedManager(this);
        this.replayManager = new ReplayManager(this);
        this.matchStatistics = new MatchStatistics();
        this.gameSaver = new GameSaver(this);
        this.forefinger = new Forefinger(this);
        this.levelEditor = new LevelEditor(this);
        this.aiFactory = new AiFactory(this);
        this.debugActionsManager = new DebugActionsManager(this);
        this.clickDetector = new ClickDetector();
        LoadingManager.getInstance().setGameController(this);
    }

    public void clearLevel() {
        if (GameRules.inEditorMode) {
            this.levelEditor.clearLevel();
        }
    }

    void takeAwaySomeMoneyToAchieveBalance() {
        updateRuleset();
        Iterator it = this.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() != 0) {
                province.money -= province.getIncome() - province.getTaxes();
            }
        }
    }

    private void checkForAloneUnits() {
        int i = 0;
        while (i < this.unitList.size()) {
            Unit unit = (Unit) this.unitList.get(i);
            if (isCurrentTurn(unit.getColor()) && unit.currentHex.numberOfFriendlyHexesNearby() == 0) {
                this.fieldController.killUnitByStarvation(unit.currentHex);
                i--;
            }
            i++;
        }
    }

    private void checkForBankrupts() {
        Iterator it = this.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (isCurrentTurn(province.getColor()) && province.money < 0) {
                province.money = 0;
                this.fieldController.killEveryoneByStarvation(province);
            }
        }
    }

    public void move() {
        this.currentTime = System.currentTimeMillis();
        this.matchStatistics.increaseTimeCount();
        this.cameraController.move();
        checkForAiToMove();
        checkToEndTurn();
        checkToUpdateCacheByAnim();
        if (this.fieldController.letsCheckAnimHexes && this.currentTime > this.fieldController.timeToCheckAnimHexes && doesCurrentTurnEndDependOnAnimHexes()) {
            this.fieldController.checkAnimHexes();
        }
        moveCheckToMarch();
        moveUnits();
        this.fieldController.moveAnimHexes();
        this.selectionController.moveSelections();
        this.jumperUnit.moveJumpAnim();
        this.fieldController.moveZoneFactor.move();
        this.selectionController.getBlackoutFactor().move();
        this.selectionController.moveDefenseTips();
        if (this.fieldController.moveZone.size() > 0 && ((double) this.fieldController.moveZoneFactor.get()) < 0.01d) {
            this.fieldController.clearMoveZone();
        }
        this.selectionController.tipFactor.move();
        this.fieldController.moveResponseAnimHex();
        moveTutorialStuff();
    }

    private void moveTutorialStuff() {
        if (GameRules.tutorialMode) {
            this.forefinger.move();
            this.tutorialScript.move();
        }
    }

    private void checkForAiToMove() {
        if (!this.readyToEndTurn) {
            if (GameRules.replayMode) {
                this.replayManager.performStep();
            } else if (!isPlayerTurn()) {
                ((ArtificialIntelligence) this.aiList.get(this.turn)).makeMove();
                updateCacheOnceAfterSomeTime();
                applyReadyToEndTurn();
            }
        }
    }

    public void applyReadyToEndTurn() {
        this.readyToEndTurn = true;
    }

    public void onInitialSnapshotRecreated() {
        this.turn = 0;
        this.readyToEndTurn = false;
        prepareCertainUnitsToMove();
    }

    private void moveUnits() {
        Iterator it = this.unitList.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            unit.moveJumpAnim();
            unit.move();
        }
    }

    public void setIgnoreMarch(boolean ignoreMarch) {
        this.ignoreMarch = ignoreMarch;
    }

    private void moveCheckToMarch() {
        if (Settings.longTapToMove && !this.ignoreMarch && this.checkToMarch && checkConditionsToMarch()) {
            this.checkToMarch = false;
            this.fieldController.updateFocusedHex();
            this.selectionController.setSelectedUnit(null);
            if (this.fieldController.focusedHex != null && this.fieldController.focusedHex.active) {
                this.fieldController.marchUnitsToHex(this.fieldController.focusedHex);
            }
        }
    }

    boolean checkConditionsToMarch() {
        if (this.currentTouchCount == 1 && this.currentTime - this.cameraController.touchDownTime > ((long) this.marchDelay) && this.cameraController.touchedAsClick()) {
            return true;
        }
        return false;
    }

    private void checkToUpdateCacheByAnim() {
        if (this.letsUpdateCacheByAnim && this.currentTime > this.timeToUpdateCache && doesCurrentTurnEndDependOnAnimHexes() && !isSomethingMoving()) {
            this.letsUpdateCacheByAnim = false;
            updateCache();
            updateFogOfWar();
            this.updateWholeCache = false;
        }
    }

    private void updateFogOfWar() {
        if (GameRules.fogOfWarEnabled) {
            this.fieldController.fogOfWarManager.updateFog();
        }
    }

    private void updateCache() {
        if (this.updateWholeCache) {
            this.yioGdxGame.gameView.updateCacheLevelTextures();
        } else {
            this.yioGdxGame.gameView.updateCacheNearAnimHexes();
        }
    }

    private boolean canEndTurn() {
        boolean z = true;
        if (isInEditorMode() || !this.readyToEndTurn || !this.cameraController.checkConditionsToEndTurn() || this.speedManager.getSpeed() == 0) {
            return false;
        }
        if (!doesCurrentTurnEndDependOnAnimHexes()) {
            return true;
        }
        if (this.fieldController.animHexes.size() != 0) {
            z = false;
        }
        return z;
    }

    private boolean doesCurrentTurnEndDependOnAnimHexes() {
        if (!isPlayerTurn() && this.speedManager.getSpeed() == 2) {
            return isCurrentTurn(0);
        }
        return true;
    }

    public boolean haveToAskToEndTurn() {
        if (!GameRules.tutorialMode && Settings.askToEndTurn && this.fieldController.atLeastOneUnitIsReadyToMove()) {
            return true;
        }
        return false;
    }

    private void checkToEndTurn() {
        if (canEndTurn()) {
            this.readyToEndTurn = false;
            endTurnActions();
            this.turn = getNextTurnIndex();
            turnStartActions();
        }
    }

    public void prepareCertainUnitsToMove() {
        Iterator it = this.unitList.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (isCurrentTurn(unit.getColor())) {
                unit.setReadyToMove(true);
                unit.startJumping();
            }
        }
    }

    public void stopAllUnitsFromJumping() {
        Iterator it = this.unitList.iterator();
        while (it.hasNext()) {
            ((Unit) it.next()).stopJumping();
        }
    }

    private int checkIfWeHaveWinner() {
        if (this.fieldController.activeHexes.size() == 0) {
            return -1;
        }
        if (GameRules.diplomacyEnabled) {
            return this.fieldController.diplomacyManager.getDiplomaticWinner();
        }
        if (this.fieldController.numberOfActiveProvinces() != 1) {
            return -1;
        }
        Iterator it = this.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (!((Hex) province.hexList.get(0)).isNeutral()) {
                return province.getColor();
            }
        }
        System.out.println("Problem in GameController.checkIfWeHaveWinner()");
        return -1;
    }

    private int zeroesInArray(int[] array) {
        int zeroes = 0;
        for (int i : array) {
            if (i == 0) {
                zeroes++;
            }
        }
        return zeroes;
    }

    private void checkToEndGame() {
        if (!GameRules.replayMode) {
            int winner = checkIfWeHaveWinner();
            if (winner >= 0) {
                endGame(winner);
            } else {
                checkToProposeSurrender();
            }
        }
    }

    private void checkToProposeSurrender() {
        if (!GameRules.diplomacyEnabled && this.playersNumber == 1 && !this.proposedSurrender) {
            int possibleWinner = this.fieldController.possibleWinner();
            if (possibleWinner >= 0 && isPlayerTurn(possibleWinner)) {
                Scenes.sceneSurrenderDialog.create();
                this.proposedSurrender = true;
            }
        }
    }

    private int indexOfNumberInArray(int[] array, int number) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == number) {
                return i;
            }
        }
        return -1;
    }

    public static int maxNumberFromArray(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public void forceGameEnd() {
        int[] playerHexCount = this.fieldController.getPlayerHexCount();
        int maxNumber = maxNumberFromArray(playerHexCount);
        int maxColor = 0;
        for (int i = 0; i < playerHexCount.length; i++) {
            if (maxNumber == playerHexCount[i]) {
                maxColor = i;
                break;
            }
        }
        this.fieldController.provinces.clear();
        ArrayList<Hex> hexList = new ArrayList();
        Iterator it = this.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.colorIndex == maxColor) {
                hexList.add(activeHex);
                break;
            }
        }
        this.fieldController.provinces.add(new Province(this, hexList));
        checkToEndGame();
    }

    private void endGame(int winColor) {
        onGameFinished(winColor);
        Scenes.sceneAfterGameMenu.create(winColor, isPlayerTurn(winColor));
    }

    private void onGameFinished(int winColor) {
        checkToTagCampaignLevel(winColor);
        checkToTagUserLevel(winColor);
    }

    private void checkToTagUserLevel(int winColor) {
        if (isPlayerTurn(winColor)) {
            String key = GameRules.ulKey;
            if (key != null) {
                UserLevelProgressManager.getInstance().onLevelCompleted(key);
            }
        }
    }

    private void checkToTagCampaignLevel(int winColor) {
        CampaignProgressManager instance = CampaignProgressManager.getInstance();
        if (instance.completedCampaignLevel(winColor)) {
            instance.markLevelAsCompleted(instance.currentLevelIndex);
            Scenes.sceneCampaignMenu.updateLevelSelector();
        }
    }

    public void resetCurrentTouchCount() {
        this.currentTouchCount = 0;
    }

    public void resetProgress() {
        CampaignProgressManager.getInstance().resetProgress();
        this.yioGdxGame.selectedLevelIndex = 1;
    }

    private void endTurnActions() {
        checkToEndGame();
        this.ruleset.onTurnEnd();
        this.replayManager.onTurnEnded();
        this.fieldController.diplomacyManager.onTurnEnded();
        Iterator it = this.unitList.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            unit.setReadyToMove(false);
            unit.stopJumping();
        }
    }

    private void turnStartActions() {
        this.selectionController.deselectAll();
        if (isCurrentTurn(0)) {
            this.matchStatistics.turnWasMade();
            this.fieldController.expandTrees();
        }
        prepareCertainUnitsToMove();
        this.fieldController.transformGraves();
        collectTributesAndPayTaxes();
        checkForStarvation();
        checkToUpdateCacheTextures();
        if (isPlayerTurn()) {
            resetCurrentTouchCount();
            this.snapshotManager.onTurnStart();
            this.jumperUnit.startJumping();
            checkToSkipTurn();
            this.fieldController.fogOfWarManager.updateFog();
        } else {
            Iterator it = this.fieldController.animHexes.iterator();
            while (it.hasNext()) {
                ((Hex) it.next()).animFactor.setValues(1.0d, 0.0d);
            }
        }
        this.fieldController.diplomacyManager.onTurnStarted();
        checkToAutoSave();
    }

    private void checkToUpdateCacheTextures() {
        if (isCurrentTurn(0)) {
            this.yioGdxGame.gameView.updateCacheLevelTextures();
        }
    }

    private void checkToSkipTurn() {
        if (this.fieldController.numberOfProvincesWithColor(this.turn) == 0) {
            onEndTurnButtonPressed();
        }
    }

    private void checkForStarvation() {
        if (!GameRules.replayMode) {
            checkForBankrupts();
            checkForAloneUnits();
        }
    }

    private void checkToAutoSave() {
        if (Settings.autosave && this.turn == 0 && this.playersNumber > 0) {
            performAutosave();
        }
    }

    private void collectTributesAndPayTaxes() {
        Iterator it = this.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (isCurrentTurn(province.getColor())) {
                province.money += province.getBalance();
            }
        }
    }

    void updateCacheOnceAfterSomeTime() {
        this.letsUpdateCacheByAnim = true;
        this.timeToUpdateCache = System.currentTimeMillis() + 30;
    }

    public void onEndTurnButtonPressed() {
        this.cameraController.onEndTurnButtonPressed();
        if (!isPlayerTurn()) {
            return;
        }
        if (isInMultiplayerMode()) {
            endTurnInMultiplayerMode();
        } else {
            applyReadyToEndTurn();
        }
    }

    private void endTurnInMultiplayerMode() {
        if (Settings.askToEndTurn) {
            Scenes.sceneTurnStartDialog.create();
            int nextTurnIndex = this.turn;
            while (this.fieldController.hasAtLeastOneProvince()) {
                nextTurnIndex = getNextTurnIndex(nextTurnIndex);
                if (isPlayerTurn(nextTurnIndex) && this.fieldController.numberOfProvincesWithColor(nextTurnIndex) > 0) {
                    break;
                }
            }
            Scenes.sceneTurnStartDialog.dialog.setColor(getColorIndexWithOffset(nextTurnIndex));
            return;
        }
        applyReadyToEndTurn();
    }

    public boolean isInMultiplayerMode() {
        return this.playersNumber > 1;
    }

    public void defaultValues() {
        this.cameraController.defaultValues();
        this.ignoreMarch = false;
        this.readyToEndTurn = false;
        this.fieldController.defaultValues();
        this.selectionController.setSelectedUnit(null);
        this.turn = 0;
        this.jumperUnit.startJumping();
        this.matchStatistics.defaultValues();
        this.speedManager.defaultValues();
        this.replayManager.defaultValues();
        GameRules.defaultValues();
        this.proposedSurrender = false;
        this.backgroundVisible = true;
        this.colorIndexViewOffset = 0;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public void initTutorial() {
        Settings.fastConstruction = false;
        if (GameRules.slayRules) {
            this.tutorialScript = new TutorialScriptSlayRules(this);
        } else {
            this.tutorialScript = new TutorialScriptGenericRules(this);
        }
        this.tutorialScript.createTutorialGame();
        GameRules.tutorialMode = true;
    }

    public int getColorOffsetBySliderIndex(int colorOffsetSliderIndex, int colorNumber) {
        if (colorOffsetSliderIndex == 0) {
            return this.predictableRandom.nextInt(colorNumber);
        }
        return colorOffsetSliderIndex - 1;
    }

    public void onEndCreation() {
        this.snapshotManager.clear();
        this.fieldController.createPlayerHexCount();
        updateRuleset();
        createCamera();
        this.fieldController.onEndCreation();
        this.aiFactory.createAiList(GameRules.difficulty);
        this.selectionController.deselectAll();
        this.replayManager.onEndCreation();
    }

    public void checkToEnableAiOnlyMode() {
        if (this.playersNumber == 0) {
            GameRules.aiOnlyMode = true;
        }
    }

    public void updateInitialParameters(LoadingParameters parameters) {
        this.initialParameters.copyFrom(parameters);
    }

    private void sayArray(int[] array) {
        System.out.print("[ ");
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println("]");
    }

    public void createCamera() {
        this.cameraController.createCamera();
    }

    void createAiList() {
        this.aiFactory.createAiList(GameRules.difficulty);
    }

    int getRandomLevelSize() {
        switch (this.random.nextInt(3)) {
            case 1:
                return 2;
            case 2:
                return 4;
            default:
                return 1;
        }
    }

    public void setLevelSize(int size) {
        this.cameraController.init(size);
        switch (size) {
            case 1:
                this.boundWidth = GraphicsYio.width;
                this.boundHeight = GraphicsYio.height;
                break;
            case 2:
                this.boundWidth = GraphicsYio.width * 2.0f;
                this.boundHeight = GraphicsYio.height;
                break;
            case 4:
                this.boundWidth = GraphicsYio.width * 2.0f;
                this.boundHeight = GraphicsYio.height * 2.0f;
                break;
            default:
                return;
        }
        this.cameraController.setBounds(this.boundWidth, this.boundHeight);
        this.fieldController.setLevelSize(size);
        this.yioGdxGame.gameView.createLevelCacheTextures();
    }

    public GameSaver getGameSaver() {
        return this.gameSaver;
    }

    public void debugActions() {
        this.debugActionsManager.debugActions();
    }

    public boolean isCityNamesEnabled() {
        return this.cityNamesEnabled;
    }

    public void setCityNamesEnabled(int cityNames) {
        if (cityNames == 1) {
            this.cityNamesEnabled = true;
        } else {
            this.cityNamesEnabled = false;
        }
    }

    void selectAdjacentHexes(Hex startHex) {
        this.selectionController.selectAdjacentHexes(startHex);
    }

    public void addSolidObject(Hex hex, int type) {
        this.fieldController.addSolidObject(hex, type);
    }

    public void cleanOutHex(Hex hex) {
        this.fieldController.cleanOutHex(hex);
    }

    public int getColorIndexWithOffset(int srcIndex) {
        return GameRules.inEditorMode ? srcIndex : this.ruleset.getColorIndexWithOffset(srcIndex);
    }

    public int getInvertedColor(int srcColor) {
        for (int color = 0; color < 7; color++) {
            if (this.ruleset.getColorIndexWithOffset(color) == srcColor) {
                return color;
            }
        }
        return -1;
    }

    private void performAutosave() {
        this.yioGdxGame.saveSystem.performAutosave();
    }

    public void takeSnapshot() {
        this.snapshotManager.takeSnapshot();
    }

    public int mergedUnitStrength(Unit unit1, Unit unit2) {
        return unit1.strength + unit2.strength;
    }

    public boolean playerHasAtLeastOneUnitWithStrength(int playerColor, int strength) {
        Iterator it = this.unitList.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (unit.getColor() == playerColor && unit.strength == strength) {
                return true;
            }
        }
        return false;
    }

    public boolean canUnitAttackHex(int unitStrength, int unitColor, Hex hex) {
        if (GameRules.diplomacyEnabled) {
            return this.ruleset.canUnitAttackHex(unitStrength, hex) && this.fieldController.diplomacyManager.canUnitAttackHex(unitStrength, unitColor, hex);
        } else {
            return this.ruleset.canUnitAttackHex(unitStrength, hex);
        }
    }

    boolean canMergeUnits(int strength1, int strength2) {
        return strength1 + strength2 <= 4;
    }

    public boolean mergeUnits(Hex hex, Unit unit1, Unit unit2) {
        if (!this.ruleset.canMergeUnits(unit1, unit2)) {
            return false;
        }
        this.fieldController.cleanOutHex(unit1.currentHex);
        this.fieldController.cleanOutHex(unit2.currentHex);
        Unit mergedUnit = this.fieldController.addUnit(hex, mergedUnitStrength(unit1, unit2));
        this.matchStatistics.onUnitsMerged();
        mergedUnit.setReadyToMove(true);
        if (unit1.isReadyToMove() && unit2.isReadyToMove()) {
            return true;
        }
        mergedUnit.setReadyToMove(false);
        mergedUnit.stopJumping();
        return true;
    }

    void tickleMoneySign() {
        ButtonYio coinButton;
        if (Settings.fastConstruction) {
            coinButton = this.yioGdxGame.menuControllerYio.getButtonById(610);
        } else {
            coinButton = this.yioGdxGame.menuControllerYio.getButtonById(37);
        }
        coinButton.appearFactor.setValues(1.0d, 0.13d);
        coinButton.appearFactor.appear(4, 1.0d);
    }

    public void restartGame() {
        if (this.fieldController.initialLevelString != null) {
            this.initialParameters.applyFullLevel(this.fieldController.initialLevelString);
        }
        LoadingManager.getInstance().startGame(this.initialParameters);
    }

    public void undoAction() {
        if (this.snapshotManager.undoAction()) {
            resetCurrentTouchCount();
        }
    }

    public void turnOffEditorMode() {
        GameRules.inEditorMode = false;
    }

    void updateCurrentPriceString() {
        this.currentPriceString = "$" + this.selectionController.getCurrentTipPrice();
        this.priceStringWidth = GraphicsYio.getTextWidth(Fonts.gameFont, this.currentPriceString);
    }

    void updateBalanceString() {
        if (this.fieldController.selectedProvince != null) {
            this.balanceString = this.fieldController.selectedProvince.getBalanceString();
        }
    }

    public Unit addUnit(Hex hex, int strength) {
        return this.fieldController.addUnit(hex, strength);
    }

    boolean isSomethingMoving() {
        Iterator it = this.fieldController.animHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit() && hex.unit.moveFactor.get() < 1.0f) {
                return true;
            }
        }
        if (GameRules.inEditorMode && this.levelEditor.isSomethingMoving()) {
            return true;
        }
        return false;
    }

    public LevelEditor getLevelEditor() {
        return this.levelEditor;
    }

    public void touchDown(int screenX, int screenY, int pointer, int button) {
        this.currentTouchCount++;
        this.touchPoint.set((double) screenX, (double) screenY);
        if (!GameRules.inEditorMode || !this.levelEditor.touchDown(screenX, screenY)) {
            if (this.currentTouchCount == 1) {
                setCheckToMarch(true);
            }
            this.clickDetector.touchDown(this.touchPoint);
            this.cameraController.touchDown(screenX, screenY);
        }
    }

    public void detectAndShowMoveZoneForBuildingUnit(int strength) {
        this.fieldController.detectAndShowMoveZoneForBuildingUnit(strength);
    }

    public void detectAndShowMoveZoneForFarm() {
        this.fieldController.detectAndShowMoveZoneForFarm();
    }

    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength) {
        return this.fieldController.detectMoveZone(startHex, strength);
    }

    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength, int moveLimit) {
        return this.fieldController.detectMoveZone(startHex, strength, moveLimit);
    }

    public void addAnimHex(Hex hex) {
        this.fieldController.addAnimHex(hex);
    }

    Province findProvinceCopy(Province src) {
        return this.fieldController.findProvinceCopy(src);
    }

    public Province getProvinceByHex(Hex hex) {
        return this.fieldController.getProvinceByHex(hex);
    }

    private int getNextTurnIndex(int currentTurn) {
        int next = currentTurn + 1;
        if (next >= GameRules.colorNumber) {
            return 0;
        }
        return next;
    }

    private int getNextTurnIndex() {
        return getNextTurnIndex(this.turn);
    }

    public boolean isPlayerTurn(int turn) {
        return turn < this.playersNumber;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn(this.turn);
    }

    public boolean isCurrentTurn(int turn) {
        return this.turn == turn;
    }

    public void moveUnit(Unit unit, Hex toWhere, Province unitProvince) {
        if (!unit.isReadyToMove()) {
            System.out.println("Someone tried to move unit that is not ready to move: " + unit);
            if (!GameRules.replayMode) {
                return;
            }
        }
        this.replayManager.onUnitMoved(unit.currentHex, toWhere);
        if (unit.currentHex.sameColor(toWhere)) {
            moveUnitPeacefully(unit, toWhere);
        } else {
            moveUnitWithAttack(unit, toWhere, unitProvince);
        }
        if (isPlayerTurn()) {
            this.fieldController.hideMoveZone();
            updateBalanceString();
        }
    }

    private void moveUnitWithAttack(Unit unit, Hex destination, Province unitProvince) {
        if (!destination.canBeAttackedBy(unit) || unitProvince == null) {
            System.out.println("Problem in GameController.moveUnitWithAttack");
            Yio.printStackTrace();
            return;
        }
        this.fieldController.setHexColor(destination, this.turn);
        this.fieldController.cleanOutHex(destination);
        unit.moveToHex(destination);
        unitProvince.addHex(destination);
        if (isPlayerTurn()) {
            this.fieldController.selectedHexes.add(destination);
            updateCacheOnceAfterSomeTime();
        }
    }

    private void moveUnitPeacefully(Unit unit, Hex toWhere) {
        if (toWhere.containsUnit()) {
            mergeUnits(toWhere, unit, toWhere.unit);
        } else {
            unit.moveToHex(toWhere);
        }
        if (isPlayerTurn()) {
            this.fieldController.setResponseAnimHex(toWhere);
        }
    }

    public void onClick() {
        this.fieldController.updateFocusedHex();
        showFocusedHexInConsole();
        if (this.fieldController.focusedHex != null && isPlayerTurn()) {
            focusedHexActions(this.fieldController.focusedHex);
        }
    }

    private void showFocusedHexInConsole() {
        if (DebugFlags.showFocusedHexInConsole) {
            YioGdxGame.say(this.fieldController.focusedHex.index1 + " " + this.fieldController.focusedHex.index2);
        }
    }

    public void focusedHexActions(Hex focusedHex) {
        this.selectionController.focusedHexActions(focusedHex);
    }

    public void setCheckToMarch(boolean checkToMarch) {
        this.checkToMarch = checkToMarch;
    }

    public void touchUp(int screenX, int screenY, int pointer, int button) {
        this.currentTouchCount--;
        this.touchPoint.set((double) screenX, (double) screenY);
        if (this.currentTouchCount < 0) {
            this.currentTouchCount = 0;
        }
        if (!GameRules.inEditorMode || !this.levelEditor.touchUp(screenX, screenY)) {
            this.clickDetector.touchUp(this.touchPoint);
            this.cameraController.touchUp(screenX, screenY);
            if (this.clickDetector.isClicked()) {
                onClick();
            }
        }
    }

    public void touchDragged(int screenX, int screenY, int pointer) {
        this.touchPoint.set((double) screenX, (double) screenY);
        if (!GameRules.inEditorMode || !this.levelEditor.touchDrag(screenX, screenY)) {
            this.clickDetector.touchDrag(this.touchPoint);
            this.cameraController.touchDrag(screenX, screenY);
        }
    }

    public void updateRuleset() {
        if (GameRules.slayRules) {
            this.ruleset = new RulesetSlay(this);
        } else {
            this.ruleset = new RulesetGeneric(this);
        }
    }

    public void scrolled(int amount) {
        if (amount == 1) {
            this.cameraController.changeZoomLevel(0.5d);
        } else if (amount == -1) {
            this.cameraController.changeZoomLevel(-0.6d);
        }
    }

    public void close() {
        for (int i = 0; i < this.fieldController.fWidth; i++) {
            for (int j = 0; j < this.fieldController.fHeight; j++) {
                if (this.fieldController.field[i][j] != null) {
                    this.fieldController.field[i][j].close();
                }
            }
        }
        if (this.fieldController.provinces != null) {
            Iterator it = this.fieldController.provinces.iterator();
            while (it.hasNext()) {
                ((Province) it.next()).close();
            }
        }
        this.fieldController.provinces = null;
        this.fieldController.field = (Hex[][]) null;
        this.yioGdxGame = null;
    }

    public ArrayList<Unit> getUnitList() {
        return this.unitList;
    }

    public MapGenerator getMapGeneratorSlay() {
        return this.mapGeneratorSlay;
    }

    public Random getPredictableRandom() {
        return this.predictableRandom;
    }

    public MapGenerator getMapGeneratorGeneric() {
        return this.mapGeneratorGeneric;
    }

    public YioGdxGame getYioGdxGame() {
        return this.yioGdxGame;
    }

    public boolean isInEditorMode() {
        return GameRules.inEditorMode;
    }

    public Random getRandom() {
        return this.random;
    }

    public long getCurrentTime() {
        return this.currentTime;
    }

    public MatchStatistics getMatchStatistics() {
        return this.matchStatistics;
    }

    public int getTurn() {
        return this.turn;
    }

    public ArrayList<ArtificialIntelligence> getAiList() {
        return this.aiList;
    }

    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
    }
}
