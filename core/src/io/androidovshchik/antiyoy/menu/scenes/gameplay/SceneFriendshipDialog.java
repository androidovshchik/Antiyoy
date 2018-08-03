package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.diplomatic_dialogs.FriendshipDialog;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class SceneFriendshipDialog extends AbstractGameplayScene {
    public FriendshipDialog dialog = null;

    public SceneFriendshipDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        if (this.dialog == null) {
            initDialog();
        }
        forceElementToTop(this.dialog);
        this.dialog.appear();
    }

    private void initDialog() {
        this.dialog = new FriendshipDialog(this.menuControllerYio);
        this.dialog.setPosition(generateRectangle(0.0d, 0.15d, 1.0d, GraphicsYio.convertToHeight(0.6d)));
        this.menuControllerYio.addElementToScene(this.dialog);
    }

    public void hide() {
        this.dialog.destroy();
    }
}
