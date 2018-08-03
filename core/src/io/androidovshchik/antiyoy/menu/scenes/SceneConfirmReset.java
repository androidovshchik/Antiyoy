package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.net.HttpStatus;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneConfirmReset extends AbstractScene {
    public SceneConfirmReset(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, true);
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.4d, 0.9d, 0.2d), HttpStatus.SC_GONE, null);
        if (basePanel.notRendered()) {
            basePanel.addTextLine(getString("confirm_reset"));
            basePanel.addTextLine(" ");
            basePanel.addTextLine(" ");
            this.menuControllerYio.getButtonRenderer().renderButton(basePanel);
        }
        basePanel.setTouchable(false);
        basePanel.setAnimation(5);
        ButtonYio restartButton = this.buttonFactory.getButton(generateRectangle(0.5d, 0.4d, 0.45d, SceneEditorInstruments.ICON_SIZE), HttpStatus.SC_LENGTH_REQUIRED, getString("menu_reset"));
        restartButton.setReaction(Reaction.rbResetProgress);
        restartButton.setShadow(false);
        restartButton.setAnimation(5);
        ButtonYio cancelButton = this.buttonFactory.getButton(generateRectangle(0.05d, 0.4d, 0.45d, SceneEditorInstruments.ICON_SIZE), HttpStatus.SC_PRECONDITION_FAILED, getString("cancel"));
        cancelButton.setReaction(Reaction.rbMoreSettings);
        cancelButton.setShadow(false);
        cancelButton.setAnimation(5);
        this.menuControllerYio.endMenuCreation();
    }
}
