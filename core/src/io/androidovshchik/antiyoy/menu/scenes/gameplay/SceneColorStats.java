package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneColorStats extends AbstractGameplayScene {
    public SceneColorStats(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.getYioGdxGame().gameController.selectionController.deselectAll();
        this.menuControllerYio.getButtonById(30).setTouchable(false);
        this.menuControllerYio.getButtonById(31).setTouchable(false);
        this.menuControllerYio.getButtonById(32).setTouchable(false);
        ButtonYio showPanel = this.buttonFactory.getButton(generateRectangle(0.0d, 0.1d, 1.0d, 0.41d), 56321, null);
        showPanel.setTouchable(false);
        showPanel.setAnimation(6);
        showPanel.appearFactor.appear(3, 1.0d);
        ButtonYio okButton = this.buttonFactory.getButton(generateRectangle(0.6d, 0.1d, 0.4d, SceneEditorInstruments.ICON_SIZE), 56322, getString("end_game_ok"));
        okButton.setShadow(false);
        okButton.setReaction(Reaction.rbHideColorStats);
        okButton.setAnimation(6);
        okButton.appearFactor.appear(3, 1.0d);
    }

    public void hide() {
        this.menuControllerYio.getButtonById(30).setTouchable(true);
        this.menuControllerYio.getButtonById(31).setTouchable(true);
        this.menuControllerYio.getButtonById(32).setTouchable(true);
        this.menuControllerYio.getButtonById(56321).destroy();
        this.menuControllerYio.getButtonById(56321).appearFactor.destroy(1, 3.0d);
        this.menuControllerYio.getButtonById(56322).destroy();
        this.menuControllerYio.getButtonById(56322).appearFactor.destroy(1, 3.0d);
    }
}
