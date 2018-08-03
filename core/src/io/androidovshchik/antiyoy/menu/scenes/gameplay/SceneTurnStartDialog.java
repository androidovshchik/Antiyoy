package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.TurnStartDialog;

public class SceneTurnStartDialog extends AbstractGameplayScene {
    public TurnStartDialog dialog = null;

    public SceneTurnStartDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        initDialogOnce();
        forceElementToTop(this.dialog);
        this.dialog.appear();
    }

    private void initDialogOnce() {
        if (this.dialog == null) {
            this.dialog = new TurnStartDialog(this.menuControllerYio);
            this.dialog.setPosition(generateRectangle(0.0d, 0.0d, 1.0d, 1.0d));
            this.menuControllerYio.addElementToScene(this.dialog);
        }
    }

    public void hide() {
        this.dialog.destroy();
    }
}
