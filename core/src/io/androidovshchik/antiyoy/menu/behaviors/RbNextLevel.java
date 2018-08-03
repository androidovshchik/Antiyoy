package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbNextLevel extends Reaction {
    public void perform(ButtonYio buttonYio) {
        CampaignProgressManager instance = CampaignProgressManager.getInstance();
        if (instance.currentLevelIndex == CampaignProgressManager.getIndexOfLastLevel()) {
            Scenes.sceneFireworks.create();
            return;
        }
        getYioGdxGame(buttonYio).campaignLevelFactory.createCampaignLevel(instance.getNextLevelIndex());
    }
}
