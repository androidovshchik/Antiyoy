package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.diplomatic_dialogs.DipInfoDialog;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SceneDipInfoDialog extends AbstractGameplayScene {
    public DipInfoDialog dialog = null;

    public SceneDipInfoDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        if (this.dialog == null) {
            initDipInfoDialog();
        }
        this.dialog.appear();
    }

    private void initDipInfoDialog() {
        this.dialog = new DipInfoDialog(this.menuControllerYio);
        this.dialog.setPosition(generateRectangle(0.0d, 0.15d, 1.0d, GraphicsYio.convertToHeight(0.6d)));
        this.menuControllerYio.addElementToScene(this.dialog);
    }

    public void hide() {
        this.dialog.destroy();
    }
}
