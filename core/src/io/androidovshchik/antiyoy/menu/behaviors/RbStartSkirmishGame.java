package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.menu.slider.SliderYio;

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
