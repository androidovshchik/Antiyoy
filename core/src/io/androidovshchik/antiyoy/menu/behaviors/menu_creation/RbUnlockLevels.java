package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.gameplay.DebugFlags;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbUnlockLevels extends Reaction {
    public void perform(ButtonYio buttonYio) {
        DebugFlags.unlockLevels = true;
        Scenes.sceneCampaignMenu.create();
        Scenes.sceneNotification.showNotification("Levels unlocked");
    }
}
