package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbInfo extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Settings.getInstance().saveSettings();
        Scenes.sceneInfoMenu.create();
    }
}
