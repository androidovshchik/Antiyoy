package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SceneConfirmEndTurn extends AbstractGameplayScene {
    private ButtonYio basePanel;
    private ButtonYio cancelButton;
    private ButtonYio confirmButton;
    ButtonYio invisibleButton;

    public SceneConfirmEndTurn(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        createInvisibleButton();
        createBasePanel();
        createConfirmButton();
        createCancelButton();
    }

    private void createInvisibleButton() {
        this.invisibleButton = this.buttonFactory.getButton(generateRectangle(-0.1d, -0.1d, 0.01d, 0.01d), 323, " ");
        this.invisibleButton.setReaction(Reaction.rbNothing);
        this.invisibleButton.setShadow(false);
        this.invisibleButton.setAnimation(4);
        this.invisibleButton.setTouchOffset(2.0f * GraphicsYio.height);
    }

    private void forceElementsToTop() {
        forceElementToTop(this.basePanel);
        forceElementToTop(this.confirmButton);
        forceElementToTop(this.cancelButton);
    }

    private void createCancelButton() {
        this.cancelButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.12d, 0.4d, 0.06d), 322, getString("cancel"));
        this.cancelButton.setReaction(Reaction.rbHideEndTurnConfirm);
        this.cancelButton.setShadow(false);
        this.cancelButton.setAnimation(6);
        this.cancelButton.disableTouchAnimation();
    }

    private void createConfirmButton() {
        this.confirmButton = this.buttonFactory.getButton(generateRectangle(0.5d, 0.12d, 0.4d, 0.06d), 321, getString("yes"));
        this.confirmButton.setReaction(Reaction.rbEndTurn);
        this.confirmButton.setShadow(false);
        this.confirmButton.setAnimation(6);
        this.confirmButton.disableTouchAnimation();
    }

    private void createBasePanel() {
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.1d, 0.12d, 0.8d, 0.2d), 320, null);
        if (this.basePanel.notRendered()) {
            this.basePanel.addTextLine(getString("confirm_end_turn"));
            this.basePanel.addTextLine(" ");
            this.basePanel.addTextLine(" ");
            this.menuControllerYio.getButtonRenderer().renderButton(this.basePanel);
        }
        this.basePanel.setTouchable(false);
        this.basePanel.setAnimation(6);
    }

    public void hide() {
        for (int i = 320; i <= 329; i++) {
            ButtonYio b = this.menuControllerYio.getButtonById(i);
            if (b != null) {
                b.destroy();
                b.appearFactor.setValues(0.0d, 0.0d);
            }
        }
    }
}
