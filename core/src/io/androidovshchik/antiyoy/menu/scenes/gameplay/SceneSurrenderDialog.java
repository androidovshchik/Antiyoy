package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class SceneSurrenderDialog extends AbstractScene {
    public SceneSurrenderDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        Scenes.sceneTutorialTip.createTutorialTip(this.menuControllerYio.getArrayListFromString(LanguagesManager.getInstance().getString("win_or_continue")));
        addWinButtonToTutorialTip();
    }

    public void addWinButtonToTutorialTip() {
        ButtonYio winButton = this.buttonFactory.getButton(generateRectangle(0.0d, 0.1d, 0.5d, 0.05d), 54, null);
        winButton.setTextLine(getString("win_game"));
        this.menuControllerYio.getButtonRenderer().renderButton(winButton);
        winButton.setShadow(false);
        winButton.setReaction(Reaction.rbWinGame);
        winButton.setAnimation(6);
        winButton.appearFactor.appear(3, 1.0d);
        winButton.disableTouchAnimation();
        ButtonYio okButton = this.menuControllerYio.getButtonById(53);
        okButton.setPosition(generateRectangle(0.5d, 0.1d, 0.5d, 0.05d));
        okButton.setTextLine(getString("continue"));
        okButton.setReaction(Reaction.rbRefuseEarlyGameEnd);
        okButton.setAnimation(6);
        okButton.disableTouchAnimation();
        this.menuControllerYio.getButtonRenderer().renderButton(okButton);
    }
}
