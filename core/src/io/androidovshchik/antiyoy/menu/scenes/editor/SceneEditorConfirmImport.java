package io.androidovshchik.antiyoy.menu.scenes.editor;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.editor.EditorReactions;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;

public class SceneEditorConfirmImport extends AbstractScene {
    public SceneEditorConfirmImport(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, true);
        ButtonYio labelButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.45d, 0.8d, 0.2d), 350, null);
        if (labelButton.notRendered()) {
            labelButton.cleatText();
            this.menuControllerYio.renderTextAndSomeEmptyLines(labelButton, getString("confirm_import"), 3);
        }
        labelButton.setTouchable(false);
        labelButton.setAnimation(5);
        ButtonYio noButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.45d, 0.4d, 0.08d), 351, getString("no"));
        noButton.setReaction(EditorReactions.rbEditorActionsMenu);
        noButton.setShadow(false);
        noButton.setAnimation(5);
        ButtonYio yesButton = this.buttonFactory.getButton(generateRectangle(0.5d, 0.45d, 0.4d, 0.08d), 352, getString("yes"));
        yesButton.setReaction(EditorReactions.rbEditorImport);
        yesButton.setShadow(false);
        yesButton.setAnimation(5);
        this.menuControllerYio.endMenuCreation();
    }
}
