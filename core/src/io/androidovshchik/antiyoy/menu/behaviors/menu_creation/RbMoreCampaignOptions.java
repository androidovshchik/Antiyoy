package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbMoreCampaignOptions extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneMoreCampaignOptions.create();
    }
}
