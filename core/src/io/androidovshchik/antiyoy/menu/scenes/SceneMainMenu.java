package io.androidovshchik.antiyoy.menu.scenes;

import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.gameplay.campaign.CampaignProgressManager;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SceneMainMenu extends AbstractScene {
    public ButtonYio playButton;
    Reaction playButtonReaction = new C01231();

    class C01231 extends Reaction {
        C01231() {
        }

        public void perform(ButtonYio buttonYio) {
            SceneMainMenu.this.onPlayButtonPressed();
        }
    }

    public SceneMainMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    private void onPlayButtonPressed() {
        if (CampaignProgressManager.getInstance().isAtLeastOneLevelCompleted()) {
            Scenes.sceneChoodeGameModeMenu.create();
            this.menuControllerYio.yioGdxGame.setGamePaused(true);
            this.menuControllerYio.yioGdxGame.setAnimToPlayButtonSpecial();
            return;
        }
        this.menuControllerYio.yioGdxGame.campaignLevelFactory.createCampaignLevel(0);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(0, false, true);
        ButtonYio exitButton = this.buttonFactory.getButton(generateSquare(0.8d, 0.87d, 0.15d * ((double) YioGdxGame.screenRatio)), 1, null);
        this.menuControllerYio.loadButtonOnce(exitButton, "shut_down.png");
        exitButton.setShadow(true);
        exitButton.setAnimation(1);
        exitButton.setReaction(Reaction.rbExit);
        exitButton.setTouchOffset(0.05f * GraphicsYio.width);
        exitButton.disableTouchAnimation();
        ButtonYio settingsButton = this.buttonFactory.getButton(generateSquare(0.05d, 0.87d, 0.15d * ((double) YioGdxGame.screenRatio)), 2, null);
        this.menuControllerYio.loadButtonOnce(settingsButton, "settings_icon.png");
        settingsButton.setShadow(true);
        settingsButton.setAnimation(1);
        settingsButton.setReaction(Reaction.rbSettingsMenu);
        settingsButton.setTouchOffset(0.05f * GraphicsYio.width);
        settingsButton.disableTouchAnimation();
        this.playButton = this.buttonFactory.getButton(generateSquare(0.3d, 0.35d, 0.4d * ((double) YioGdxGame.screenRatio)), 3, null);
        this.menuControllerYio.loadButtonOnce(this.playButton, "play_button.png");
        this.playButton.setReaction(this.playButtonReaction);
        this.playButton.disableTouchAnimation();
        this.playButton.selectionFactor.setValues(1.0d, 0.0d);
        this.menuControllerYio.endMenuCreation();
    }
}
