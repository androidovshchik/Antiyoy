package io.androidovshchik.antiyoy.menu.scenes.editor;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;

public abstract class AbstractEditorPanel extends AbstractScene {
    public abstract void create();

    public abstract void hide();

    public abstract boolean isCurrentlyOpened();

    public AbstractEditorPanel(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void onTumblerButtonPressed() {
        if (isCurrentlyOpened()) {
            hide();
            return;
        }
        this.menuControllerYio.hideAllEditorPanels();
        create();
    }
}
