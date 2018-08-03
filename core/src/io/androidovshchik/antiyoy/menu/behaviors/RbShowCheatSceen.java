package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbShowCheatSceen extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneSecretScreen.create();
    }
}
