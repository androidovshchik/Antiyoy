package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.ButtonFactory;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public abstract class AbstractScene {
    protected ButtonFactory buttonFactory;
    protected final MenuControllerYio menuControllerYio;

    public abstract void create();

    public AbstractScene(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        this.buttonFactory = menuControllerYio.getButtonFactory();
    }

    protected void destroyByIndex(int startIndex, int endIndex) {
        for (int index = startIndex; index <= endIndex; index++) {
            this.menuControllerYio.destroyButton(index);
        }
    }

    public RectangleYio generateRectangle(double x, double y, double width, double height) {
        return this.menuControllerYio.generateRectangle(x, y, width, height);
    }

    public RectangleYio generateSquare(double x, double y, double size) {
        return this.menuControllerYio.generateSquare(x, y, size);
    }

    public String getString(String key) {
        return LanguagesManager.getInstance().getString(key);
    }

    public void forceElementToTop(InterfaceElement element) {
        this.menuControllerYio.removeElementFromScene(element);
        this.menuControllerYio.addElementToScene(element);
    }

    public void forceElementToTop(ButtonYio buttonYio) {
        this.menuControllerYio.removeButtonById(buttonYio.id);
        this.menuControllerYio.addButtonToArray(buttonYio);
    }
}
