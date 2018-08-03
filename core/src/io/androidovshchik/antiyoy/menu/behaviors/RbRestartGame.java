package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbRestartGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        if (buttonYio.id == 221) {
            getYioGdxGame(buttonYio).restartGame();
        } else {
            Scenes.sceneConfirmRestart.create();
        }
    }
}
