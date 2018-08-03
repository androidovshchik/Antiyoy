package io.androidovshchik.antiyoy.menu.scenes;

import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorActions;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorAutomationPanel;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorChecks;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorConfirmClear;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorConfirmImport;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorConfirmRandomize;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorHexPanel;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorMoneyPanel;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorObjectPanel;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorParams;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorSlots;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneAiOnlyOverlay;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneColorStats;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneConfirmBlackMarkDialog;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneConfirmDislike;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneConfirmEndTurn;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneDipInfoDialog;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneDipMessage;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneDiplomacy;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneDiplomaticLog;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneFastConstructionPanel;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneFriendshipDialog;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneGameOverlay;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneSelectionOverlay;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneStopWarDialog;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneSurrenderDialog;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneTurnStartDialog;
import yio.tro.antiyoy.menu.scenes.gameplay.SceneTutorialTip;

public class Scenes {
    public static SceneAfterGameMenu sceneAfterGameMenu;
    public static SceneBleentoroRelease sceneBleentoroRelease;
    public static SceneCampaignMenu sceneCampaignMenu;
    public static SceneChoodeGameModeMenu sceneChoodeGameModeMenu;
    public static SceneColorStats sceneColorStats;
    public static SceneConfirmBlackMarkDialog sceneConfirmBlackMarkDialog;
    public static SceneConfirmDislike sceneConfirmDislike;
    public static SceneConfirmEndTurn sceneConfirmEndTurn;
    public static SceneConfirmReset sceneConfirmReset;
    public static SceneConfirmRestart sceneConfirmRestart;
    public static SceneDipInfoDialog sceneDipInfoDialog;
    public static SceneDipMessage sceneDipMessage;
    public static SceneDiplomacy sceneDiplomacy;
    public static SceneDiplomaticLog sceneDiplomaticLog;
    public static SceneEditorActions sceneEditorActions;
    public static SceneEditorAutomationPanel sceneEditorAutomationPanel;
    public static SceneEditorChecks sceneEditorChecks;
    public static SceneEditorConfirmClear sceneEditorConfirmClear;
    public static SceneEditorConfirmImport sceneEditorConfirmImport;
    public static SceneEditorConfirmRandomize sceneEditorConfirmRandomize;
    public static SceneEditorHexPanel sceneEditorHexPanel;
    public static SceneEditorInstruments sceneEditorInstruments;
    public static SceneEditorMoneyPanel sceneEditorMoneyPanel;
    public static SceneEditorObjectPanel sceneEditorObjectPanel;
    public static SceneEditorParams sceneEditorParams;
    public static SceneEditorSlots sceneEditorSlots;
    public static SceneExceptionReport sceneExceptionReport;
    public static SceneFastConstructionPanel sceneFastConstructionPanel;
    public static SceneFireworks sceneFireworks;
    public static SceneFriendshipDialog sceneFriendshipDialog;
    public static SceneGameOverlay sceneGameOverlay;
    public static SceneHelpIndex sceneHelpIndex;
    public static SceneInfoMenu sceneInfoMenu;
    public static SceneLanguages sceneLanguages;
    public static SceneMainMenu sceneMainMenu;
    public static SceneMoreCampaignOptions sceneMoreCampaignOptions;
    public static SceneMoreSettingsMenu sceneMoreSettingsMenu;
    public static SceneMoreSkirmishOptions sceneMoreSkirmishOptions;
    public static SceneNotification sceneNotification;
    public static ScenePauseMenu scenePauseMenu;
    public static SceneAiOnlyOverlay sceneReplayOverlay;
    public static SceneReplays sceneReplays;
    public static SceneSaveLoad sceneSaveLoad;
    public static SceneSecretScreen sceneSecretScreen;
    public static SceneSelectionOverlay sceneSelectionOverlay;
    public static SceneSettingsMenu sceneSettingsMenu;
    public static SceneSingleMessage sceneSingleMessage;
    public static SceneSkirmishMenu sceneSkirmishMenu;
    public static SceneSpecialThanks sceneSpecialThanks;
    public static SceneStatisticsMenu sceneStatisticsMenu;
    public static SceneStopWarDialog sceneStopWarDialog;
    public static SceneSurrenderDialog sceneSurrenderDialog;
    public static SceneTestMenu sceneTestMenu;
    public static SceneTestScrollableList sceneTestScrollableList;
    public static SceneTurnStartDialog sceneTurnStartDialog;
    public static SceneTutorialIndex sceneTutorialIndex;
    public static SceneTutorialTip sceneTutorialTip;
    public static SceneUserLevels sceneUserLevels;

    public static void createScenes(MenuControllerYio menuController) {
        sceneMainMenu = new SceneMainMenu(menuController);
        sceneSettingsMenu = new SceneSettingsMenu(menuController);
        sceneMoreSettingsMenu = new SceneMoreSettingsMenu(menuController);
        sceneInfoMenu = new SceneInfoMenu(menuController);
        sceneMoreSkirmishOptions = new SceneMoreSkirmishOptions(menuController);
        sceneSkirmishMenu = new SceneSkirmishMenu(menuController);
        sceneTestMenu = new SceneTestMenu(menuController);
        sceneChoodeGameModeMenu = new SceneChoodeGameModeMenu(menuController);
        sceneTutorialIndex = new SceneTutorialIndex(menuController);
        sceneHelpIndex = new SceneHelpIndex(menuController);
        sceneCampaignMenu = new SceneCampaignMenu(menuController);
        sceneConfirmEndTurn = new SceneConfirmEndTurn(menuController);
        sceneConfirmReset = new SceneConfirmReset(menuController);
        sceneConfirmRestart = new SceneConfirmRestart(menuController);
        sceneEditorConfirmClear = new SceneEditorConfirmClear(menuController);
        sceneEditorActions = new SceneEditorActions(menuController);
        sceneEditorConfirmImport = new SceneEditorConfirmImport(menuController);
        sceneEditorObjectPanel = new SceneEditorObjectPanel(menuController);
        sceneEditorHexPanel = new SceneEditorHexPanel(menuController);
        sceneEditorInstruments = new SceneEditorInstruments(menuController);
        sceneGameOverlay = new SceneGameOverlay(menuController);
        sceneSelectionOverlay = new SceneSelectionOverlay(menuController);
        scenePauseMenu = new ScenePauseMenu(menuController);
        sceneColorStats = new SceneColorStats(menuController);
        sceneTutorialTip = new SceneTutorialTip(menuController);
        sceneSurrenderDialog = new SceneSurrenderDialog(menuController);
        sceneNotification = new SceneNotification(menuController);
        sceneAfterGameMenu = new SceneAfterGameMenu(menuController);
        sceneStatisticsMenu = new SceneStatisticsMenu(menuController);
        sceneSingleMessage = new SceneSingleMessage(menuController);
        sceneExceptionReport = new SceneExceptionReport(menuController);
        sceneEditorAutomationPanel = new SceneEditorAutomationPanel(menuController);
        sceneEditorConfirmRandomize = new SceneEditorConfirmRandomize(menuController);
        sceneSecretScreen = new SceneSecretScreen(menuController);
        sceneEditorMoneyPanel = new SceneEditorMoneyPanel(menuController);
        sceneMoreCampaignOptions = new SceneMoreCampaignOptions(menuController);
        sceneReplayOverlay = new SceneAiOnlyOverlay(menuController);
        sceneReplays = new SceneReplays(menuController);
        sceneFastConstructionPanel = new SceneFastConstructionPanel(menuController);
        sceneFireworks = new SceneFireworks(menuController);
        sceneSaveLoad = new SceneSaveLoad(menuController);
        sceneEditorParams = new SceneEditorParams(menuController);
        sceneDiplomacy = new SceneDiplomacy(menuController);
        sceneDipInfoDialog = new SceneDipInfoDialog(menuController);
        sceneFriendshipDialog = new SceneFriendshipDialog(menuController);
        sceneConfirmDislike = new SceneConfirmDislike(menuController);
        sceneDipMessage = new SceneDipMessage(menuController);
        sceneStopWarDialog = new SceneStopWarDialog(menuController);
        sceneConfirmBlackMarkDialog = new SceneConfirmBlackMarkDialog(menuController);
        sceneTestScrollableList = new SceneTestScrollableList(menuController);
        sceneLanguages = new SceneLanguages(menuController);
        sceneSpecialThanks = new SceneSpecialThanks(menuController);
        sceneUserLevels = new SceneUserLevels(menuController);
        sceneEditorSlots = new SceneEditorSlots(menuController);
        sceneEditorSlots = new SceneEditorSlots(menuController);
        sceneEditorChecks = new SceneEditorChecks(menuController);
        sceneTurnStartDialog = new SceneTurnStartDialog(menuController);
        sceneDiplomaticLog = new SceneDiplomaticLog(menuController);
        sceneBleentoroRelease = new SceneBleentoroRelease(menuController);
    }
}
