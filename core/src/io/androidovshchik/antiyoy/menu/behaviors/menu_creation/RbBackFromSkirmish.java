package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbBackFromSkirmish extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneSkirmishMenu.saveValues();
        Scenes.sceneChoodeGameModeMenu.create();
    }
}
