package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.stuff.LanguagesManager;

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
