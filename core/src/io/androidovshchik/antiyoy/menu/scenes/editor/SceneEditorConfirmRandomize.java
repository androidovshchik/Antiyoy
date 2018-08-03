package io.androidovshchik.antiyoy.menu.scenes.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.editor.EditorReactions;
import yio.tro.antiyoy.menu.scenes.AbstractScene;

public class SceneEditorConfirmRandomize extends AbstractScene {
    public SceneEditorConfirmRandomize(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.hideAllEditorPanels();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, true);
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.025d, 0.15d, 0.95d, 0.16d), 520, null);
        if (basePanel.notRendered()) {
            basePanel.addTextLine(getString("confirm_randomize"));
            basePanel.addTextLine(" ");
            basePanel.addTextLine(" ");
            basePanel.addTextLine(" ");
            this.menuControllerYio.getButtonRenderer().renderButton(basePanel);
        }
        basePanel.setTouchable(false);
        basePanel.setAnimation(6);
        ButtonYio clearButton = this.buttonFactory.getButton(generateRectangle(0.5d, 0.15d, 0.475d, 0.06d), 521, getString("yes"));
        clearButton.setReaction(EditorReactions.rbEditorRandomize);
        clearButton.setShadow(false);
        clearButton.setAnimation(6);
        ButtonYio cancelButton = this.buttonFactory.getButton(generateRectangle(0.025d, 0.15d, 0.475d, 0.06d), 522, getString("cancel"));
        cancelButton.setReaction(EditorReactions.rbEditorHideConfirmRandomize);
        cancelButton.setShadow(false);
        cancelButton.setAnimation(6);
    }

    public void hide() {
        destroyByIndex(520, 529);
    }
}
