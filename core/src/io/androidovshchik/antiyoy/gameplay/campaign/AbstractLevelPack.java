package io.androidovshchik.antiyoy.gameplay.campaign;

import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;

public abstract class AbstractLevelPack {
    protected final CampaignLevelFactory campaignLevelFactory;
    protected int index;

    abstract String getLevelFromPack();

    public AbstractLevelPack(CampaignLevelFactory campaignLevelFactory) {
        this.campaignLevelFactory = campaignLevelFactory;
    }

    boolean checkForlevelPack() {
        this.index = this.campaignLevelFactory.index;
        String levelFromPackTwo = getLevelFromPack();
        if (levelFromPackTwo.equals("-") || GameRules.slayRules) {
            return false;
        }
        LoadingParameters instance = LoadingParameters.getInstance();
        instance.mode = 2;
        instance.applyFullLevel(levelFromPackTwo);
        instance.campaignLevelIndex = this.index;
        instance.slayRules = GameRules.slayRules;
        instance.colorOffset = this.campaignLevelFactory.readColorOffsetFromSlider(instance.colorNumber);
        applySpecialParameters(instance);
        LoadingManager.getInstance().startGame(instance);
        onLevelLoaded();
        this.campaignLevelFactory.checkForHelloMessage(this.index);
        return true;
    }

    protected void applySpecialParameters(LoadingParameters instance) {
    }

    protected void onLevelLoaded() {
    }

    protected void setProvinceMoney(int i, int j, int money) {
        FieldController fieldController = this.campaignLevelFactory.gameController.fieldController;
        fieldController.getProvinceByHex(fieldController.field[i][j]).money = money;
    }
}
