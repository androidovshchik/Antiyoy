package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class SceneAfterGameMenu extends AbstractScene {
    boolean playerIsWinner = true;
    private ButtonYio replayButton;
    int whoWon = 0;

    public SceneAfterGameMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create(int whoWon, boolean playerIsWinner) {
        String message;
        this.whoWon = whoWon;
        this.playerIsWinner = playerIsWinner;
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().setGamePaused(true);
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, false);
        if (playerIsWinner) {
            message = this.menuControllerYio.getColorNameByIndexWithOffset(whoWon, "_player") + " " + getString("player") + " " + getString("won") + ".";
        } else {
            message = this.menuControllerYio.getColorNameByIndexWithOffset(whoWon, "_ai") + " " + getString("ai") + " " + getString("won") + ".";
        }
        if (CampaignProgressManager.getInstance().completedCampaignLevel(whoWon)) {
            message = getString("level_complete");
        }
        ButtonYio textPanel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.4d, 0.9d, 0.2d), 60, null);
        textPanel.cleatText();
        textPanel.addTextLine(message);
        textPanel.addTextLine("");
        textPanel.addTextLine("");
        this.menuControllerYio.getButtonRenderer().renderButton(textPanel);
        textPanel.setTouchable(false);
        textPanel.setAnimation(5);
        ButtonYio okButton = this.buttonFactory.getButton(generateRectangle(0.55d, 0.4d, 0.4d, SceneEditorInstruments.ICON_SIZE), 62, null);
        if (CampaignProgressManager.getInstance().completedCampaignLevel(whoWon)) {
            okButton.setTextLine(getString("next"));
        } else if (playerIsWinner) {
            okButton.setTextLine(getString("end_game_ok"));
        } else {
            okButton.setTextLine(getString("end_game_okay"));
        }
        this.menuControllerYio.getButtonRenderer().renderButton(okButton);
        okButton.setShadow(false);
        okButton.setReaction(Reaction.rbChooseGameModeMenu);
        if (CampaignProgressManager.getInstance().completedCampaignLevel(whoWon)) {
            okButton.setReaction(Reaction.rbNextLevel);
        }
        okButton.setAnimation(5);
        ButtonYio statisticsButton = this.buttonFactory.getButton(generateRectangle(0.05d, 0.4d, 0.5d, SceneEditorInstruments.ICON_SIZE), 61, getString("statistics"));
        statisticsButton.setShadow(false);
        statisticsButton.setReaction(Reaction.rbStatisticsMenu);
        statisticsButton.setAnimation(5);
        createReplayButton();
        this.menuControllerYio.endMenuCreation();
    }

    private void createReplayButton() {
        if (Settings.replaysEnabled) {
            this.replayButton = this.buttonFactory.getButton(generateRectangle(0.6d, 0.9d, 0.35d, 0.05d), 63, getString("replay"));
            this.replayButton.setReaction(Reaction.rbStartInstantReplay);
            this.replayButton.setAnimation(1);
            this.replayButton.setTouchOffset(0.05f * GraphicsYio.width);
            this.replayButton.disableTouchAnimation();
        }
    }

    public void create() {
        create(this.whoWon, this.playerIsWinner);
    }
}
