package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;

public class SceneTestMenu extends AbstractScene {
    public SceneTestMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, false, true);
        this.menuControllerYio.spawnBackButton(38721132, Reaction.rbChooseGameModeMenu);
        this.menuControllerYio.endMenuCreation();
    }
}
