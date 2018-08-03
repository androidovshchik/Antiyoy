package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.campaign.CampaignProgressManager;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneTutorialTip extends AbstractGameplayScene {

    class C01481 extends Reaction {
        C01481() {
        }

        public void perform(ButtonYio buttonYio) {
            CampaignProgressManager.getInstance().markLevelAsCompleted(0);
            Scenes.sceneHelpIndex.create();
        }
    }

    public SceneTutorialTip(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void createTutorialTip(ArrayList<String> text) {
        this.menuControllerYio.getButtonById(31).setTouchable(false);
        this.menuControllerYio.getButtonById(32).setTouchable(false);
        for (int i = 0; i < 2; i++) {
            text.add(" ");
        }
        ButtonYio textPanel = this.buttonFactory.getButton(generateRectangle(0.0d, 0.0d, 1.0d, 1.0d), 50, null);
        textPanel.setPosition(generateRectangle(0.0d, 0.1d, 1.0d, 0.05d * ((double) text.size())));
        textPanel.cleatText();
        textPanel.addManyLines(text);
        this.menuControllerYio.getButtonRenderer().renderButton(textPanel);
        textPanel.setTouchable(false);
        textPanel.setAnimation(6);
        textPanel.appearFactor.appear(3, 1.0d);
        ButtonYio okButton = this.buttonFactory.getButton(generateRectangle(0.6d, 0.1d, 0.4d, SceneEditorInstruments.ICON_SIZE), 53, getString("end_game_ok"));
        okButton.setShadow(false);
        okButton.setReaction(Reaction.rbCloseTutorialTip);
        okButton.setAnimation(6);
        okButton.appearFactor.appear(3, 1.0d);
        okButton.disableTouchAnimation();
    }

    public void createTutorialTipWithFixedHeight(ArrayList<String> text, int lines) {
        this.menuControllerYio.getButtonById(31).setTouchable(false);
        this.menuControllerYio.getButtonById(32).setTouchable(false);
        for (int i = 0; i < 2; i++) {
            text.add(" ");
        }
        ButtonYio textPanel = this.buttonFactory.getButton(generateRectangle(0.0d, 0.0d, 1.0d, 1.0d), 50, null);
        textPanel.setPosition(generateRectangle(0.0d, 0.1d, 1.0d, 0.3d));
        textPanel.cleatText();
        textPanel.addManyLines(text);
        while (textPanel.text.size() < lines) {
            textPanel.addTextLine(" ");
        }
        this.menuControllerYio.getButtonRenderer().renderButton(textPanel);
        textPanel.setTouchable(false);
        textPanel.setAnimation(6);
        textPanel.appearFactor.appear(3, 1.0d);
        ButtonYio okButton = this.buttonFactory.getButton(generateRectangle(0.6d, 0.1d, 0.4d, SceneEditorInstruments.ICON_SIZE), 53, getString("end_game_ok"));
        okButton.setShadow(false);
        okButton.setReaction(Reaction.rbCloseTutorialTip);
        okButton.setAnimation(6);
        okButton.appearFactor.appear(3, 1.0d);
        okButton.disableTouchAnimation();
    }

    public void hide() {
        this.menuControllerYio.getButtonById(50).destroy();
        this.menuControllerYio.getButtonById(53).destroy();
        this.menuControllerYio.getButtonById(50).appearFactor.destroy(1, 3.0d);
        this.menuControllerYio.getButtonById(53).appearFactor.destroy(1, 3.0d);
        if (this.menuControllerYio.getButtonById(54) != null) {
            this.menuControllerYio.getButtonById(54).destroy();
            this.menuControllerYio.getButtonById(54).appearFactor.destroy(1, 3.0d);
        }
        this.menuControllerYio.getButtonById(30).setTouchable(true);
        this.menuControllerYio.getButtonById(31).setTouchable(true);
        this.menuControllerYio.getButtonById(32).setTouchable(true);
    }

    public void addHelpButtonToTutorialTip() {
        ButtonYio helpButton = this.buttonFactory.getButton(generateRectangle(0.0d, 0.1d, 0.6d, SceneEditorInstruments.ICON_SIZE), 54, null);
        helpButton.setTextLine(getString("help"));
        this.menuControllerYio.buttonRenderer.renderButton(helpButton);
        helpButton.setShadow(false);
        helpButton.setReaction(new C01481());
        helpButton.setAnimation(6);
        helpButton.appearFactor.appear(3, 1.0d);
        helpButton.disableTouchAnimation();
    }

    public void create() {
    }
}
