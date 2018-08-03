package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbInfo extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Settings.getInstance().saveSettings();
        Scenes.sceneInfoMenu.create();
    }
}
