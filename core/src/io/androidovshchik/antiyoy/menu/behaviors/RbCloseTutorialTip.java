package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbCloseTutorialTip extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getYioGdxGame(buttonYio).setGamePaused(false);
        if (getGameController(buttonYio).tutorialScript != null) {
            getGameController(buttonYio).tutorialScript.setTipIsCurrentlyShown(false);
        }
        Scenes.sceneTutorialTip.hide();
    }
}
