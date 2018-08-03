package io.androidovshchik.antiyoy.menu.scenes.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.editor.EditorReactions;
import yio.tro.antiyoy.menu.scenes.AbstractScene;

public class SceneEditorConfirmClear extends AbstractScene {
    public SceneEditorConfirmClear(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.hideAllEditorPanels();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, true);
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.025d, 0.15d, 0.95d, 0.2d), 360, null);
        if (basePanel.notRendered()) {
            basePanel.addTextLine(getString("confirm_clear_level"));
            basePanel.addTextLine(" ");
            basePanel.addTextLine(" ");
            this.menuControllerYio.getButtonRenderer().renderButton(basePanel);
        }
        basePanel.setTouchable(false);
        basePanel.setAnimation(6);
        ButtonYio clearButton = this.buttonFactory.getButton(generateRectangle(0.5d, 0.15d, 0.475d, 0.06d), 361, getString("editor_clear"));
        clearButton.setReaction(EditorReactions.rbClearEditorLevel);
        clearButton.setShadow(false);
        clearButton.setAnimation(6);
        ButtonYio cancelButton = this.buttonFactory.getButton(generateRectangle(0.025d, 0.15d, 0.475d, 0.06d), 362, getString("cancel"));
        cancelButton.setReaction(EditorReactions.rbEditorHideConfirmClearLevelMenu);
        cancelButton.setShadow(false);
        cancelButton.setAnimation(6);
    }

    public void hide() {
        for (int i = 360; i <= 362; i++) {
            this.menuControllerYio.getButtonById(i).destroy();
        }
    }
}
