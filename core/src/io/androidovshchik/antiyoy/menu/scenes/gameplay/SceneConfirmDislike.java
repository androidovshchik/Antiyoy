package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.diplomatic_dialogs.ConfirmDislikeDialog;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class SceneConfirmDislike extends AbstractGameplayScene {
    public ConfirmDislikeDialog dialog = null;

    public SceneConfirmDislike(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        if (this.dialog == null) {
            initDialog();
        }
        this.dialog.appear();
    }

    private void initDialog() {
        this.dialog = new ConfirmDislikeDialog(this.menuControllerYio);
        this.dialog.setPosition(generateRectangle(0.0d, 0.15d, 1.0d, GraphicsYio.convertToHeight(0.35d)));
        this.menuControllerYio.addElementToScene(this.dialog);
    }

    public void hide() {
        this.dialog.destroy();
    }
}
