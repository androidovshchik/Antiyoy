package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbResumeGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getYioGdxGame(buttonYio).gameView.beginSpawnProcess();
        Scenes.sceneGameOverlay.create();
        getYioGdxGame(buttonYio).setGamePaused(false);
        getYioGdxGame(buttonYio).setAnimToResumeButtonSpecial();
    }
}
