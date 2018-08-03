package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;

public abstract class AbstractGameplayScene extends AbstractScene {
    public abstract void create();

    public abstract void hide();

    public AbstractGameplayScene(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }
}
