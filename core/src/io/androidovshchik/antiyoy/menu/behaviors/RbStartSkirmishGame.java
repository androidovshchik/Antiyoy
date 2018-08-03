package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.gameplay.loading.LoadingManager;
import yio.tro.antiyoy.gameplay.loading.LoadingParameters;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.menu.slider.SliderYio;

public class RbStartSkirmishGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        MenuControllerYio menuControllerYio = buttonYio.menuControllerYio;
        Scenes.sceneSkirmishMenu.saveValues();
        Scenes.sceneMoreSkirmishOptions.create();
        menuControllerYio.getButtonById(80).setTouchable(false);
        LoadingParameters instance = LoadingParameters.getInstance();
        instance.mode = 1;
        instance.levelSize = getLevelSizeBySliderPos(Scenes.sceneSkirmishMenu.mapSizeSlider);
        instance.playersNumber = Scenes.sceneSkirmishMenu.playersSlider.getCurrentRunnerIndex();
        instance.colorNumber = Scenes.sceneSkirmishMenu.colorsSlider.getCurrentRunnerIndex() + 2;
        instance.difficulty = Scenes.sceneSkirmishMenu.difficultySlider.getCurrentRunnerIndex();
        instance.colorOffset = getGameController(buttonYio).getColorOffsetBySliderIndex(Scenes.sceneMoreSkirmishOptions.colorOffsetSlider.getCurrentRunnerIndex(), instance.colorNumber);
        instance.slayRules = Scenes.sceneMoreSkirmishOptions.chkSlayRules.isChecked();
        instance.fogOfWar = Scenes.sceneMoreSkirmishOptions.chkFogOfWar.isChecked();
        instance.diplomacy = Scenes.sceneMoreSkirmishOptions.chkDiplomacy.isChecked();
        LoadingManager.getInstance().startGame(instance);
        getYioGdxGame(buttonYio).setAnimToStartButtonSpecial();
    }

    public int getLevelSizeBySliderPos(SliderYio sliderYio) {
        switch (sliderYio.getCurrentRunnerIndex()) {
            case 1:
                return 2;
            case 2:
                return 4;
            default:
                return 1;
        }
    }
}
