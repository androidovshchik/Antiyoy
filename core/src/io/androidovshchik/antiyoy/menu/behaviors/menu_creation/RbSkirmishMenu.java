package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbSkirmishMenu extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getYioGdxGame(buttonYio).setGamePaused(true);
        Scenes.sceneSkirmishMenu.create();
        getYioGdxGame(buttonYio).setAnimToPlayButtonSpecial();
    }
}
