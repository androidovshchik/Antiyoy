package io.androidovshchik.antiyoy.gameplay.campaign;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.loading.LoadingManager;
import yio.tro.antiyoy.gameplay.loading.LoadingParameters;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.menu.slider.SliderYio;
import yio.tro.antiyoy.stuff.LanguagesManager;

public class CampaignLevelFactory {
    public static final int EXPERT_LEVELS_START = 60;
    public static final int HARD_LEVELS_START = 24;
    public static final int NORMAL_LEVELS_START = 9;
    public GameController gameController;
    int index = -1;
    private final LevelPackFive levelPackFive = new LevelPackFive(this);
    private final LevelPackFour levelPackFour = new LevelPackFour(this);
    private final LevelPackOne levelPackOne = new LevelPackOne(this);
    private final LevelPackThree levelPackThree = new LevelPackThree(this);
    private final LevelPackTwo levelPackTwo = new LevelPackTwo(this);

    public CampaignLevelFactory(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean createCampaignLevel(int index) {
        this.index = index;
        Scenes.sceneMoreCampaignOptions.prepare();
        CampaignProgressManager.getInstance().setCurrentLevelIndex(index);
        this.gameController.getYioGdxGame().setSelectedLevelIndex(index);
        updateRules();
        if (checkForTutorial()) {
            return true;
        }
        if (CampaignProgressManager.getInstance().isLevelLocked(index)) {
            return false;
        }
        if (this.levelPackOne.checkForLevelPackOne() || this.levelPackTwo.checkForlevelPack() || this.levelPackThree.checkForlevelPack() || this.levelPackFour.checkForlevelPack() || this.levelPackFive.checkForlevelPack()) {
            return true;
        }
        createLevelWithPredictableRandom();
        return true;
    }

    private void updateRules() {
        GameRules.setSlayRules(this.gameController.yioGdxGame.menuControllerYio.getCheckButtonById(17).isChecked());
    }

    private boolean checkForTutorial() {
        if (this.index != 0) {
            return false;
        }
        this.gameController.initTutorial();
        return true;
    }

    private void createLevelWithPredictableRandom() {
        LoadingParameters instance = LoadingParameters.getInstance();
        instance.mode = 6;
        instance.levelSize = getLevelSizeByIndex(this.index);
        instance.playersNumber = 1;
        instance.colorNumber = getColorNumberByIndex(this.index);
        instance.difficulty = getDifficultyByIndex(this.index);
        instance.colorOffset = readColorOffsetFromSlider(instance.colorNumber);
        instance.slayRules = GameRules.slayRules;
        instance.campaignLevelIndex = this.index;
        LoadingManager.getInstance().startGame(instance);
        checkForHelloMessage(this.index);
    }

    public int readColorOffsetFromSlider(int colorNumber) {
        return this.gameController.getColorOffsetBySliderIndex(getColorOffsetSlider().getCurrentRunnerIndex(), colorNumber);
    }

    private SliderYio getColorOffsetSlider() {
        return Scenes.sceneMoreCampaignOptions.colorOffsetSlider;
    }

    public void checkForHelloMessage(int index) {
        if (index == 24) {
            ArrayList<String> text = this.gameController.yioGdxGame.menuControllerYio.getArrayListFromString(LanguagesManager.getInstance().getString("level_24"));
            text.add(" ");
            text.add(" ");
            Scenes.sceneTutorialTip.createTutorialTip(text);
        }
    }

    public static int getDifficultyByIndex(int index) {
        if (index <= 8) {
            return 0;
        }
        if (index <= 23) {
            return 1;
        }
        if (index >= 60) {
            return 3;
        }
        return 2;
    }

    private int getColorNumberByIndex(int index) {
        if (index <= 4 || index == 20) {
            return 3;
        }
        if (index <= 7) {
            return 4;
        }
        if (index < 10 || index > 13) {
            return 5;
        }
        return 4;
    }

    private int getLevelSizeByIndex(int index) {
        if (index == 4 || index == 7) {
            return 2;
        }
        if (index == 15) {
            return 1;
        }
        if (index == 20 || index == 30 || index == 35) {
            return 4;
        }
        if (index >= 60 && index <= 64) {
            return 2;
        }
        if (index > 50 && index <= 53) {
            return 2;
        }
        if (index <= 10) {
            return 1;
        }
        if (index <= 40) {
            return 2;
        }
        return 4;
    }
}
