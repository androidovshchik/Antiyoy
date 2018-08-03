package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.gameplay.campaign.CampaignProgressManager;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;

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
