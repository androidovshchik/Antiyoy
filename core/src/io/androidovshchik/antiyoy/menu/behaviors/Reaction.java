package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.editor.RbInputModeMove;
import yio.tro.antiyoy.menu.behaviors.editor.RbSwitchFilterOnlyLand;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbBuildSolidObject;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbBuildUnit;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbEndTurn;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbHideColorStats;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbHideEndTurnConfirm;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbShowColorStats;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbUndo;
import yio.tro.antiyoy.menu.behaviors.gameplay.RbWinGame;
import yio.tro.antiyoy.menu.behaviors.help.RbArticleMoney;
import yio.tro.antiyoy.menu.behaviors.help.RbArticleRules;
import yio.tro.antiyoy.menu.behaviors.help.RbArticleTactics;
import yio.tro.antiyoy.menu.behaviors.help.RbArticleTowers;
import yio.tro.antiyoy.menu.behaviors.help.RbArticleTrees;
import yio.tro.antiyoy.menu.behaviors.help.RbArticleUnits;
import yio.tro.antiyoy.menu.behaviors.help.RbHelpIndex;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbBackFromSkirmish;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbCampaignMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbChooseGameModeMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbCloseSettingsMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbConfirmReset;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbExitToCampaign;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbInfo;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbLanguageMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbMainMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbMoreCampaignOptions;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbMoreSettings;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbMoreSkirmishOptions;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbPauseMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbReplaysMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbSaveMoreSkirmishOptions;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbSettingsMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbShowFps;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbSkirmishMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbSpecialThanksMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbStartInstantReplay;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbStatisticsMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbTestMenu;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbTutorialIndex;
import yio.tro.antiyoy.menu.behaviors.menu_creation.RbUnlockLevels;

public abstract class Reaction {
    public static final RbArticleMoney rbArticleMoney = new RbArticleMoney();
    public static final RbArticleRules rbArticleRules = new RbArticleRules();
    public static final RbArticleTactics rbArticleTactics = new RbArticleTactics();
    public static final RbArticleTowers rbArticleTowers = new RbArticleTowers();
    public static final RbArticleTrees rbArticleTrees = new RbArticleTrees();
    public static final RbArticleUnits rbArticleUnits = new RbArticleUnits();
    public static final RbBackFromSkirmish rbBackFromSkirmish = new RbBackFromSkirmish();
    public static final RbBuildSolidObject rbBuildSolidObject = new RbBuildSolidObject();
    public static final RbBuildUnit rbBuildUnit = new RbBuildUnit();
    public static final RbCampaignMenu rbCampaignMenu = new RbCampaignMenu();
    public static final RbChooseGameModeMenu rbChooseGameModeMenu = new RbChooseGameModeMenu();
    public static final RbCloseSettingsMenu rbCloseSettingsMenu = new RbCloseSettingsMenu();
    public static final RbCloseTutorialTip rbCloseTutorialTip = new RbCloseTutorialTip();
    public static final RbConfirmReset rbConfirmReset = new RbConfirmReset();
    public static RbDebugActions rbDebugActions = new RbDebugActions();
    public static final RbEndTurn rbEndTurn = new RbEndTurn();
    public static final RbExit rbExit = new RbExit();
    public static final RbExitToCampaign rbExitToCampaign = new RbExitToCampaign();
    public static final RbHelpIndex rbHelpIndex = new RbHelpIndex();
    public static final RbHideColorStats rbHideColorStats = new RbHideColorStats();
    public static final RbHideEndTurnConfirm rbHideEndTurnConfirm = new RbHideEndTurnConfirm();
    public static final RbInfo rbInfo = new RbInfo();
    public static final RbInputModeMove rbInputModeMove = new RbInputModeMove();
    public static final RbLanguageMenu rbLanguageMenu = new RbLanguageMenu();
    public static final RbLoadGame rbLoadGame = new RbLoadGame();
    public static final RbMainMenu rbMainMenu = new RbMainMenu();
    public static final RbMoreCampaignOptions rbMoreCampaignOptions = new RbMoreCampaignOptions();
    public static final RbMoreSettings rbMoreSettings = new RbMoreSettings();
    public static final RbMoreSkirmishOptions rbMoreSkirmishOptions = new RbMoreSkirmishOptions();
    public static final RbNextLevel rbNextLevel = new RbNextLevel();
    public static RbNothing rbNothing = new RbNothing();
    public static final RbPauseMenu rbPauseMenu = new RbPauseMenu();
    public static final RbRefuseEarlyGameEnd rbRefuseEarlyGameEnd = new RbRefuseEarlyGameEnd();
    public static final RbReplaysMenu rbReplaysMenu = new RbReplaysMenu();
    public static final RbResetProgress rbResetProgress = new RbResetProgress();
    public static final RbRestartGame rbRestartGame = new RbRestartGame();
    public static final RbResumeGame rbResumeGame = new RbResumeGame();
    public static final RbSaveGame rbSaveGame = new RbSaveGame();
    public static final RbSaveMoreSkirmishOptions rbSaveMoreSkirmishOptions = new RbSaveMoreSkirmishOptions();
    public static final RbSettingsMenu rbSettingsMenu = new RbSettingsMenu();
    public static final RbShowCheatSceen rbShowCheatSceen = new RbShowCheatSceen();
    public static final RbShowColorStats rbShowColorStats = new RbShowColorStats();
    public static final RbShowFps rbShowFps = new RbShowFps();
    public static final RbSkirmishMenu rbSkirmishMenu = new RbSkirmishMenu();
    public static final RbSpecialThanksMenu rbSpecialThanksMenu = new RbSpecialThanksMenu();
    public static final RbStartInstantReplay rbStartInstantReplay = new RbStartInstantReplay();
    public static final RbStartSkirmishGame rbStartSkirmishGame = new RbStartSkirmishGame();
    public static final RbStatisticsMenu rbStatisticsMenu = new RbStatisticsMenu();
    public static final RbSwitchFilterOnlyLand rbSwitchFilterOnlyLand = new RbSwitchFilterOnlyLand();
    public static RbTestMenu rbTestMenu = new RbTestMenu();
    public static final RbTutorialGeneric rbTutorialGeneric = new RbTutorialGeneric();
    public static final RbTutorialIndex rbTutorialIndex = new RbTutorialIndex();
    public static final RbTutorialSlay rbTutorialSlay = new RbTutorialSlay();
    public static final RbUndo rbUndo = new RbUndo();
    public static final RbUnlockLevels rbUnlockLevels = new RbUnlockLevels();
    public static final RbWinGame rbWinGame = new RbWinGame();

    public abstract void perform(ButtonYio buttonYio);

    protected YioGdxGame getYioGdxGame(ButtonYio buttonYio) {
        return buttonYio.menuControllerYio.yioGdxGame;
    }

    protected GameController getGameController(ButtonYio buttonYio) {
        return buttonYio.menuControllerYio.yioGdxGame.gameController;
    }
}
