package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbPauseMenu extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).selectionController.deselectAll();
        Scenes.scenePauseMenu.create();
        getYioGdxGame(buttonYio).setGamePaused(true);
    }
}
