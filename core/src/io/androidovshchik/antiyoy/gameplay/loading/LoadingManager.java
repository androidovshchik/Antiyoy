package io.androidovshchik.antiyoy.gameplay.loading;

import java.util.Iterator;
import java.util.Random;
import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.GameSaver;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.campaign.CampaignProgressManager;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class LoadingManager {
    public static final int MAX_LOADING_DELAY = 4000;
    private static LoadingManager instance = null;
    private WideScreenCompensationManager compensationManager = new WideScreenCompensationManager();
    GameController gameController;
    private GameSaver gameSaver;
    LoadingParameters parameters;
    YioGdxGame yioGdxGame;

    public static void initialize() {
        instance = null;
    }

    public static LoadingManager getInstance() {
        if (instance == null) {
            instance = new LoadingManager();
        }
        return instance;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        this.yioGdxGame = gameController.yioGdxGame;
        this.compensationManager.setGameController(gameController);
    }

    public void startGame(LoadingParameters loadingParameters) {
        this.parameters = loadingParameters;
        beginCreation();
        switch (loadingParameters.mode) {
            case 0:
                createTutorial();
                break;
            case 1:
                createSkirmish();
                break;
            case 2:
                createCustomCampaignLevel();
                break;
            case 3:
                createLoadedGame();
                break;
            case 4:
                createEditorLoaded();
                break;
            case 5:
                createEditorPlay();
                break;
            case 6:
                createRandomCampaignLevel();
                break;
            case 7:
                createEditorNew();
                break;
            case 8:
                createLoadedReplay();
                break;
            case 9:
                createUserLevel();
                break;
        }
        endCreation();
    }

    private void createUserLevel() {
        GameRules.campaignMode = false;
        GameRules.userLevelMode = true;
        GameRules.ulKey = this.parameters.ulKey;
        recreateActiveHexesFromParameter();
        this.gameSaver.detectRules();
    }

    private void createLoadedReplay() {
        boolean z = true;
        recreateActiveHexesFromParameter();
        GameRules.replayMode = true;
        if (this.parameters.campaignLevelIndex == -1) {
            z = false;
        }
        GameRules.campaignMode = z;
        this.gameController.checkToEnableAiOnlyMode();
        this.gameController.replayManager.setReplay(this.parameters.replay);
        this.gameController.replayManager.onLoadingFromSlotFinished(this.gameController.fieldController);
        this.gameController.stopAllUnitsFromJumping();
    }

    private void createEditorNew() {
        this.parameters.activeHexes = "";
        recreateActiveHexesFromParameter();
        this.gameSaver.detectRules();
        GameRules.inEditorMode = true;
        GameRules.slayRules = false;
    }

    private void createEditorLoaded() {
        recreateActiveHexesFromParameter();
        this.gameSaver.detectRules();
        GameRules.inEditorMode = true;
    }

    private void createEditorPlay() {
        recreateActiveHexesFromParameter();
        this.gameSaver.detectRules();
        applyEditorChosenColorFix();
        this.gameController.checkToEnableAiOnlyMode();
    }

    private void applyEditorChosenColorFix() {
        if (this.gameController.colorIndexViewOffset != 0) {
            this.gameController.updateRuleset();
            Iterator it = this.gameController.fieldController.activeHexes.iterator();
            while (it.hasNext()) {
                Hex activeHex = (Hex) it.next();
                if (GameRules.slayRules || !activeHex.isNeutral()) {
                    activeHex.colorIndex = this.gameController.getInvertedColor(activeHex.colorIndex);
                }
            }
            this.gameController.fieldController.detectProvinces();
            this.gameController.stopAllUnitsFromJumping();
            this.gameController.prepareCertainUnitsToMove();
        }
    }

    private void createLoadedGame() {
        recreateActiveHexesFromParameter();
        this.gameController.turn = this.parameters.turn;
        if (this.parameters.campaignLevelIndex > 0) {
            GameRules.campaignMode = true;
            CampaignProgressManager.getInstance().setCurrentLevelIndex(this.parameters.campaignLevelIndex);
        } else {
            CampaignProgressManager.getInstance().setCurrentLevelIndex(0);
        }
        GameRules.userLevelMode = this.parameters.userLevelMode;
        GameRules.ulKey = this.parameters.ulKey;
        this.gameController.checkToEnableAiOnlyMode();
    }

    private void createRandomCampaignLevel() {
        this.gameController.predictableRandom = new Random((long) this.parameters.campaignLevelIndex);
        GameRules.campaignMode = true;
        if (this.parameters.slayRules) {
            generateMapForSlayRules();
        } else {
            generateMapForGenericRules();
        }
    }

    private void generateMapForSlayRules() {
        int c = 0;
        FieldController fieldController = this.gameController.fieldController;
        long startTime = System.currentTimeMillis();
        while (c < 6 && System.currentTimeMillis() - startTime < 4000) {
            fieldController.clearAnims();
            fieldController.createField();
            fieldController.generateMap(true);
            if (fieldController.getPredictionForWinner() != 0) {
                c++;
            } else {
                return;
            }
        }
    }

    private void generateMapForGenericRules() {
        int c = 0;
        FieldController fieldController = this.gameController.fieldController;
        long startTime = System.currentTimeMillis();
        while (c < 6 && System.currentTimeMillis() - startTime < 4000) {
            fieldController.clearAnims();
            fieldController.createField();
            fieldController.generateMap(false);
            if (!fieldController.areConditionsGoodForPlayer()) {
                c++;
            } else {
                return;
            }
        }
    }

    private void createCustomCampaignLevel() {
        this.gameController.predictableRandom = new Random((long) this.parameters.campaignLevelIndex);
        GameRules.campaignMode = true;
        recreateActiveHexesFromParameter();
        this.gameSaver.detectRules();
    }

    private void createTutorial() {
        recreateActiveHexesFromParameter();
        GameRules.campaignMode = true;
        this.gameController.fieldController.giveMoneyToPlayerProvinces(90);
    }

    private void recreateActiveHexesFromParameter() {
        this.gameSaver.setActiveHexesString(this.parameters.activeHexes);
        this.gameSaver.beginRecreation();
    }

    private void createSkirmish() {
        this.gameController.fieldController.generateMap();
        this.gameController.checkToEnableAiOnlyMode();
        CampaignProgressManager.getInstance().setCurrentLevelIndex(0);
    }

    private void endCreation() {
        this.compensationManager.setGameController(this.gameController);
        this.compensationManager.perform();
        this.gameController.onEndCreation();
        this.gameController.updateInitialParameters(this.parameters);
        this.yioGdxGame.onEndCreation();
        if (GameRules.inEditorMode) {
            this.gameController.getLevelEditor().onEndCreation();
        }
        Scenes.sceneGameOverlay.create();
        if (GameRules.diplomacyEnabled) {
            this.gameController.fieldController.diplomacyManager.checkForSingleMessage();
        }
    }

    private void beginCreation() {
        System.out.println();
        System.out.println("Loading level...");
        this.gameSaver = this.gameController.gameSaver;
        this.gameController.defaultValues();
        this.yioGdxGame.beginBackgroundChange(4, false, true);
        this.gameController.predictableRandom = new Random((long) this.parameters.campaignLevelIndex);
        applyLoadingParameters();
        this.gameController.fieldController.createField();
    }

    private void applyLoadingParameters() {
        this.gameController.setLevelSize(this.parameters.levelSize);
        this.gameController.setPlayersNumber(this.parameters.playersNumber);
        GameRules.setColorNumber(this.parameters.colorNumber);
        GameRules.setDifficulty(this.parameters.difficulty);
        this.gameController.colorIndexViewOffset = this.parameters.colorOffset;
        GameRules.setSlayRules(this.parameters.slayRules);
        GameRules.setFogOfWarEnabled(this.parameters.fogOfWar);
        GameRules.setDiplomacyEnabled(this.parameters.diplomacy);
    }
}
