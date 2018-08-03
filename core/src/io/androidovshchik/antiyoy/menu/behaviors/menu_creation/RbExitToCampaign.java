package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbExitToCampaign extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneMoreCampaignOptions.saveValues();
        Scenes.sceneCampaignMenu.create();
    }
}
