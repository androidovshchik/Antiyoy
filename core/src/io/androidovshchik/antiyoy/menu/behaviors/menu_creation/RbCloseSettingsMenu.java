package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class RbCloseSettingsMenu extends Reaction {
    public void perform(ButtonYio buttonYio) {
        boolean needToRestart = Settings.getInstance().saveSettings();
        Scenes.sceneMainMenu.create();
        if (needToRestart) {
            Scenes.sceneNotification.showNotification(LanguagesManager.getInstance().getString("restart_app"));
        }
        Settings.getInstance().loadSettings();
    }
}
