package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbResetProgress extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).resetProgress();
        Scenes.sceneCampaignMenu.updateLevelSelector();
        Scenes.sceneCampaignMenu.create();
    }
}
