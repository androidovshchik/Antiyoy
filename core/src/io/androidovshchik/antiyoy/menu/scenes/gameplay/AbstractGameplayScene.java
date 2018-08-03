package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.scenes.AbstractScene;

public abstract class AbstractGameplayScene extends AbstractScene {
    public abstract void create();

    public abstract void hide();

    public AbstractGameplayScene(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }
}
