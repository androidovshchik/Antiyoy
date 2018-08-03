package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.FireworksElement.FireworksElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class SceneFireworks extends AbstractScene {
    FireworksElement fireworksElement = null;

    public SceneFireworks(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, true, true);
        createFireworks();
        this.menuControllerYio.spawnBackButton(560, Reaction.rbMainMenu);
        this.menuControllerYio.endMenuCreation();
    }

    private void createFireworks() {
        if (this.fireworksElement == null) {
            this.fireworksElement = new FireworksElement(this.menuControllerYio, -1);
            this.fireworksElement.position.set(0.0d, 0.0d, (double) GraphicsYio.width, (double) GraphicsYio.height);
            this.menuControllerYio.addElementToScene(this.fireworksElement);
        }
        this.fireworksElement.appear();
    }
}
