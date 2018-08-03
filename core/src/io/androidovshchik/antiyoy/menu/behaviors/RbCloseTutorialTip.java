package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbCloseTutorialTip extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getYioGdxGame(buttonYio).setGamePaused(false);
        if (getGameController(buttonYio).tutorialScript != null) {
            getGameController(buttonYio).tutorialScript.setTipIsCurrentlyShown(false);
        }
        Scenes.sceneTutorialTip.hide();
    }
}
