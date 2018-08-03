package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbResetProgress extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).resetProgress();
        Scenes.sceneCampaignMenu.updateLevelSelector();
        Scenes.sceneCampaignMenu.create();
    }
}
