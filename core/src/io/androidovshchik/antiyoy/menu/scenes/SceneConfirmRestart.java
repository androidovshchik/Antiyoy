package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneConfirmRestart extends AbstractScene {
    public SceneConfirmRestart(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, true);
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.4d, 0.9d, 0.2d), 220, null);
        if (basePanel.notRendered()) {
            basePanel.addTextLine(getString("confirm_restart"));
            basePanel.addTextLine(" ");
            basePanel.addTextLine(" ");
            this.menuControllerYio.getButtonRenderer().renderButton(basePanel);
        }
        basePanel.setTouchable(false);
        basePanel.setAnimation(5);
        ButtonYio restartButton = this.buttonFactory.getButton(generateRectangle(0.5d, 0.4d, 0.45d, SceneEditorInstruments.ICON_SIZE), 221, getString("in_game_menu_restart"));
        restartButton.setReaction(Reaction.rbRestartGame);
        restartButton.setShadow(false);
        restartButton.setAnimation(5);
        ButtonYio cancelButton = this.buttonFactory.getButton(generateRectangle(0.05d, 0.4d, 0.45d, SceneEditorInstruments.ICON_SIZE), 222, getString("cancel"));
        cancelButton.setReaction(Reaction.rbPauseMenu);
        cancelButton.setShadow(false);
        cancelButton.setAnimation(5);
        this.menuControllerYio.endMenuCreation();
    }
}
