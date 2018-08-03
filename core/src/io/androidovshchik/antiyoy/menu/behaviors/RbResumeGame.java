package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbResumeGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getYioGdxGame(buttonYio).gameView.beginSpawnProcess();
        Scenes.sceneGameOverlay.create();
        getYioGdxGame(buttonYio).setGamePaused(false);
        getYioGdxGame(buttonYio).setAnimToResumeButtonSpecial();
    }
}
