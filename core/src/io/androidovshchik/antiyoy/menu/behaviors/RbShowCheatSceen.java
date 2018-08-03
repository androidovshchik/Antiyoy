package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbShowCheatSceen extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneSecretScreen.create();
    }
}
